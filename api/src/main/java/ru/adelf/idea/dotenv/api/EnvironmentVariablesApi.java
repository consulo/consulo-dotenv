package ru.adelf.idea.dotenv.api;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.PsiSearchHelper;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import ru.adelf.idea.dotenv.api.index.DotEnvKeyValuesIndexKey;
import ru.adelf.idea.dotenv.api.model.FileAcceptResult;

import java.util.*;
import java.util.function.Predicate;

public final class EnvironmentVariablesApi {

    public static @NotNull Map<String, String> getAllKeyValues(Project project) {
        FileBasedIndex fileBasedIndex = FileBasedIndex.getInstance();
        Map<String, String> keyValues = new HashMap<>();
        Map<String, String> secondaryKeyValues = new HashMap<>();
        Map<VirtualFile, FileAcceptResult> resultsCache = new HashMap<>();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        boolean showValues = DotEnvSettings.getInstance().isStoreValues();

        fileBasedIndex.processAllKeys(DotEnvKeyValuesIndexKey.KEY, key -> {
            for (VirtualFile virtualFile : fileBasedIndex.getContainingFiles(DotEnvKeyValuesIndexKey.KEY, key, scope)) {

                FileAcceptResult fileAcceptResult;

                if (resultsCache.containsKey(virtualFile)) {
                    fileAcceptResult = resultsCache.get(virtualFile);
                } else {
                    fileAcceptResult = getFileAcceptResult(virtualFile);
                    resultsCache.put(virtualFile, fileAcceptResult);
                }

                if (!fileAcceptResult.isAccepted()) {
                    continue;
                }

                fileBasedIndex.processValues(DotEnvKeyValuesIndexKey.KEY, key, virtualFile, ((file, val) -> {
                    String keyValue = val;

                    if (!showValues) {
                        keyValue = "";
                    }

                    if (fileAcceptResult.isPrimary()) {
                        keyValues.putIfAbsent(key, keyValue);
                    } else {
                        secondaryKeyValues.putIfAbsent(key, keyValue);
                    }

                    return true;
                }), scope);
            }

            return true;
        }, project);

        secondaryKeyValues.putAll(keyValues);

        return secondaryKeyValues;
    }

    /**
     * @param project project
     * @param key     environment variable key
     * @return All key declarations, in .env files, Dockerfile, docker-compose.yml, etc
     */
    public static PsiElement @NotNull [] getKeyDeclarations(Project project, String key) {
        List<PsiElement> targets = new ArrayList<>();
        List<PsiElement> secondaryTargets = new ArrayList<>();

        FileBasedIndex.getInstance().getFilesWithKey(DotEnvKeyValuesIndexKey.KEY, new HashSet<>(Collections.singletonList(key)), virtualFile -> {
            PsiFile psiFileTarget = PsiManager.getInstance(project).findFile(virtualFile);
            if (psiFileTarget == null) {
                return true;
            }

            for (EnvironmentVariablesProvider provider : EnvironmentVariablesProviderUtil.getEnvVariablesProviders()) {
                FileAcceptResult fileAcceptResult = provider.acceptFile(virtualFile);
                if (!fileAcceptResult.isAccepted()) {
                    continue;
                }

                (fileAcceptResult.isPrimary() ? targets : secondaryTargets).addAll(EnvironmentVariablesUtil.getElementsByKey(key, provider.getElements(psiFileTarget)));
            }

            return true;
        }, GlobalSearchScope.allScope(project));

        return (!targets.isEmpty() ? targets : secondaryTargets).toArray(PsiElement.EMPTY_ARRAY);
    }

    /**
     * @param project project
     * @param key     environment variable key
     * @return All key usages, like getenv('KEY')
     */
    public static PsiElement @NotNull [] getKeyUsages(Project project, String key) {
        Set<PsiElement> targets = new HashSet<>();

        PsiSearchHelper searchHelper = PsiSearchHelper.getInstance(project);

        Predicate<PsiFile> psiFileProcessor = psiFile -> {
            for (EnvironmentVariablesUsagesProvider provider : EnvironmentVariablesProviderUtil.getEnvVariablesUsagesProviders()) {
                targets.addAll(EnvironmentVariablesUtil.getUsagesElementsByKey(key, provider.getUsages(psiFile)));
            }

            return true;
        };

        searchHelper.processAllFilesWithWord(key, GlobalSearchScope.allScope(project), psiFileProcessor, true);
        searchHelper.processAllFilesWithWordInLiterals(key, GlobalSearchScope.allScope(project), psiFileProcessor);
        searchHelper.processAllFilesWithWordInText(key, GlobalSearchScope.allScope(project), psiFileProcessor, true);

        return targets.toArray(PsiElement.EMPTY_ARRAY);
    }

    private static FileAcceptResult getFileAcceptResult(VirtualFile virtualFile) {
        for (EnvironmentVariablesProvider provider : EnvironmentVariablesProviderUtil.getEnvVariablesProviders()) {
            FileAcceptResult fileAcceptResult = provider.acceptFile(virtualFile);
            if (!fileAcceptResult.isAccepted()) {
                continue;
            }

            return fileAcceptResult;
        }

        return FileAcceptResult.NOT_ACCEPTED;
    }
}
