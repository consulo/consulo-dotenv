package ru.adelf.idea.dotenv;

import consulo.language.Language;

public class DotEnvLanguage extends Language {
    public static final DotEnvLanguage INSTANCE = new DotEnvLanguage();

    private DotEnvLanguage() {
        super("DotEnv");
    }

    @Override
    public String getDisplayName() {
        return ".env";
    }
}
