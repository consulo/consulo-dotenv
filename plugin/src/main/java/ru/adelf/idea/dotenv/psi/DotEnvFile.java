package ru.adelf.idea.dotenv.psi;

import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.virtualFileSystem.fileType.FileType;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.DotEnvFileType;
import ru.adelf.idea.dotenv.DotEnvLanguage;

public class DotEnvFile extends PsiFileBase {
    public DotEnvFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DotEnvLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public FileType getFileType() {
        return DotEnvFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return ".env file";
    }
}
