package ru.adelf.idea.dotenv.inspections;

import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.localize.LocalizeValue;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import ru.adelf.idea.dotenv.indexing.DotEnvKeyValuesIndex;
import ru.adelf.idea.dotenv.psi.DotEnvNestedVariableKey;

public class UndefinedNestedVariableInspection extends DotEnvLocalInspectionTool {

    @Override
    public PsiElementVisitor buildVisitor(final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof DotEnvNestedVariableKey) {
                    DotEnvNestedVariableKey key = (DotEnvNestedVariableKey) element;
                    boolean isUndefinedProperty = FileBasedIndex.getInstance().processValues(
                        DotEnvKeyValuesIndex.KEY,
                        element.getText(),
                        null,
                        new FileBasedIndex.ValueProcessor<String>() {
                            @Override
                            public boolean process(VirtualFile file, String value) {
                                return false;
                            }
                        },
                        GlobalSearchScope.allScope(element.getProject())
                    );
                    if (isUndefinedProperty) {
                        holder.registerProblem(
                            element,
                            DotEnvLocalize.inspectionNameUndefinedNested0Variable(element.getText()).get()
                        );
                    }
                }
                super.visitElement(element);
            }
        };
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameUndefinedNestedVariable();
    }
}
