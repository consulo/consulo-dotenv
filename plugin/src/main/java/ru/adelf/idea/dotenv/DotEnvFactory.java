package ru.adelf.idea.dotenv;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.PsiRecursiveElementWalkingVisitor;
import consulo.project.Project;
import consulo.util.lang.ref.Ref;
import org.jetbrains.annotations.NotNull;

public final class DotEnvFactory {
    public static PsiElement createFromText(@NotNull Project project, @NotNull IElementType type, @NotNull String text) {
        final Ref<PsiElement> ret = new Ref<>();
        PsiFile dummyFile = createDummyFile(project, text);
        dummyFile.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                ASTNode node = element.getNode();
                if (node != null && node.getElementType() == type) {
                    ret.set(element);
                    stopWalking();
                } else {
                    super.visitElement(element);
                }
            }
        });

        assert !ret.isNull() : "cannot create element from text:\n" + dummyFile.getText();

        return ret.get();
    }

    private static @NotNull PsiFile createDummyFile(Project project, String fileText) {
        return PsiFileFactory.getInstance(project).createFileFromText("DUMMY__.env", DotEnvFileType.INSTANCE, fileText, System.currentTimeMillis(), false);
    }
}
