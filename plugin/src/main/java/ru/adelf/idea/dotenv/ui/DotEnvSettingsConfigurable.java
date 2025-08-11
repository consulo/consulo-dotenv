package ru.adelf.idea.dotenv.ui;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.SimpleConfigurableByProperties;
import consulo.configurable.StandardConfigurableIds;
import consulo.disposer.Disposable;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.localize.LocalizeValue;
import consulo.ui.CheckBox;
import consulo.ui.Component;
import consulo.ui.HtmlLabel;
import consulo.ui.Label;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvSettings;

@ExtensionImpl
public class DotEnvSettingsConfigurable extends SimpleConfigurableByProperties implements ApplicationConfigurable {
    @Nonnull
    @Override
    public String getId() {
        return "dot.env.configurable";
    }

    @Nullable
    @Override
    public String getParentId() {
        return StandardConfigurableIds.EDITOR_GROUP;
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return DotEnvLocalize.dotenvConfigurableName();
    }

    @RequiredUIAccess
    @Nonnull
    @Override
    protected Component createLayout(@Nonnull PropertyBuilder propertyBuilder, @Nonnull Disposable disposable) {
        DotEnvSettings settings = getSettings();

        VerticalLayout layout = VerticalLayout.create();

        CheckBox completionEnabledCheckbox = CheckBox.create(DotEnvLocalize.enableEnvironmentVariablesCompletions());
        layout.add(completionEnabledCheckbox);
        propertyBuilder.add(completionEnabledCheckbox, () -> settings.completionEnabled, v -> settings.completionEnabled = v);

        CheckBox storeValuesCheckbox = CheckBox.create(DotEnvLocalize.storeAndCompleteValues());
        storeValuesCheckbox.setToolTipText(DotEnvLocalize.storingValuesInTheIndicesCanBeTurnedOffDueToSecurityReasons());
        layout.add(storeValuesCheckbox);
        propertyBuilder.add(storeValuesCheckbox, () -> settings.storeValues, v -> settings.storeValues = v);

        Label infoStore = Label.create(DotEnvLocalize.labelRunFileInvalidateCachesToUpdateIndices());
        infoStore.setVisible(false);
        layout.add(infoStore);

        storeValuesCheckbox.addValueListener(e -> infoStore.setVisible(e.getValue()));

        CheckBox hideValuesCheckbox = CheckBox.create(DotEnvLocalize.hideValuesInEnvFiles());
        layout.add(hideValuesCheckbox);
        propertyBuilder.add(hideValuesCheckbox, () -> settings.hideValuesInTheFile, v -> settings.hideValuesInTheFile = v);

        HtmlLabel hideInfo =
            HtmlLabel.create(DotEnvLocalize.labelCheckThisIfYouWantValuesToBeHiddenByDefaultBrMainMenuCodeFoldingActionsCanBeUsedToControlIt());
        layout.add(hideInfo);
        hideInfo.setVisible(false);

        hideValuesCheckbox.addValueListener(e -> hideInfo.setVisible(e.getValue()));

        return layout;
    }

    private static DotEnvSettings getSettings() {
        return (DotEnvSettings) DotEnvSettings.getInstance();
    }
}
