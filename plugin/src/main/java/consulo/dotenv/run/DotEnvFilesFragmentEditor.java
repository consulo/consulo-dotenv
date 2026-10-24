/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.dotenv.run;

import consulo.disposer.Disposable;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunConfigurationBase;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import consulo.ui.Button;
import consulo.ui.ButtonStyle;
import consulo.ui.Space;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.layout.DockLayout;
import consulo.ui.layout.VerticalLayout;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * One file field per .env file: the first one on the labeled row, the others and the add button on the row below it.
 */
final class DotEnvFilesFragmentEditor implements RunConfigurationFragmentEditor<RunConfiguration> {
    private final Project myProject;
    private final Disposable myParent;
    private final List<FileChooserTextBoxBuilder.Controller> myRows = new ArrayList<>();
    private final VerticalLayout myFirst = VerticalLayout.create(Space.NONE);
    private final VerticalLayout myRest = VerticalLayout.create(Space.SMALL);
    private final Button myAdd;

    @RequiredUIAccess
    DotEnvFilesFragmentEditor(Project project, Disposable parent) {
        myProject = project;
        myParent = parent;
        myAdd = Button.create(DotEnvLocalize.runConfigurationEnvFilesAdd(), event -> {
            myRows.add(createRow(""));
            refresh();
        });
        myAdd.setIcon(PlatformIconGroup.generalAdd());
        myAdd.addStyle(ButtonStyle.INPLACE);
    }

    @RequiredUIAccess
    @Override
    public void addRows(RunConfigurationFragmentLayout layout) {
        layout.addLabeled(DotEnvLocalize.runConfigurationEnvFilesLabel(), myFirst);
        layout.addUnlabeled(myRest);
    }

    @RequiredUIAccess
    @Override
    public void reset(RunConfiguration target) {
        myRows.clear();
        for (String path : getState(target).getPaths()) {
            myRows.add(createRow(FileUtil.toSystemDependentName(path)));
        }
        if (myRows.isEmpty()) {
            myRows.add(createRow(""));
        }
        refresh();
    }

    @RequiredUIAccess
    @Override
    public void apply(RunConfiguration target) {
        List<String> paths = new ArrayList<>();
        for (FileChooserTextBoxBuilder.Controller row : myRows) {
            String path = StringUtil.notNullize(row.getValue()).trim();
            if (!path.isEmpty()) {
                paths.add(FileUtil.toSystemIndependentName(path));
            }
        }
        getState(target).setPaths(paths);
    }

    @RequiredUIAccess
    private void refresh() {
        myFirst.removeAll();
        myRest.removeAll();
        for (int i = 0; i < myRows.size(); i++) {
            (i == 0 ? myFirst : myRest).add(myRows.get(i).getComponent());
        }
        myRest.add(DockLayout.create().left(myAdd));
    }

    @RequiredUIAccess
    private FileChooserTextBoxBuilder.Controller createRow(String path) {
        List<FileChooserTextBoxBuilder.Controller> self = new ArrayList<>(1);
        AnAction remove = new DumbAwareAction(DotEnvLocalize.runConfigurationEnvFilesRemove(), LocalizeValue.empty(), PlatformIconGroup.actionsGc()) {
            @RequiredUIAccess
            @Override
            public void actionPerformed(AnActionEvent e) {
                remove(self.getFirst());
            }
        };
        FileChooserTextBoxBuilder.Controller row = FileChooserTextBoxBuilder.create(myProject)
            .fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withShowHiddenFiles(true))
            .dialogTitle(DotEnvLocalize.runConfigurationEnvFilesBrowse())
            .lastActions(remove)
            .uiDisposable(myParent)
            .build();
        row.setValue(path, false);
        self.add(row);
        return row;
    }

    @RequiredUIAccess
    private void remove(FileChooserTextBoxBuilder.Controller row) {
        if (myRows.size() == 1) {
            row.setValue("");
            return;
        }
        myRows.remove(row);
        refresh();
    }

    private static DotEnvFilesFragmentState getState(RunConfiguration configuration) {
        return ((RunConfigurationBase) configuration).getFragmentState(DotEnvFilesFragmentState.class);
    }
}
