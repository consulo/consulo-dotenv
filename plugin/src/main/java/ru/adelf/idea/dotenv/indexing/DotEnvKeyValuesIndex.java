package ru.adelf.idea.dotenv.indexing;

import consulo.annotation.component.ExtensionImpl;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.index.io.data.DataExternalizer;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileBasedIndexExtension;
import consulo.language.psi.stub.FileContent;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.DotEnvSettings;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesProvider;
import ru.adelf.idea.dotenv.api.index.DotEnvKeyValuesIndexKey;
import ru.adelf.idea.dotenv.api.model.KeyValuePsiElement;
import ru.adelf.idea.dotenv.api.EnvironmentVariablesProviderUtil;

import java.util.HashMap;
import java.util.Map;

@ExtensionImpl
public class DotEnvKeyValuesIndex extends FileBasedIndexExtension<String, String> {

    public static final ID<String, String> KEY = DotEnvKeyValuesIndexKey.KEY;

    @Override
    public @NotNull ID<String, String> getName() {
        return KEY;
    }

    @Override
    public @NotNull DataIndexer<String, String, FileContent> getIndexer() {
        return fileContent -> {
            final Map<String, String> map = new HashMap<>();

            boolean storeValues = DotEnvSettings.getInstance().isStoreValues();

            for (EnvironmentVariablesProvider provider : EnvironmentVariablesProviderUtil.getEnvVariablesProviders()) {
                for (KeyValuePsiElement keyValueElement : provider.getElements(fileContent.getPsiFile())) {
                    if (storeValues) {
                        map.put(keyValueElement.getKey(), keyValueElement.getShortValue());
                    } else {
                        map.put(keyValueElement.getKey(), "");
                    }
                }
            }

            return map;
        };
    }

    @Override
    public @NotNull KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public @NotNull DataExternalizer<String> getValueExternalizer() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public @NotNull FileBasedIndex.InputFilter getInputFilter() {
        return (project, file) -> {
            for (EnvironmentVariablesProvider provider : EnvironmentVariablesProviderUtil.getEnvVariablesProviders()) {
                if (provider.acceptFile(file).isAccepted()) return true;
            }

            return false;
        };
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @Override
    public int getVersion() {
        return 7;
    }
}
