package ru.adelf.idea.dotenv;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiFile;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesProvider;
import ru.adelf.idea.dotenv.api.model.FileAcceptResult;
import ru.adelf.idea.dotenv.api.model.KeyValuePsiElement;
import ru.adelf.idea.dotenv.psi.DotEnvFile;

import java.util.Collection;
import java.util.Collections;

@ExtensionImpl
public class DotEnvVariablesProvider implements EnvironmentVariablesProvider {
    @Override
    public @NotNull FileAcceptResult acceptFile(VirtualFile file) {
        if(!file.getFileType().equals(DotEnvFileType.INSTANCE)) {
            return FileAcceptResult.NOT_ACCEPTED;
        }

        // .env.dist , .env.example files are secondary
        return file.getName().equals(".env") ? FileAcceptResult.ACCEPTED : FileAcceptResult.ACCEPTED_SECONDARY;
    }

    @Override
    public @NotNull Collection<KeyValuePsiElement> getElements(PsiFile psiFile) {
        if(psiFile instanceof DotEnvFile) {
            DotEnvPsiElementsVisitor visitor = new DotEnvPsiElementsVisitor();
            psiFile.acceptChildren(visitor);

            return visitor.getCollectedItems();
        }

        return Collections.emptyList();
    }
}
