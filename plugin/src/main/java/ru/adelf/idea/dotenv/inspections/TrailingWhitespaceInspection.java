package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.ast.TokenType;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.impl.psi.PsiWhiteSpaceImpl;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvFactory;
import ru.adelf.idea.dotenv.psi.DotEnvFile;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;
import ru.adelf.idea.dotenv.psi.DotEnvValue;
import ru.adelf.idea.dotenv.psi.impl.DotEnvValueImpl;

@ExtensionImpl
public class TrailingWhitespaceInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameValueHasTrailingWhitespace();
    }

    @Override
    @Nullable
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!(file instanceof DotEnvFile)) {
            return null;
        }

        return analyzeFile(file, manager, isOnTheFly).getResultsArray();
    }

    private static @NotNull ProblemsHolder analyzeFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder problemsHolder = manager.createProblemsHolder(file, isOnTheFly);

        PsiTreeUtil.findChildrenOfType(file, DotEnvValue.class).forEach(dotEnvValue -> {
            if (dotEnvValue.getText().matches(".*[ \\t]+")) {
                problemsHolder.registerProblem(dotEnvValue,
                    new TextRange(dotEnvValue.getText().stripTrailing().length(), dotEnvValue.getText().length()),
                    DotEnvLocalize.inspectionMessageLineHasTrailingWhitespace().get(),
                    new TrailingWhitespaceInspection.RemoveTrailingWhitespaceQuickFix()
                );
            }
        });

        PsiTreeUtil.findChildrenOfType(file, PsiWhiteSpaceImpl.class).forEach(whiteSpace -> {
            if (whiteSpace.getText().matches("\\s*[ \\t]\\n\\s*")) {
                problemsHolder.registerProblem(whiteSpace,
                    DotEnvLocalize.inspectionMessageLineHasTrailingWhitespace().get(),
                    new TrailingWhitespaceInspection.RemoveTrailingWhitespaceQuickFix()
                );
            }
        });

        return problemsHolder;
    }

    private static class RemoveTrailingWhitespaceQuickFix implements LocalQuickFix {

        @Override
        public @NotNull LocalizeValue getName() {
            return DotEnvLocalize.intentionNameRemoveTrailingWhitespace();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PsiElement psiElement = descriptor.getPsiElement();

                if (psiElement instanceof DotEnvValueImpl) {
                    PsiElement newPsiElement = DotEnvFactory.createFromText(project, DotEnvTypes.VALUE,
                        "DUMMY_KEY=" + psiElement.getText().stripTrailing());
                    psiElement.replace(newPsiElement);
                }
                else if (psiElement instanceof PsiWhiteSpaceImpl) {
                    PsiElement newPsiElement = DotEnvFactory.createFromText(project, TokenType.WHITE_SPACE,
                        "DUMMY_KEY='VALUE'" + psiElement.getText().replaceAll("[ \\t]*\\n", "\n"));
                    psiElement.replace(newPsiElement);
                }
            }
            catch (IncorrectOperationException e) {
                Logger.getInstance(IncorrectDelimiterInspection.class).error(e);
            }
        }
    }
}
