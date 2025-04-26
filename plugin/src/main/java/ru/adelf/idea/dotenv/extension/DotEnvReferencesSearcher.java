package ru.adelf.idea.dotenv.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.content.scope.SearchScope;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.search.ReferencesSearch;
import consulo.language.psi.search.ReferencesSearchQueryExecutor;
import consulo.language.psi.search.SearchRequestCollector;
import consulo.language.psi.search.UsageSearchContext;
import consulo.project.util.query.QueryExecutorBase;
import consulo.util.lang.StringUtil;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.psi.DotEnvProperty;

import java.util.function.Predicate;

public class DotEnvReferencesSearcher  extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> implements ReferencesSearchQueryExecutor {
    public DotEnvReferencesSearcher() {
        super(true);
    }

    @Override
    public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters, @NotNull Predicate<? super PsiReference> consumer) {
        PsiElement refElement = queryParameters.getElementToSearch();
        if (!(refElement instanceof DotEnvProperty)) return;

        addPropertyUsages((DotEnvProperty)refElement, queryParameters.getEffectiveSearchScope(), queryParameters.getOptimizer());
    }

    private static void addPropertyUsages(@NotNull DotEnvProperty property, @NotNull SearchScope scope, @NotNull SearchRequestCollector collector) {
        final String propertyName = property.getKeyText();
        if (StringUtil.isNotEmpty(propertyName)) {
            /*SearchScope additional = GlobalSearchScope.EMPTY_SCOPE;
            for (CustomPropertyScopeProvider provider : CustomPropertyScopeProvider.EP_NAME.getExtensionList()) {
                additional = additional.union(provider.getScope(property.getProject()));
            }

            SearchScope propScope = scope.intersectWith(property.getUseScope()).intersectWith(additional);*/
            collector.searchWord(propertyName, scope, UsageSearchContext.ANY, true, property);
            collector.searchWord("process.env." + propertyName, scope, UsageSearchContext.ANY, true, property);
        }
    }
}
