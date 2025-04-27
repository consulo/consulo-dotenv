package ru.adelf.idea.dotenv.java;

import com.intellij.java.language.JavaLanguage;
import com.intellij.java.language.psi.PsiLiteralExpression;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionParameters;
import consulo.language.editor.completion.CompletionProvider;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.CompletionType;
import consulo.language.pattern.PlatformPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.util.ProcessingContext;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.api.BaseEnvCompletionProvider;
import ru.adelf.idea.dotenv.api.DotEnvSettings;

@ExtensionImpl
public class JavaEnvCompletionContributor extends BaseEnvCompletionProvider {
    public JavaEnvCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiLiteralExpression.class), new CompletionProvider() {
            @Override
            public void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                PsiElement psiElement = completionParameters.getOriginalPosition();

                if (psiElement == null || !DotEnvSettings.getInstance().isCompletionEnabled()) {
                    return;
                }

                if (getStringLiteral(psiElement) == null) {
                    return;
                }

                fillCompletionResultSet(completionResultSet, psiElement.getProject());
            }
        });
    }

    @Nullable
    public static PsiLiteralExpression getStringLiteral(@NotNull PsiElement psiElement) {
        PsiElement parent = psiElement.getParent();

        if (!(parent instanceof PsiLiteralExpression)) {
            return null;
        }

        if (!JavaPsiHelper.isEnvStringLiteral((PsiLiteralExpression) parent)) {
            return null;
        }

        return (PsiLiteralExpression) parent;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return JavaLanguage.INSTANCE;
    }
}
