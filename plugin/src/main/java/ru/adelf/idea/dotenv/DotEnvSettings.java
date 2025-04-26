package ru.adelf.idea.dotenv;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.util.xml.serializer.XmlSerializerUtil;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
@Singleton
@State(name = "DotEnvSettings", storages = {@Storage("dot-env.xml")})
public class DotEnvSettings implements PersistentStateComponent<DotEnvSettings> {
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

    public static DotEnvSettings getInstance() {
        return ApplicationManager.getApplication().getService(DotEnvSettings.class);
    }
}
