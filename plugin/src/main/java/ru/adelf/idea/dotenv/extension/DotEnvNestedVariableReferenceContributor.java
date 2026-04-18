package ru.adelf.idea.dotenv.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.psi.*;
import consulo.language.pattern.PlatformPatterns;
import consulo.language.util.ProcessingContext;
import jakarta.annotation.Nonnull;
import ru.adelf.idea.dotenv.DotEnvLanguage;
import ru.adelf.idea.dotenv.psi.DotEnvNestedVariableKey;
import consulo.document.util.TextRange;

@ExtensionImpl
public class DotEnvNestedVariableReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@Nonnull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(DotEnvNestedVariableKey.class),
            new PsiReferenceProvider() {
                @Nonnull
                @Override
                public PsiReference[] getReferencesByElement(@Nonnull PsiElement element, @Nonnull ProcessingContext context) {
                    String key = element.getText();
                    if (key.isEmpty()) {
                        return PsiReference.EMPTY_ARRAY;
                    }
                    return new PsiReference[]{
                        new DotEnvReference(element, new TextRange(0, key.length()), key)
                    };
                }
            }
        );
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }
}
