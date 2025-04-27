package ru.adelf.idea.dotenv.java;

import com.intellij.java.language.JavaLanguage;
import com.intellij.java.language.psi.PsiLiteralExpression;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionConfidence;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.util.lang.ThreeState;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl(id = "envStringCompletionConfidence")
public class JavaCompletionConfidence extends CompletionConfidence {
    @Override
    public @NotNull ThreeState shouldSkipAutopopup(@NotNull PsiElement contextElement, @NotNull PsiFile psiFile, int offset) {
        PsiElement literal = contextElement.getContext();
        if(!(literal instanceof PsiLiteralExpression)) {
            return ThreeState.UNSURE;
        }

        if(JavaPsiHelper.isEnvStringLiteral((PsiLiteralExpression) literal)) {
            return ThreeState.NO;
        }

        return ThreeState.UNSURE;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return JavaLanguage.INSTANCE;
    }
}