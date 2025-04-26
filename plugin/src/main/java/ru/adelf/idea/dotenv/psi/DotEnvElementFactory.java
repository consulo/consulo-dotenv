package ru.adelf.idea.dotenv.psi;

import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import ru.adelf.idea.dotenv.DotEnvFileType;

final class DotEnvElementFactory {

    static DotEnvProperty createProperty(Project project, String text) {
        final DotEnvFile file = createFile(project, text);
        return (DotEnvProperty) file.getFirstChild();
    }

    static DotEnvNestedVariableKey createNestedVariableKey(Project project, String name) {
        DotEnvProperty property = createProperty(project, String.format("DUMMY=\"${%s}\"", name));
        return PsiTreeUtil.getChildOfType(property.getValue(), DotEnvNestedVariableKey.class);
    }

    private static DotEnvFile createFile(Project project, String text) {
        String name = "dummy.env";
        return (DotEnvFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, DotEnvFileType.INSTANCE, text);
    }
}
