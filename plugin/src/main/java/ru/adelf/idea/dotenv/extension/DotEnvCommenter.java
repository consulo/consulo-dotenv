package ru.adelf.idea.dotenv.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.language.Commenter;
import consulo.language.CommenterDataHolder;
import consulo.language.Language;
import consulo.language.SelfManagingCommenter;
import consulo.language.psi.PsiFile;
import consulo.util.lang.CharArrayUtil;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvLanguage;

@ExtensionImpl
public class DotEnvCommenter implements Commenter, SelfManagingCommenter<CommenterDataHolder> {
    private static final String HASH_COMMENT_PREFIX = "#";

    @Override
    public String getLineCommentPrefix() {
        return HASH_COMMENT_PREFIX;
    }

    @Override
    public String getBlockCommentPrefix() {
        return null;
    }

    @Override
    public String getBlockCommentSuffix() {
        return null;
    }

    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }

    @Override
    public @Nullable CommenterDataHolder createLineCommentingState(int startLine, int endLine, @NotNull Document document, @NotNull PsiFile file) {
        return null;
    }

    @Override
    public @Nullable CommenterDataHolder createBlockCommentingState(int selectionStart,
                                                                    int selectionEnd,
                                                                    @NotNull Document document,
                                                                    @NotNull PsiFile file) {
        return null;
    }

    @Override
    public void commentLine(int line, int offset, @NotNull Document document, @NotNull CommenterDataHolder data) {
        document.insertString(offset, HASH_COMMENT_PREFIX);
    }

    @Override
    public void uncommentLine(int line, int offset, @NotNull Document document, @NotNull CommenterDataHolder data) {
        document.deleteString(offset, offset + 1);
    }

    @Override
    public boolean isLineCommented(int line, int offset, @NotNull Document document, @NotNull CommenterDataHolder data) {
        return CharArrayUtil.regionMatches(document.getCharsSequence(), offset, HASH_COMMENT_PREFIX);
    }

    @Override
    public @Nullable String getCommentPrefix(int line, @NotNull Document document, @NotNull CommenterDataHolder data) {
        return HASH_COMMENT_PREFIX;
    }

    @Override
    public @Nullable TextRange getBlockCommentRange(int selectionStart,
                                                    int selectionEnd,
                                                    @NotNull Document document,
                                                    @NotNull CommenterDataHolder data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getBlockCommentPrefix(int selectionStart, @NotNull Document document, @NotNull CommenterDataHolder data) {
        return getBlockCommentPrefix();
    }

    @Override
    public @Nullable String getBlockCommentSuffix(int selectionEnd, @NotNull Document document, @NotNull CommenterDataHolder data) {
        return getBlockCommentSuffix();
    }

    @Override
    public void uncommentBlockComment(int startOffset, int endOffset, Document document, CommenterDataHolder data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull TextRange insertBlockComment(int startOffset, int endOffset, Document document, CommenterDataHolder data) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }
}