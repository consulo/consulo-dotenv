package ru.adelf.idea.dotenv.grammars;

import consulo.language.lexer.FlexAdapter;

public class DotEnvLexerAdapter extends FlexAdapter {
    public DotEnvLexerAdapter() {
        super(new _DotEnvLexer(null));
    }
}