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
import consulo.logging.Logger;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvFactory;
import ru.adelf.idea.dotenv.psi.DotEnvFile;
import ru.adelf.idea.dotenv.psi.DotEnvKey;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;

import java.util.Locale;

@ExtensionImpl
public class LowercaseKeyInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull String getDisplayName() {
        return DotEnvLocalize.inspectionNameKeyUsesLowercaseChars().get();
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

        PsiTreeUtil.findChildrenOfType(file, DotEnvKey.class).forEach(dotEnvKey -> {
            if (dotEnvKey.getText().matches(".*[a-z].*")) {
                problemsHolder.registerProblem(dotEnvKey,
                    DotEnvLocalize.inspectionMessageKeyUsesLowercaseCharsOnlyKeysWithUppercaseCharsAreAllowed().get()/*,
                        new ForceUppercaseQuickFix()*/
                );
            }
        });

        return problemsHolder;
    }

    private static class ForceUppercaseQuickFix implements LocalQuickFix {

        @Override
        public @NotNull String getName() {
            return DotEnvLocalize.intentionNameChangeToUppercase().get();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PsiElement psiElement = descriptor.getPsiElement();

                PsiElement newPsiElement = DotEnvFactory.createFromText(project, DotEnvTypes.KEY,
                    psiElement.getText().toUpperCase(Locale.ROOT) + "=dummy");

                psiElement.replace(newPsiElement);
            }
            catch (IncorrectOperationException e) {
                Logger.getInstance(IncorrectDelimiterInspection.class).error(e);
            }
        }

        @Override
        public @NotNull String getFamilyName() {
            return getName();
        }
    }
}
