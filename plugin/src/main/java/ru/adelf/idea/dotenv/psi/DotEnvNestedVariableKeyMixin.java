package ru.adelf.idea.dotenv.psi;

import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ReferenceProvidersRegistry;

/**
 * @author VISTALL
 * @since 2026-03-17
 */
public abstract class DotEnvNestedVariableKeyMixin extends ASTWrapperPsiElement {
    public DotEnvNestedVariableKeyMixin(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        return references.length == 0 ? null : references[0];
    }

    @Override
    public PsiReference[] getReferences() {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }
}
