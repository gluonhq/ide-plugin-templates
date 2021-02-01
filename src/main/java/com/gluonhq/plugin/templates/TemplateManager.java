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
