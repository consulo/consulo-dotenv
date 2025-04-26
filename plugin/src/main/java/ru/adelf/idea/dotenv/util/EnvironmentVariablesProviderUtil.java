package ru.adelf.idea.dotenv.util;

import consulo.component.extension.ExtensionPointName;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesProvider;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesUsagesProvider;

public final class EnvironmentVariablesProviderUtil {
    private static final ExtensionPointName<EnvironmentVariablesProvider> providersEP
            = new ExtensionPointName<>(EnvironmentVariablesProvider.class);

    private static final ExtensionPointName<EnvironmentVariablesUsagesProvider> usageProvidersEP
            = new ExtensionPointName<>(EnvironmentVariablesUsagesProvider.class);

    public static EnvironmentVariablesProvider[] getEnvVariablesProviders() {
        return providersEP.getExtensions();
    }

    public static EnvironmentVariablesUsagesProvider[] getEnvVariablesUsagesProviders() {
        return usageProvidersEP.getExtensions();
    }
}
