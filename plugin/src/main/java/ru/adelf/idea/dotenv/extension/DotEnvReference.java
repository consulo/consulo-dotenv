package ru.adelf.idea.dotenv.extension;

import consulo.document.util.TextRange;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.psi.*;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesApi;

import java.util.Arrays;

public class DotEnvReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private final String key;

    public DotEnvReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
        key = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
    }

    public DotEnvReference(@NotNull PsiElement element, TextRange textRange, String key) {
        super(element, textRange);
        this.key = key;
    }

    @Override
    public @NotNull ResolveResult[] multiResolve(boolean incompleteCode) {
        final PsiElement[] elements = EnvironmentVariablesApi.getKeyDeclarations(myElement.getProject(), key);

        return Arrays.stream(elements)
                .filter(psiElement -> psiElement instanceof PsiNamedElement)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public @NotNull Object[] getVariants() {
        Project project = myElement.getProject();
        final PsiElement[] elements = EnvironmentVariablesApi.getKeyDeclarations(project, key);

        return Arrays.stream(elements)
                .filter(psiElement -> psiElement instanceof PsiNamedElement)
                .map(psiElement -> LookupElementBuilder.create(psiElement).
                        withTypeText(psiElement.getContainingFile().getName()))
                .toArray(LookupElement[]::new);
    }
}