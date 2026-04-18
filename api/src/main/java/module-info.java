/**
 * @author VISTALL
 * @since 2025-04-27
 */
module ru.adelf.idea.dotenv.api {
    requires consulo.application.api;
    requires consulo.component.api;
    requires consulo.index.io;
    requires transitive consulo.language.api;
    requires transitive consulo.language.editor.api;
    requires consulo.project.api;
    requires consulo.util.lang;
    requires consulo.virtual.file.system.api;

    exports ru.adelf.idea.dotenv.api;
    exports ru.adelf.idea.dotenv.api.index;
    exports ru.adelf.idea.dotenv.api.model;
}
