package ru.adelf.idea.dotenv.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.refactoring.RefactoringSupportProvider;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.DotEnvLanguage;
import ru.adelf.idea.dotenv.psi.DotEnvProperty;

@ExtensionImpl
public class DotEnvRefactoringSupportProvider extends RefactoringSupportProvider {
    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
        return element instanceof DotEnvProperty;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }
}