package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.psi.DotEnvFile;
import ru.adelf.idea.dotenv.psi.DotEnvKey;

@ExtensionImpl
public class LeadingCharacterInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameInvalidLeadingCharacter();
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

        PsiTreeUtil.findChildrenOfType(file, DotEnvKey.class).forEach(dotEnvKey -> {
            // Also accepts lower case chars as keys with lower case chars are handled by another inspection
            // same for dash (-> IncorrectDelimiter
            if (!dotEnvKey.getText().matches("[A-Za-z_-].*")) {
                problemsHolder.registerProblem(dotEnvKey,
                    DotEnvLocalize.inspectionMessageInvalidFirstCharForKeyOnlyZAreAllowed().get());
            }
        });

        return problemsHolder;
    }

}
