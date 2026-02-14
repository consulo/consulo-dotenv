package ru.adelf.idea.dotenv;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.grammars.DotEnvLexerAdapter;
import ru.adelf.idea.dotenv.parser.DotEnvParser;
import ru.adelf.idea.dotenv.psi.DotEnvFile;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;
import ru.adelf.idea.dotenv.psi.impl.DotEnvTypesFactory;

@ExtensionImpl
public class DotEnvParserDefinition implements ParserDefinition {
    private static final TokenSet WHITE_SPACES = TokenSet.WHITE_SPACE;
    private static final IFileElementType FILE = new IFileElementType(DotEnvLanguage.INSTANCE);

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }

    @Override
    public @NotNull Lexer createLexer(LanguageVersion languageVersion) {
        return new DotEnvLexerAdapter();
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
        return WHITE_SPACES;
    }

    @Override
    public @NotNull TokenSet getCommentTokens(LanguageVersion languageVersion) {
        return TokenSet.create(DotEnvTypes.COMMENT);
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiParser createParser(LanguageVersion languageVersion) {
        return new DotEnvParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new DotEnvFile(viewProvider);
    }

    @Override
    public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return DotEnvTypesFactory.createElement(node);
    }
}
