package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.editor.inspection.*;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import ru.adelf.idea.dotenv.DotEnvBundle;
import ru.adelf.idea.dotenv.psi.DotEnvValue;

@ExtensionImpl
public class NestedVariableOutsideDoubleQuotesInspection extends DotEnvLocalInspectionTool {

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionNameNestedVariableOutsideDoubleQuotes();
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof DotEnvValue) {
                    processValue((DotEnvValue) element, holder);
                }
                super.visitElement(element);
            }
        };
    }

    private void processValue(DotEnvValue value, ProblemsHolder holder) {
        TextRange range = findUnquotedRange(value);
        if (range != null) {
            holder.registerProblem(
                value,
                DotEnvLocalize.inspectionNameNestedVariableOutsideDoubleQuotes().get(),
                ProblemHighlightType.ERROR,
                new TextRange(range.getStartOffset(), range.getEndOffset()),
                new QuoteEnvironmentVariableValueQuickFix()
            );
        }
    }

    private static class QuoteEnvironmentVariableValueQuickFix implements LocalQuickFix {

        @Nonnull
        @Override
        public LocalizeValue getName() {
            return DotEnvLocalize.quickfixNamePutEnvironmentVariableValueInsideQuotes();
        }

        @Override
        public void applyFix(@Nonnull Project project, ProblemDescriptor descriptor) {
            PsiElement element = descriptor.getPsiElement();
            if (element instanceof DotEnvValue) {
                DotEnvValue value = (DotEnvValue) element;
                TextRange range = findUnquotedRange(value);
                if (range != null) {
                    PsiFile file = value.getContainingFile();
                    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
                    if (document != null) {
                        document.replaceString(
                            value.getTextRange().getStartOffset() + range.getStartOffset(),
                            value.getTextRange().getStartOffset() + range.getEndOffset(),
                            "\"" + StringUtil.trim(value.getText()) + "\""
                        );
                    }
                }
            }
        }
    }

    private static TextRange findUnquotedRange(DotEnvValue value) {
        String text = value.getText();
        if (!text.contains("${")) {
            return null;
        }
        int left = StringUtil.skipWhitespaceForward(text, 0);
        int right = StringUtil.skipWhitespaceBackward(text, text.length());
        if (text.charAt(left) == '"' && text.charAt(right - 1) == '"') {
            return null;
        }
        return new TextRange(left, right);
    }
}
