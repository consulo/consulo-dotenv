package ru.adelf.idea.dotenv.models;

import consulo.language.psi.PsiElement;

/**
 * Environment key usage PsiElement representation with key
 */
public class KeyUsagePsiElement {

    private final String key;
    private final PsiElement element;

    public KeyUsagePsiElement(String key, PsiElement element) {
        this.key = key;
        this.element = element;
    }

    public String getKey() {
        return key;
    }

    public PsiElement getElement() {
        return element;
    }
}
