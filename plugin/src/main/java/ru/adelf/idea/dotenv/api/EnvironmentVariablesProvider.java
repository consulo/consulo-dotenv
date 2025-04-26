package ru.adelf.idea.dotenv.api;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.language.psi.PsiFile;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.models.KeyValuePsiElement;

import java.util.Collection;

@ExtensionAPI(ComponentScope.APPLICATION)
public interface EnvironmentVariablesProvider {
    @NotNull
    FileAcceptResult acceptFile(VirtualFile file);

    @NotNull
    Collection<KeyValuePsiElement> getElements(PsiFile psiFile);
}
