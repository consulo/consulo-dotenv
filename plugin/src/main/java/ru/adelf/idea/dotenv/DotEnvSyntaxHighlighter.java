package ru.adelf.idea.dotenv;

import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.codeEditor.HighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenType;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.grammars.DotEnvLexerAdapter;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;

import static consulo.colorScheme.TextAttributesKey.createTextAttributesKey;

class DotEnvSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final TextAttributesKey SEPARATOR =
            createTextAttributesKey("DOTENV_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    private static final TextAttributesKey KEY =
            createTextAttributesKey("DOTENV_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    private static final TextAttributesKey VALUE =
            createTextAttributesKey("DOTENV_VALUE", DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey COMMENT =
            createTextAttributesKey("DOTENV_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    private static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("DOTENV_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new DotEnvLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(DotEnvTypes.SEPARATOR)) {
            return SEPARATOR_KEYS;
        } else if (tokenType.equals(DotEnvTypes.KEY_CHARS)) {
            return KEY_KEYS;
        } else if (tokenType.equals(DotEnvTypes.VALUE_CHARS)) {
            return VALUE_KEYS;
        } else if (tokenType.equals(DotEnvTypes.COMMENT) || tokenType.equals(DotEnvTypes.EXPORT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}