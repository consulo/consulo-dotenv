package ru.adelf.idea.dotenv.go;

import com.goide.psi.GoCallExpr;
import com.goide.psi.GoStringLiteral;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiRecursiveElementWalkingVisitor;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.api.model.KeyUsagePsiElement;

import java.util.Collection;
import java.util.HashSet;

class GoEnvironmentCallsVisitor extends PsiRecursiveElementWalkingVisitor {
    private final Collection<KeyUsagePsiElement> collectedItems = new HashSet<>();

    @Override
    public void visitElement(PsiElement element) {
        if(element instanceof GoCallExpr) {
            this.visitCall((GoCallExpr) element);
        }

        super.visitElement(element);
    }

    private void visitCall(GoCallExpr expression) {
        GoStringLiteral stringLiteral = GoPsiHelper.getEnvironmentGoLiteral(expression);
        if(stringLiteral != null) {
            collectedItems.add(new KeyUsagePsiElement(stringLiteral.getDecodedText(), stringLiteral));
        }
    }

    @NotNull
    Collection<KeyUsagePsiElement> getCollectedItems() {
        return collectedItems;
    }
}
