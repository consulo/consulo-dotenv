package ru.adelf.idea.dotenv.api;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.language.psi.PsiFile;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.models.KeyUsagePsiElement;

import java.util.Collection;

@ExtensionAPI(ComponentScope.APPLICATION)
public interface EnvironmentVariablesUsagesProvider {
    boolean acceptFile(VirtualFile file);

    @NotNull
    Collection<KeyUsagePsiElement> getUsages(PsiFile psiFile);
}
