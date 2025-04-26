package ru.adelf.idea.dotenv;

import consulo.annotation.internal.MigratedExtensionsTo;
import consulo.dotenv.localize.DotEnvLocalize;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

@MigratedExtensionsTo(DotEnvLocalize.class)
public final class DotEnvBundle {
    public static final String BUNDLE = "messages.DotEnvBundle";

    private DotEnvBundle() {
    }

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        throw new UnsupportedOperationException(key);
    }

    public static @NotNull Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        throw new UnsupportedOperationException(key);
    }
}