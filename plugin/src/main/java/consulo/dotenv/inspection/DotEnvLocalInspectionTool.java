package consulo.dotenv.inspection;

import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import ru.adelf.idea.dotenv.DotEnvLanguage;

/**
 * @author VISTALL
 * @since 2025-04-26
 */
public abstract class DotEnvLocalInspectionTool extends LocalInspectionTool {
    @Nullable
    @Override
    public Language getLanguage() {
        return DotEnvLanguage.INSTANCE;
    }

    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return LocalizeValue.empty();
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }
}
