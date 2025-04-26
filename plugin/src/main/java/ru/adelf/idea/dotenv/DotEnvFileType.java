package ru.adelf.idea.dotenv;

import consulo.dotenv.icon.DotEnvIconGroup;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DotEnvFileType extends LanguageFileType {
    public static final DotEnvFileType INSTANCE = new DotEnvFileType();

    private DotEnvFileType() {
        super(DotEnvLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getId() {
        return "DOT_ENV_FILE";
    }

    @Nonnull
    @Override
    public LocalizeValue getDescription() {
        return DotEnvLocalize.labelEnvFile();
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return "env";
    }

    @Override
    @Nullable
    public Image getIcon() {
        return DotEnvIconGroup.env();
    }
}