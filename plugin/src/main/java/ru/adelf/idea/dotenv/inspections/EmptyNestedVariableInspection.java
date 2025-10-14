package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import ru.adelf.idea.dotenv.DotEnvBundle;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;

@ExtensionImpl
public class EmptyNestedVariableInspection extends DotEnvLocalInspectionTool {

    @Override
    public PsiElementVisitor buildVisitor(final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element.getNode().getElementType() == DotEnvTypes.NESTED_VARIABLE_START
                    && element.getNextSibling() != null
                    && element.getNextSibling().getNode().getElementType() == DotEnvTypes.NESTED_VARIABLE_END) {
                    holder.registerProblem(
                        element,
                        DotEnvBundle.message("inspection.name.empty.nested.variable")
                    );
                }
                super.visitElement(element);
            }
        };
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameEmptyNestedVariable();
    }
}
