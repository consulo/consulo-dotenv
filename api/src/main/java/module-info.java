/**
 * @author VISTALL
 * @since 2025-04-27
 */
module ru.adelf.idea.dotenv.api {
    requires transitive consulo.ide.api;

    exports ru.adelf.idea.dotenv.api;
    exports ru.adelf.idea.dotenv.api.index;
    exports ru.adelf.idea.dotenv.api.model;
}