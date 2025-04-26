package ru.adelf.idea.dotenv.common;

import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.completion.lookup.PrioritizedLookupElement;
import consulo.language.editor.navigation.GotoDeclarationHandler;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesApi;

import java.util.Map;

public abstract class BaseEnvCompletionProvider extends CompletionContributor implements GotoDeclarationHandler {

    protected void fillCompletionResultSet(@NotNull CompletionResultSet completionResultSet, @NotNull Project project) {
        for (Map.Entry<String, String> entry : EnvironmentVariablesApi.getAllKeyValues(project).entrySet()) {
            LookupElementBuilder lockup = LookupElementBuilder.create(entry.getKey())
                    .withCaseSensitivity(false);

            if (!entry.getValue().isEmpty()) {
                lockup = lockup.withTailText(" = " + entry.getValue(), true);
            }

            completionResultSet.addElement(PrioritizedLookupElement.withPriority(lockup, 100));
        }
    }
}
