/**
 * @author VISTALL
 * @since 2025-04-27
 */
module ru.adelf.idea.dotenv {
    requires ru.adelf.idea.dotenv.api;

    requires consulo.application.api;
    requires consulo.application.content.api;
    requires consulo.code.editor.api;
    requires consulo.color.scheme.api;
    requires consulo.component.api;
    requires consulo.configurable.api;
    requires consulo.disposer.api;
    requires consulo.document.api;
    requires consulo.index.io;
    requires consulo.language.api;
    requires consulo.language.editor.api;
    requires consulo.language.editor.refactoring.api;
    requires consulo.language.impl;
    requires consulo.localize.api;
    requires consulo.logging.api;
    requires consulo.project.api;
    requires consulo.ui.api;
    requires consulo.util.lang;
    requires consulo.util.xml.serializer;
    requires consulo.virtual.file.system.api;
}
