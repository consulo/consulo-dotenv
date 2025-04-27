package ru.adelf.idea.dotenv.ui;

import consulo.configurable.Configurable;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.ui.ex.awt.JBLabel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvSettings;

import javax.swing.*;
import javax.swing.border.Border;

public class DotEnvSettingsConfigurable implements Configurable {

    private JCheckBox completionEnabledCheckbox;
    private JCheckBox storeValuesCheckbox;
    private JCheckBox hideValuesCheckbox;

    @Override
    public @Nls String getDisplayName() {
        return "DotEnv";
    }

    @Override
    public @Nullable JComponent createComponent() {
        DotEnvSettings settings = getSettings();
        Border standardBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        completionEnabledCheckbox = new JCheckBox(DotEnvLocalize.enableEnvironmentVariablesCompletions().get(), settings.completionEnabled);
        completionEnabledCheckbox.setBorder(standardBorder);

        storeValuesCheckbox = new JCheckBox(DotEnvLocalize.storeAndCompleteValues().get(), settings.storeValues);
        storeValuesCheckbox.setBorder(standardBorder);
        storeValuesCheckbox.setToolTipText(DotEnvLocalize.storingValuesInTheIndicesCanBeTurnedOffDueToSecurityReasons().get());

        JLabel storeValuesInvalidateCachesLabel = new JBLabel(DotEnvLocalize.labelRunFileInvalidateCachesToUpdateIndices().get());
        storeValuesInvalidateCachesLabel.setBorder(standardBorder);
        storeValuesInvalidateCachesLabel.setVisible(false);

        storeValuesCheckbox.addChangeListener(e -> storeValuesInvalidateCachesLabel.setVisible(storeValuesCheckbox.isSelected() != getSettings().storeValues));

        hideValuesCheckbox = new JCheckBox(DotEnvLocalize.hideValuesInEnvFiles().get(), settings.storeValues);
        hideValuesCheckbox.setBorder(standardBorder);

        JLabel hideValuesLabel = new JBLabel(
            DotEnvLocalize.labelCheckThisIfYouWantValuesToBeHiddenByDefaultBrMainMenuCodeFoldingActionsCanBeUsedToControlIt().get());
        hideValuesLabel.setBorder(standardBorder);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(completionEnabledCheckbox);
        rootPanel.add(storeValuesCheckbox);
        rootPanel.add(storeValuesInvalidateCachesLabel);
        rootPanel.add(hideValuesCheckbox);
        rootPanel.add(hideValuesLabel);

        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return !completionEnabledCheckbox.isSelected() == getSettings().completionEnabled
            || !storeValuesCheckbox.isSelected() == getSettings().storeValues
            || !hideValuesCheckbox.isSelected() == getSettings().hideValuesInTheFile
            ;
    }

    @Override
    public void apply() {
        DotEnvSettings settings = getSettings();

        settings.completionEnabled = completionEnabledCheckbox.isSelected();
        settings.storeValues = storeValuesCheckbox.isSelected();
        settings.hideValuesInTheFile = hideValuesCheckbox.isSelected();
    }

    @Override
    public void reset() {
        completionEnabledCheckbox.setSelected(getSettings().completionEnabled);
        storeValuesCheckbox.setSelected(getSettings().storeValues);
        hideValuesCheckbox.setSelected(getSettings().hideValuesInTheFile);
    }

    private static DotEnvSettings getSettings() {
        return (DotEnvSettings) DotEnvSettings.getInstance();
    }
}
