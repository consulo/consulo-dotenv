package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
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
import ru.adelf.idea.dotenv.psi.DotEnvProperty;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;

import java.util.regex.Pattern;

@ExtensionImpl
public class SpaceAroundSeparatorInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull String getDisplayName() {
        return DotEnvLocalize.inspectionNameExtraSpacesSurrounding().get();
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

        PsiTreeUtil.findChildrenOfType(file, DotEnvProperty.class).forEach(dotEnvProperty -> {

            String key = dotEnvProperty.getKey().getText();
            String value = dotEnvProperty.getValue() != null ? dotEnvProperty.getValue().getText() : "";
            String separator = dotEnvProperty.getText()
                .replaceFirst("^" + Pattern.quote(key), "")
                .replaceFirst(Pattern.quote(value) + "$", "");

            if (separator.matches("([ \t]+=.*)|(.*=[ \t]+)")) {
                problemsHolder.registerProblem(dotEnvProperty, new TextRange(dotEnvProperty.getKey().getText().length(),
                        dotEnvProperty.getKey().getText().length() +
                            separator.length()),
                    DotEnvLocalize.inspectionMessageExtraSpacesSurrounding().get(),
                    new RemoveSpaceAroundSeparatorQuickFix());
            }
        });

        return problemsHolder;
    }

    private static class RemoveSpaceAroundSeparatorQuickFix implements LocalQuickFix {

        @Override
        public @NotNull String getName() {
            return DotEnvLocalize.intentionNameRemoveSpacesSurrounding().get();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                DotEnvProperty dotEnvProperty = (DotEnvProperty) descriptor.getPsiElement();

                String key = dotEnvProperty.getKey().getText();
                String value = dotEnvProperty.getValue() != null ? dotEnvProperty.getValue().getText() : "";

                PsiElement newPsiElement = DotEnvFactory.createFromText(
                    project,
                    DotEnvTypes.PROPERTY,
                    key + "=" + value
                );

                dotEnvProperty.replace(newPsiElement);
            }
            catch (IncorrectOperationException e) {
                Logger.getInstance(ExtraBlankLineInspection.class).error(e);
            }
        }

        @Override
        public @NotNull String getFamilyName() {
            return getName();
        }
    }
}
