/**
 * @author VISTALL
 * @since 2025-04-27
 */
module ru.adelf.idea.dotenv.java {
    requires ru.adelf.idea.dotenv.api;

    requires consulo.code.editor.api;
    requires consulo.datacontext.api;
    requires consulo.language.api;
    requires consulo.language.editor.api;
    requires consulo.util.lang;
    requires consulo.virtual.file.system.api;

    requires consulo.java.language.api;
    requires consulo.java.language.impl;
}
