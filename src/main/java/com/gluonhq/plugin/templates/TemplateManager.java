package com.gluonhq.plugin.templates;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public List<Template> getProjectTemplates(GluonProjectTarget target) {
        List<Template> templates = new ArrayList<>();

        for (GluonProject project : GluonProject.values()) {
            if (!project.isTargetSupported(target)) continue;

            String type = project.getType();
            URL gluonBasicTemplate = Template.class.getResource(CATEGORY_PROJECTS + "/" + type + "/" + TEMPLATE_JSON_NAME);
            if (gluonBasicTemplate != null) {
                templates.add(new Template(type, CATEGORY_PROJECTS + "/" + type));
            }
        }

        // Sort by name (not path as is File's default)
        if (templates.size() > 1) {
            Collections.sort(templates);
        }

        return templates;
    }

    public Template getProjectTemplate(String project) {
        URL template = Template.class.getResource(CATEGORY_PROJECTS + "/" + project + "/" + TEMPLATE_JSON_NAME);
        if (template != null) {
            return new Template(project, CATEGORY_PROJECTS + "/" + project);
        }

        return null;
    }

    public Template getSourceTemplate(String project) {
        URL template = Template.class.getResource(CATEGORY_SOURCES + "/" + project + "/" + TEMPLATE_JSON_NAME);
        if (template != null) {
            return new Template(project, CATEGORY_SOURCES + "/" + project);
        }

        return null;
    }

    TemplateMetadata getTemplateMetadata(String templateLocation) {
        if (templateMap != null) {
            TemplateMetadata metadata = templateMap.get(templateLocation);
            if (metadata != null) {
                return metadata;
            }
        } else {
            templateMap = new HashMap<>();
        }

        try {
            String json = readAllBytes(Template.class.getResourceAsStream(templateLocation + "/" + TEMPLATE_JSON_NAME));
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
