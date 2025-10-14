package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtensionImpl
public class ExtraBlankLineInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameExtraBlankLine();
    }

    @Override
    public boolean runForWholeFile() {
        return true;
    }

    @Override
    @Nullable
    public ProblemDescriptor [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!(file instanceof DotEnvFile)) {
            return null;
        }

        return analyzeFile(file, manager, isOnTheFly).getResultsArray();
    }

    private static @NotNull ProblemsHolder analyzeFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder problemsHolder = manager.createProblemsHolder(file, isOnTheFly);

        PsiTreeUtil.findChildrenOfType(file, PsiWhiteSpaceImpl.class).forEach(whiteSpace -> {
            Pattern pattern = Pattern.compile("\r\n|\r|\n");
            Matcher matcher = pattern.matcher(whiteSpace.getText());

            int count = 0;
            while (matcher.find())
                count++;

            if (count > 2) {
                problemsHolder.registerProblem(whiteSpace,
                    DotEnvLocalize.inspectionMessageOnlyOneExtraLineAllowedBetweenProperties().get(),
                    new RemoveExtraBlankLineQuickFix());
            }
        });

        return problemsHolder;
    }

    private static class RemoveExtraBlankLineQuickFix implements LocalQuickFix {

        @Override
        public @NotNull LocalizeValue getName() {
            return DotEnvLocalize.intentionNameRemoveExtraBlankLine();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PsiElement psiElement = descriptor.getPsiElement();

                PsiElement newPsiElement = DotEnvFactory.createFromText(project, TokenType.WHITE_SPACE, "\n\n");

                psiElement.replace(newPsiElement);
            } catch (IncorrectOperationException e) {
                Logger.getInstance(ExtraBlankLineInspection.class).error(e);
            }
        }
    }
}
