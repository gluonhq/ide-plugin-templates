package com.gluonhq.plugin.templates;

import freemarker.template.*;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Template implements Comparable<Template> {

    private static final Logger LOG = Logger.getLogger(Template.class.getName());

    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_EXECUTIONS = "executions";

    private TemplateMetadata metadata;

    private final String projectName;
    private final String templateRoot;

    private final List<File> filesToOpen = new ArrayList<>();

    Template(String projectName, String rootResourcePath) {
        this.projectName = projectName;
        templateRoot = rootResourcePath;
    }

    public String getProjectName() {
        return projectName;
    }

    public TemplateMetadata getMetadata() {
        if (metadata == null) {
            metadata = TemplateManager.getInstance().getTemplateMetadata(templateRoot);
        }

        return metadata;
    }

    public List<File> getFilesToOpen() {
        return filesToOpen;
    }

    public void render(File projectRoot, Map<String, Object> parameters) {
        filesToOpen.clear();

        Configuration freemarker = new Configuration();
        freemarker.setDefaultEncoding(StandardCharsets.UTF_8.name());
        freemarker.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarker.setClassForTemplateLoading(Template.class, "");

        Map<String, Object> parameterMap = createParameterMap(parameters);
        processFile(projectRoot, freemarker, parameterMap);
    }

    private Map<String, Object> createParameterMap(Map<String, Object> parameters) {
        return FreeMarkerUtils.createParameterMap(parameters);
    }

    private void processFile(File projectRoot,
                             Configuration freemarker,
                             Map<String, Object> parameters) {
        List<String> executions = getMetadata().getExecutions();
        for (String execution : executions) {
            executeRecipe(freemarker, execution, parameters, projectRoot);
        }
    }

    private void executeRecipe(Configuration freemarker, String recipeResource, Map<String, Object> parameters, File projectRoot) {
        try {
            String json = FreeMarkerUtils.processFreemarkerTemplate(freemarker, parameters, templateRoot + "/" + recipeResource);

            Recipe recipe = Recipe.parse(new StringReader(json));

            RecipeContext recipeContext = new RecipeContext(freemarker, parameters, templateRoot + "/root", projectRoot);
            recipe.execute(recipeContext);

            filesToOpen.addAll(recipeContext.getFilesToOpen());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    @Override
    public int compareTo(Template o) {
        return projectName.compareTo(o.projectName);
    }
}
