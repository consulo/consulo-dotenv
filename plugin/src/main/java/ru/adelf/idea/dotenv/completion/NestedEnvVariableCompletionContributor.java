package ru.adelf.idea.dotenv.completion;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.CommonProcessors;
import consulo.language.Language;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.pattern.PlatformPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.util.ProcessingContext;
import jakarta.annotation.Nonnull;
import ru.adelf.idea.dotenv.DotEnvLanguage;
import ru.adelf.idea.dotenv.indexing.DotEnvKeyValuesIndex;
import ru.adelf.idea.dotenv.psi.DotEnvNestedVariableKey;
import ru.adelf.idea.dotenv.psi.DotEnvTypes;
import ru.adelf.idea.dotenv.psi.DotEnvValue;

@ExtensionImpl
public class NestedEnvVariableCompletionContributor extends CompletionContributor {

    public NestedEnvVariableCompletionContributor() {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement(DotEnvTypes.KEY_CHARS)
                .withLanguage(DotEnvLanguage.INSTANCE)
                .withParent(DotEnvNestedVariableKey.class),
            new NestedEnvVariableCompletionProvider()
        );
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement(DotEnvTypes.VALUE_CHARS)
                .withLanguage(DotEnvLanguage.INSTANCE)
                .withParent(DotEnvValue.class),
            new NestedVariableBlockCompletionProvider()
        );
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }

    private static class NestedEnvVariableCompletionProvider implements CompletionProvider {
        @Override
        public void addCompletions(CompletionParameters parameters,
                                      ProcessingContext context,
                                      CompletionResultSet result) {
            PsiElement position = parameters.getPosition();
            FileBasedIndex index = FileBasedIndex.getInstance();
            CommonProcessors.CollectUniquesProcessor<String> processor = new CommonProcessors.CollectUniquesProcessor<>();
            index.processAllKeys(DotEnvKeyValuesIndex.KEY, processor, position.getProject());
            for (String key : processor.getResults()) {
                if (key != null) {
                    result.addElement(LookupElementBuilder.create(key));
                }
            }
        }
    }

    private static class NestedVariableBlockCompletionProvider implements CompletionProvider {
        @Override
        public void addCompletions(CompletionParameters parameters,
                                      ProcessingContext context,
                                      CompletionResultSet result) {
            if (shouldComplete(parameters)) {
                LookupElementBuilder element = LookupElementBuilder
                    .create(INSERTED_TEXT)
                    .withPresentableText(PRESENTABLE_TEXT)
                    .withInsertHandler(new InsertHandler<LookupElement>() {
                        @Override
                        public void handleInsert(InsertionContext insertionContext, LookupElement item) {
                            int offset = insertionContext.getStartOffset() + 2;
                            insertionContext.getEditor().getCaretModel().moveToOffset(offset);
                        }
                    });
                result.withPrefixMatcher(MATCHED_PREFIX).addElement(element);
            }
        }

        private boolean shouldComplete(CompletionParameters parameters) {
            PsiElement original = parameters.getOriginalPosition();
            if (original != null
                && original.getPrevSibling() instanceof PsiElement prevSib
                && DOUBLE_QUOTE_CHARACTER.equals(prevSib.getText())) {
                int offset = parameters.getOffset();
                CharSequence seq = parameters.getEditor().getDocument().getCharsSequence();
                return offset < 2 || seq.charAt(offset - 2) != ESCAPE_CHARACTER;
            }
            return false;
        }

        private static final String INSERTED_TEXT = "${}";
        private static final String PRESENTABLE_TEXT = "${...}";
        private static final String MATCHED_PREFIX = "$";
        private static final char ESCAPE_CHARACTER = '\\';
        private static final String DOUBLE_QUOTE_CHARACTER = "\"";
    }
}
