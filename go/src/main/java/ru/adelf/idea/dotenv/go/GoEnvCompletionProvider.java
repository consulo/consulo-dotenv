package ru.adelf.idea.dotenv.go;

import com.goide.GoLanguage;
import com.goide.psi.GoCallExpr;
import com.goide.psi.GoStringLiteral;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionType;
import consulo.language.pattern.PlatformPatterns;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.api.BaseEnvCompletionProvider;
import ru.adelf.idea.dotenv.api.DotEnvSettings;

@ExtensionImpl
public class GoEnvCompletionProvider extends BaseEnvCompletionProvider {
    public GoEnvCompletionProvider() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), (completionParameters, processingContext, completionResultSet) -> {

            PsiElement psiElement = completionParameters.getOriginalPosition();

            if (psiElement == null || !DotEnvSettings.getInstance().isCompletionEnabled()) {
                return;
            }

            if (getStringLiteral(psiElement) == null) {
                return;
            }

            fillCompletionResultSet(completionResultSet, psiElement.getProject());
        });
    }

    public static @Nullable GoStringLiteral getStringLiteral(@NotNull PsiElement psiElement) {
        PsiElement parent = psiElement.getParent();

        if (!(parent instanceof GoStringLiteral)) {
            return null;
        }

        if (parent.getParent() == null) {
            return null;
        }

        PsiElement candidate = parent.getParent().getParent();

        if (candidate instanceof GoCallExpr) {
            return GoPsiHelper.getEnvironmentGoLiteral((GoCallExpr) candidate);
        }

        return null;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return GoLanguage.INSTANCE;
    }
}
