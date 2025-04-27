package ru.adelf.idea.dotenv.extension;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.document.Document;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.editor.folding.FoldingBuilderEx;
import consulo.language.editor.folding.FoldingDescriptor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvLanguage;
import ru.adelf.idea.dotenv.DotEnvSettings;
import ru.adelf.idea.dotenv.psi.DotEnvValue;

@ExtensionImpl
public class DotEnvValuesHiding extends FoldingBuilderEx implements DumbAware {
    @RequiredReadAction
    @Nonnull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        if (!DotEnvSettings.getInstance().isHideValuesInTheFile()) return emptyResult;

        return PsiTreeUtil.collectElementsOfType(root, DotEnvValue.class).stream().map(
                dotEnvValue -> new FoldingDescriptor(
                        dotEnvValue.getNode(),
                        dotEnvValue.getTextRange(),
                        null,
                        "Show value"
                )
        ).toArray(FoldingDescriptor[]::new);
    }

    @RequiredReadAction
    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        return null;
    }

    @RequiredReadAction
    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }

    private static final FoldingDescriptor[] emptyResult = FoldingDescriptor.EMPTY;

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }
}
