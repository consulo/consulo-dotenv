package ru.adelf.idea.dotenv.api;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.application.Application;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2025-04-27
 */
@ServiceAPI(ComponentScope.APPLICATION)
public interface DotEnvSettings {
    @Nonnull
    static DotEnvSettings getInstance() {
        return Application.get().getInstance(DotEnvSettings.class);
    }

    boolean isCompletionEnabled();

    boolean isStoreValues();

    boolean isHideValuesInTheFile();
}
