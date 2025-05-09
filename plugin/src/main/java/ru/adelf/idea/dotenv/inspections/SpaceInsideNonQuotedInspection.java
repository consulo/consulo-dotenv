package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
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
import ru.adelf.idea.dotenv.psi.DotEnvTypes;
import ru.adelf.idea.dotenv.psi.DotEnvValue;

import java.util.function.Supplier;
import java.util.stream.Stream;

@ExtensionImpl
public class SpaceInsideNonQuotedInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull String getDisplayName() {
        return DotEnvLocalize.inspectionNameSpaceInsideNonQuotedValue().get();
    }

    private final AddQuotesQuickFix addQuotesQuickFix = new AddQuotesQuickFix();

    @Override
    public boolean runForWholeFile() {
        return true;
    }

    @Override
    @Nullable
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!(file instanceof DotEnvFile)) {
            return null;
        }

        return analyzeFile(file, manager, isOnTheFly).getResultsArray();
    }

    private @NotNull ProblemsHolder analyzeFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder problemsHolder = manager.createProblemsHolder(file, isOnTheFly);

        PsiTreeUtil.findChildrenOfType(file, DotEnvValue.class).forEach(dotEnvValue -> {
            // first child VALUE_CHARS -> non quoted value
            // first child QUOTE -> quoted value
            if(dotEnvValue.getFirstChild().getNode().getElementType() == DotEnvTypes.VALUE_CHARS) {
                if (dotEnvValue.getText().trim().contains(" ")) {
                    problemsHolder.registerProblem(dotEnvValue,
                        DotEnvLocalize.inspectionMessageSpaceInsideAllowedOnlyForQuotedValues().get(), addQuotesQuickFix);
                }
            }
        });

        return problemsHolder;
    }

    private static class AddQuotesQuickFix implements LocalQuickFix {

        @Override
        public @NotNull String getName() {
            return DotEnvLocalize.intentionNameAddQuotes().get();
        }

        /**
         * Adds quotes to DotEnvValue element
         *
         * @param project    The project that contains the file being edited.
         * @param descriptor A problem found by this inspection.
         */
        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {

            // counting each quote type " AND '. The quickfix will use the most common quote type.
            String quote;
            Supplier<Stream<DotEnvValue>> supplier = () -> PsiTreeUtil.findChildrenOfType(descriptor.getPsiElement().getContainingFile(), DotEnvValue.class)
                    .stream()
                    .filter(dotEnvValue -> dotEnvValue.getFirstChild().getNode().getElementType() == DotEnvTypes.QUOTE);
            long total = supplier.get().count();
            long doubleQuoted = supplier.get().filter(dotEnvValue -> dotEnvValue.getFirstChild().getText().contains("\"")).count();
            long singleQuoted = total - doubleQuoted;
            if (doubleQuoted > singleQuoted) {
                quote = "\"";
            } else {
                quote = "'";
            }

            try {
                DotEnvValue valueElement = (DotEnvValue) descriptor.getPsiElement();

                PsiElement newValueElement = DotEnvFactory.createFromText(project, DotEnvTypes.VALUE, "DUMMY=" + quote + valueElement.getText() + quote);

                valueElement.getNode().getTreeParent().replaceChild(valueElement.getNode(), newValueElement.getNode());
            } catch (IncorrectOperationException e) {
                Logger.getInstance(SpaceInsideNonQuotedInspection.class).error(e);
            }
        }

        @Override
        public @NotNull String getFamilyName() {
            return getName();
        }
    }
}
