package ru.adelf.idea.dotenv;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiRecursiveElementVisitor;
import consulo.virtualFileSystem.VirtualFile;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesUsagesProvider;
import ru.adelf.idea.dotenv.models.KeyUsagePsiElement;
import ru.adelf.idea.dotenv.psi.DotEnvNestedVariableKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ExtensionImpl
public class DotEnvNestedVariablesProvider implements EnvironmentVariablesUsagesProvider {

    @Override
    public boolean acceptFile(VirtualFile file) {
        return file.getFileType() == DotEnvFileType.INSTANCE;
    }

    @Override
    public Collection<KeyUsagePsiElement> getUsages(PsiFile psiFile) {
        Set<KeyUsagePsiElement> collectedKeys = new HashSet<>();
        psiFile.acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof DotEnvNestedVariableKey) {
                    collectedKeys.add(new KeyUsagePsiElement(element.getText(), element));
                }
                super.visitElement(element);
            }
        });
        return collectedKeys;
    }
}
