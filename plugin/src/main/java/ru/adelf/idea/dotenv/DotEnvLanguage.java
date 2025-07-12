package ru.adelf.idea.dotenv;

import consulo.language.Language;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

public class DotEnvLanguage extends Language {
    public static final DotEnvLanguage INSTANCE = new DotEnvLanguage();

    private DotEnvLanguage() {
        super("DotEnv");
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO(".env");
    }
}
