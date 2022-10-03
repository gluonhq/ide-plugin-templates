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

import com.gluonhq.service.TemplateService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Contains all the API for fetching either a list of {@link GluonProject}s or {@link Template}s
 * for a particular {@link GluonProjectTarget}.
 *
 * It also has APIs to convert {@link GluonProject} to a {@link Template}.
 */
public class TemplateManager {

    private static final Logger LOG = Logger.getLogger(TemplateManager.class.getName());

    private static final String CATEGORY_PROJECTS = "projects";
    private static final String CATEGORY_SOURCES = "sources";

    public static final String TEMPLATE_JSON_NAME = "template.json";

    private Map<String, TemplateMetadata> templateMap;

    private static final TemplateManager instance = new TemplateManager();

    private TemplateManager() {
    }

    public static TemplateManager getInstance() {
        return instance;
    }

    public List<GluonProject> getGluonProjects(GluonProjectTarget target, Path configLocation) {
        List<GluonProject> gluonProjects = fetchProjectsFromServer(configLocation);
        if (gluonProjects.isEmpty()) {
            gluonProjects = GluonProject.values()
                    .stream()
                    .filter(project -> project.isTargetSupported(target))
                    .collect(Collectors.toList());
        }
        return gluonProjects;
    }

    public Template getProjectTemplate(GluonProject project) {
        if (project.getProjectLocation() != null) {
            try {
                final String projectDir = project.getProjectLocation().toString() + "/" + CATEGORY_PROJECTS;
                return new Template(project, projectDir);
            } catch (Exception e) {
                LOG.info("Downloading project templates failed. Falling back to local templates...");
            }
        }
        URL template = Template.class.getResource(CATEGORY_PROJECTS + "/" + project.getType() + "/" + TEMPLATE_JSON_NAME);
        if (template != null) {
            return new Template(project, CATEGORY_PROJECTS + "/" + project.getType());
        }
        return null;
    }

    public Template getSourceTemplate(GluonProject project) {
        if (project.getProjectLocation() != null) {
            try {
                final String sourceDir = project.getProjectLocation().toString() + "/" + CATEGORY_SOURCES;
                return new Template(project, sourceDir);
            } catch (Exception e) {
                LOG.info("Downloading source templates failed. Falling back to local templates...");
            }
        }
        URL template = Template.class.getResource(CATEGORY_SOURCES + "/" + project.getType() + "/" + TEMPLATE_JSON_NAME);
        if (template != null) {
            return new Template(project, CATEGORY_SOURCES + "/" + project.getType());
        }
        return null;
    }

    TemplateMetadata getTemplateMetadata(Template template) {
        final String templateLocation = template.getTemplateRoot();
        if (templateMap != null) {
            TemplateMetadata metadata = templateMap.get(templateLocation);
            if (metadata != null) {
                return metadata;
            }
        } else {
            templateMap = new HashMap<>();
        }

        try {
            String json;
            if (template.getGluonProject().isDownloaded()) {
                json = new String(Files.readAllBytes(Paths.get(templateLocation + "/" + TEMPLATE_JSON_NAME)));
            } else {
                json = readAllBytes(Template.class.getResourceAsStream(templateLocation + "/" + TEMPLATE_JSON_NAME));
            }
            try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
                JsonObject jsonObject = jsonReader.readObject();
                TemplateMetadata metadata = new TemplateMetadata(jsonObject);
                templateMap.put(templateLocation, metadata);
                return metadata;
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, null, e);
        }
        return null;
    }

    private List<GluonProject> fetchProjectsFromServer(Path configLocation) {
        // Ping a server and get List of JsonObjects
        final TemplateService templateService = new TemplateService();
        return templateService.getProjects(configLocation);
    }

    private List<Template> createTemplatesFromEnum(GluonProjectTarget target) {
        return convertGluonProjectToTemplates(GluonProject.values(), target);
    }

    private List<Template> convertGluonProjectToTemplates(List<GluonProject> projects, GluonProjectTarget target) {
        List<Template> templates = new ArrayList<>();
        for (GluonProject project : projects) {
            if (!project.isTargetSupported(target)) continue;
            final Template template = getProjectTemplate(project);
            if (template != null) {
                templates.add(template);
            }
        }
        return templates;
    }

    private String readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = null;
        BufferedInputStream bis = null;
        try {
            byte[] bytes = new byte[1024];
            baos = new ByteArrayOutputStream(bytes.length);
            bis = new BufferedInputStream(is);
            int bytesRead = bis.read(bytes);
            while (bytesRead > -1) {
                baos.write(bytes, 0, bytesRead);
                bytesRead = bis.read(bytes);
            }

            return baos.toString("UTF-8");
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close ByteArrayOutputStream.", e);
                }
            }
            if (bis != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close InputStream.", e);
                }
            }
        }
    }
}
