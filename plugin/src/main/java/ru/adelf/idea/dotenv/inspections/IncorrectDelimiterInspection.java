package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvBundle;
import ru.adelf.idea.dotenv.DotEnvFactory;
import ru.adelf.idea.dotenv.psi.DotEnvFile;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;
import ru.adelf.idea.dotenv.psi.impl.DotEnvKeyImpl;

@ExtensionImpl
public class IncorrectDelimiterInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameIncorrectDelimiter();
    }

    @Override
    public boolean runForWholeFile() {
        return true;
    }

    @Override
    public @Nullable ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!(file instanceof DotEnvFile)) {
            return null;
        }

        return analyzeFile(file, manager, isOnTheFly).getResultsArray();
    }

    private static @NotNull ProblemsHolder analyzeFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder problemsHolder = manager.createProblemsHolder(file, isOnTheFly);

        PsiTreeUtil.findChildrenOfType(file, DotEnvKeyImpl.class).forEach(key -> {
            if (key.getText().contains("-")) {
                problemsHolder.registerProblem(key,
                    DotEnvLocalize.inspectionMessageExpectedFound().get()/*, new ReplaceDelimiterQuickFix()*/);
            }
        });

        return problemsHolder;
    }

    private static class ReplaceDelimiterQuickFix implements LocalQuickFix {

        @Override
        public @NotNull LocalizeValue getName() {
            return DotEnvLocalize.intentionNameReplaceDelimiter();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PsiElement psiElement = descriptor.getPsiElement();

                PsiElement newPsiElement = DotEnvFactory.createFromText(project, DotEnvTypes.KEY,
                        psiElement.getText().replace("-","_")+"=dummy");

                psiElement.replace(newPsiElement);
            } catch (IncorrectOperationException e) {
                Logger.getInstance(IncorrectDelimiterInspection.class).error(e);
            }
        }
    }
}
