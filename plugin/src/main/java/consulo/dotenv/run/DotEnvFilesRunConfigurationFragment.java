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

import consulo.annotation.component.ExtensionImpl;
import consulo.disposer.Disposable;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.execution.configuration.EnvironmentAware;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunConfigurationBase;
import consulo.execution.configuration.fragment.CommandLineFragmentPatcher;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.StandardRunConfigurationFragmentOrder;
import consulo.execution.localize.ExecutionLocalize;
import consulo.localize.LocalizeValue;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Variables of .env files for any run configuration with an environment. A variable the command line already defines keeps its value,
 * later files win over earlier ones.
 */
@ExtensionImpl
public class DotEnvFilesRunConfigurationFragment implements CommandLineFragmentPatcher<RunConfiguration> {
    @Override
    public String getId() {
        return "dotEnvFiles";
    }

    @Override
    public LocalizeValue getName() {
        return DotEnvLocalize.runConfigurationFragmentEnvFiles();
    }

    @Override
    public LocalizeValue getGroup() {
        return ExecutionLocalize.runConfigurationFragmentGroupOperatingSystem();
    }

    @Override
    public int getOrder() {
        return StandardRunConfigurationFragmentOrder.ENVIRONMENT + 10;
    }

    @Override
    public Class<DotEnvFilesFragmentState> getStateClass() {
        return DotEnvFilesFragmentState.class;
    }

    @Override
    public boolean isApplicable(RunConfiguration configuration) {
        return configuration instanceof RunConfigurationBase && configuration instanceof EnvironmentAware;
    }

    @Override
    public boolean isVisibleByDefault(RunConfiguration target) {
        return !getState(target).getPaths().isEmpty();
    }

    @Override
    public void applyHidden(RunConfiguration target) {
        getState(target).setPaths(List.of());
    }

    @RequiredUIAccess
    @Override
    public RunConfigurationFragmentEditor<RunConfiguration> createEditor(Project project, Disposable parent) {
        return new DotEnvFilesFragmentEditor(project, parent);
    }

    @Override
    public void patchCommandLine(RunConfiguration target, GeneralCommandLine commandLine) throws ExecutionException {
        List<String> paths = getState(target).getPaths();
        if (paths.isEmpty()) {
            return;
        }

        Map<String, String> variables = new LinkedHashMap<>();
        for (String path : paths) {
            variables.putAll(read(resolve(path, commandLine)));
        }

        Map<String, String> environment = commandLine.getEnvironment();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            environment.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    private static Path resolve(String path, GeneralCommandLine commandLine) {
        Path file = Path.of(FileUtil.toSystemDependentName(path));
        Path workingDirectory = commandLine.getWorkDirectory() == null ? null : commandLine.getWorkDirectory().toPath();
        return file.isAbsolute() || workingDirectory == null ? file : workingDirectory.resolve(file);
    }

    private static Map<String, String> read(Path file) throws ExecutionException {
        if (!Files.isRegularFile(file)) {
            throw new ExecutionException(DotEnvLocalize.runConfigurationEnvFileNotFound(file.toString()));
        }
        try {
            return DotEnvFileParser.parse(Files.readString(file));
        }
        catch (IOException e) {
            throw new ExecutionException(DotEnvLocalize.runConfigurationEnvFileCannotRead(file.toString()).get(), e);
        }
    }

    private static DotEnvFilesFragmentState getState(RunConfiguration configuration) {
        return ((RunConfigurationBase) configuration).getFragmentState(DotEnvFilesFragmentState.class);
    }
}
