/*
 * Copyright (c) 2016, 2022, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.plugin.templates;

import freemarker.template.*;

import java.io.File;
import java.io.IOException;
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

    private final GluonProject project;
    private final String projectName;
    private final String templateRoot;

    private final List<File> filesToOpen = new ArrayList<>();

    Template(GluonProject project, String rootResourcePath) {
        this.project = project;
        this.projectName = project.getType();
        this.templateRoot = rootResourcePath;
    }

    public GluonProject getGluonProject() {
        return this.project;
    }

    public String getProjectName() {
        return projectName;
    }
    
    public String getTemplateRoot() {
        return templateRoot;
    }

    public TemplateMetadata getMetadata() {
        if (metadata == null) {
            metadata = TemplateManager.getInstance().getTemplateMetadata(this);
        }
        return metadata;
    }

    public List<File> getFilesToOpen() {
        return filesToOpen;
    }

    public void render(File projectRoot, Map<String, Object> parameters) {
        filesToOpen.clear();

        try {
            Configuration freemarker = new Configuration(Configuration.VERSION_2_3_20);
            freemarker.setDefaultEncoding(StandardCharsets.UTF_8.name());
            freemarker.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            if (getGluonProject().isDownloaded()) {
                freemarker.setDirectoryForTemplateLoading(new File(getTemplateRoot()));
            } else {
                freemarker.setClassForTemplateLoading(Template.class, getTemplateRoot());
            }
            Map<String, Object> parameterMap = createParameterMap(parameters);
            processFile(projectRoot, freemarker, parameterMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            String json = FreeMarkerUtils.processFreemarkerTemplate(freemarker, parameters, recipeResource);

            Recipe recipe = Recipe.parse(new StringReader(json));
            
            RecipeContext recipeContext = new RecipeContext(freemarker, parameters,"/root", projectRoot);
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
