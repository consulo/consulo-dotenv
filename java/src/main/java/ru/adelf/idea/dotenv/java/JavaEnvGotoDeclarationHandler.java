package ru.adelf.idea.dotenv.java;

import com.intellij.java.language.psi.PsiLiteralExpression;
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
public class JavaEnvGotoDeclarationHandler implements GotoDeclarationHandler {
    @Override
    @Nullable
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int i, Editor editor) {
        if (psiElement == null) {
            return PsiElement.EMPTY_ARRAY;
        }

        PsiLiteralExpression stringLiteral = JavaEnvCompletionContributor.getStringLiteral(psiElement);

        if (stringLiteral == null) {
            return PsiElement.EMPTY_ARRAY;
        }

        Object value = stringLiteral.getValue();

        if (!(value instanceof String)) {
            return PsiElement.EMPTY_ARRAY;
        }

        return EnvironmentVariablesApi.getKeyDeclarations(psiElement.getProject(), (String) value);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }
}
