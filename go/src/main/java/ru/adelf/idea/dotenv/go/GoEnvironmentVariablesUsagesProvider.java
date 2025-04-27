package ru.adelf.idea.dotenv.go;

import com.goide.GoFileType;
import com.goide.psi.GoFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiFile;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesUsagesProvider;
import ru.adelf.idea.dotenv.api.model.KeyUsagePsiElement;

import java.util.Collection;
import java.util.Collections;

@ExtensionImpl
public class GoEnvironmentVariablesUsagesProvider implements EnvironmentVariablesUsagesProvider {
    @Override
    public boolean acceptFile(VirtualFile file) {
        return file.getFileType().equals(GoFileType.INSTANCE);
    }

    @Override
    public @NotNull Collection<KeyUsagePsiElement> getUsages(PsiFile psiFile) {
        if(psiFile instanceof GoFile) {
            GoEnvironmentCallsVisitor visitor = new GoEnvironmentCallsVisitor();
            psiFile.acceptChildren(visitor);

            return visitor.getCollectedItems();
        }

        return Collections.emptyList();
    }
}
