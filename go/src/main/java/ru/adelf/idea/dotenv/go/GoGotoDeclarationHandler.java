package ru.adelf.idea.dotenv.go;

import com.goide.psi.GoStringLiteral;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.dataContext.DataContext;
import consulo.language.editor.navigation.GotoDeclarationHandler;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nullable;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesApi;

/**
 * @author VISTALL
 * @since 2025-04-27
 */
@ExtensionImpl
public class GoGotoDeclarationHandler implements GotoDeclarationHandler {
    @Override
    public @Nullable PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int i, Editor editor) {
        if (psiElement == null) {
            return PsiElement.EMPTY_ARRAY;
        }

        GoStringLiteral stringLiteral = GoEnvCompletionProvider.getStringLiteral(psiElement);

        if (stringLiteral == null) {
            return PsiElement.EMPTY_ARRAY;
        }

        return EnvironmentVariablesApi.getKeyDeclarations(psiElement.getProject(), stringLiteral.getDecodedText());
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }
}
