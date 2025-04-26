package consulo.dotenv;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileNameMatcherFactory;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import ru.adelf.idea.dotenv.DotEnvFileType;

/**
 * @author VISTALL
 * @since 2025-04-26
 */
@ExtensionImpl
public class DotEnvFileTypeFactory extends FileTypeFactory {
    private final FileNameMatcherFactory myFileNameMatcherFactory;

    @Inject
    public DotEnvFileTypeFactory(FileNameMatcherFactory fileNameMatcherFactory) {
        myFileNameMatcherFactory = fileNameMatcherFactory;
    }

    @Override
    public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(DotEnvFileType.INSTANCE, myFileNameMatcherFactory.createExactFileNameMatcher(".env"));
        fileTypeConsumer.consume(DotEnvFileType.INSTANCE, myFileNameMatcherFactory.createWildcardFileNameMatcher(".env.*"));
    }
}
