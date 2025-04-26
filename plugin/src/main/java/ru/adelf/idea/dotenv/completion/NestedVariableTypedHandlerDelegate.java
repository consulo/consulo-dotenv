package ru.adelf.idea.dotenv.completion;

import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.AutoPopupController;
import consulo.language.editor.action.TypedHandlerDelegate;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import ru.adelf.idea.dotenv.psi.DotEnvFile;

@ExtensionImpl
public class NestedVariableTypedHandlerDelegate extends TypedHandlerDelegate {

    @Override
    public Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
        if (!(file instanceof DotEnvFile)) {
            return super.checkAutoPopup(charTyped, project, editor, file);
        }
        if (charTyped == '$') {
            AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, null);
            return Result.CONTINUE;
        }
        else {
            return super.checkAutoPopup(charTyped, project, editor, file);
        }
    }

}