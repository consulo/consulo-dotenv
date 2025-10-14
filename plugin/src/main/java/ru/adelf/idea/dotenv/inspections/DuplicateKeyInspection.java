package ru.adelf.idea.dotenv.inspections;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.dotenv.inspection.DotEnvLocalInspectionTool;
import consulo.dotenv.localize.DotEnvLocalize;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dotenv.DotEnvPsiElementsVisitor;
import ru.adelf.idea.dotenv.api.model.KeyValuePsiElement;
import ru.adelf.idea.dotenv.psi.DotEnvFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ExtensionImpl
public class DuplicateKeyInspection extends DotEnvLocalInspectionTool {
    // Change the display name within the plugin.xml
    // This needs to be here as otherwise the tests will throw errors.
    @Override
    public @NotNull LocalizeValue getDisplayName() {
        return DotEnvLocalize.inspectionMessageDuplicateKey();
    }

    @Override
    public boolean runForWholeFile() {
        return true;
    }

    @Override
    public @Nullable ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if(!(file instanceof DotEnvFile)) {
            return null;
        }

        return analyzeFile(file, manager, isOnTheFly).getResultsArray();
    }

    @RequiredReadAction
    private static @NotNull ProblemsHolder analyzeFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        DotEnvPsiElementsVisitor visitor = new DotEnvPsiElementsVisitor();
        file.acceptChildren(visitor);

        ProblemsHolder problemsHolder = manager.createProblemsHolder(file, isOnTheFly);

        Map<String, PsiElement> existingKeys = new HashMap<>();
        Set<PsiElement> markedElements = new HashSet<>();
        for(KeyValuePsiElement keyValue : visitor.getCollectedItems()) {
            final String key = keyValue.getKey();

            if(existingKeys.containsKey(key)) {
                problemsHolder.registerProblem(keyValue.getElement(), DotEnvLocalize.inspectionMessageDuplicateKey().get());

                PsiElement markedElement = existingKeys.get(key);
                if(!markedElements.contains(markedElement)) {
                    problemsHolder.registerProblem(markedElement, DotEnvLocalize.inspectionMessageDuplicateKey().get());
                    markedElements.add(markedElement);
                }
            } else {
                existingKeys.put(key, keyValue.getElement());
            }
        }

        return problemsHolder;
    }
}
