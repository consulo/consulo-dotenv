package ru.adelf.idea.dotenv;

import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.util.xml.serializer.XmlSerializerUtil;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ServiceImpl
@Singleton
@State(name = "DotEnvSettings", storages = {@Storage("dot-env.xml")})
public class DotEnvSettings implements PersistentStateComponent<DotEnvSettings>, ru.adelf.idea.dotenv.api.DotEnvSettings {
    public boolean completionEnabled = true;
    public boolean storeValues = true;

    public boolean hideValuesInTheFile = false;

    @Override
    public @Nullable DotEnvSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DotEnvSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static ru.adelf.idea.dotenv.api.DotEnvSettings getInstance() {
        return ApplicationManager.getApplication().getService(ru.adelf.idea.dotenv.api.DotEnvSettings.class);
    }

    @Override
    public boolean isCompletionEnabled() {
        return completionEnabled;
    }

    @Override
    public boolean isStoreValues() {
        return storeValues;
    }

    @Override
    public boolean isHideValuesInTheFile() {
        return hideValuesInTheFile;
    }
}
