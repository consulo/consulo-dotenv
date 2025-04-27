package ru.adelf.idea.dotenv.api;

import consulo.component.extension.ExtensionPointName;

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
