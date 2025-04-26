package ru.adelf.idea.dotenv.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.Language;
import consulo.language.findUsage.FindUsagesProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNamedElement;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvLanguage;
import ru.adelf.idea.dotenv.psi.DotEnvProperty;

@ExtensionImpl
public class DotEnvFindUsagesProvider implements FindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Override
    public @Nullable String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    @NotNull
    public String getType(@NotNull PsiElement element) {
        if (element instanceof DotEnvProperty) {
            return DotEnvLocalize.environmentVariable().get();
        } else {
            return "";
        }
    }

    @Override
    public @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof DotEnvProperty property) {
            return property.getKeyText();
        } else {
            return "";
        }
    }

    @Override
    public @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof DotEnvProperty property) {
            return property.getKeyText() + ":" + property.getValueText();
        } else {
            return "";
        }
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }
}