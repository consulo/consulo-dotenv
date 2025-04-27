package ru.adelf.idea.dotenv.java;

import com.intellij.java.language.impl.JavaFileType;
import com.intellij.java.language.psi.PsiJavaFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiFile;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesUsagesProvider;
import ru.adelf.idea.dotenv.api.model.KeyUsagePsiElement;

import java.util.Collection;
import java.util.Collections;

@ExtensionImpl
public class JavaEnvironmentVariablesUsagesProvider implements EnvironmentVariablesUsagesProvider {
    @Override
    public boolean acceptFile(VirtualFile file) {
        return file.getFileType().equals(JavaFileType.INSTANCE);
    }

    @Override
    public @NotNull Collection<KeyUsagePsiElement> getUsages(PsiFile psiFile) {
        if (psiFile instanceof PsiJavaFile) {
            JavaEnvironmentCallsVisitor visitor = new JavaEnvironmentCallsVisitor();
            psiFile.acceptChildren(visitor);

            return visitor.getCollectedItems();
        }

        return Collections.emptyList();
    }
}
