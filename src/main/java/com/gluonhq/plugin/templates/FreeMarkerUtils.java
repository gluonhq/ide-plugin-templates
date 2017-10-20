package com.gluonhq.plugin.templates;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FreeMarkerUtils {

    public static Map<String, Object> createParameterMap(Map<String, Object> parameters) {
        final Map<String, Object> parameterMap = new HashMap<>();

        // Parameters supplied by user
        parameterMap.putAll(parameters);
        
        final Object projectName = parameterMap.get(ProjectConstants.PARAM_PROJECT_NAME);
        if (projectName == null || String.valueOf(projectName).isEmpty()) {
            // check if it is a Gluon Function subProject
            final Object projectFnName = parameterMap.get(ProjectConstants.PARAM_GLUON_FUNCTION_PROJECT_NAME);
            if (projectFnName == null || String.valueOf(projectFnName).isEmpty()) {
                throw new IllegalArgumentException("Error: projectName can't be null or empty");
            }
        } else {
            // Gluon Mobile subProject
            String name = String.valueOf(projectName);
            if (! name.toLowerCase(Locale.ROOT).endsWith("app")) {
                name = name + "App";
            }
            parameterMap.put("projectNameApp", name);
        }
        return parameterMap;
    }

    public static String processFreemarkerTemplate(Configuration freemarker, Map<String, Object> parameters, String resource) throws IOException, TemplateException {
        freemarker.template.Template template = freemarker.getTemplate(resource);
        StringWriter out = new StringWriter();
        template.process(parameters, out);
        out.flush();
        final String response = out.toString();
        return response.replaceAll("\\r", "");
    }
    
    public static void setExecutionPermission(Path path) {
        String OS = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (OS.contains("win")) {
            return;
        }
        
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
 
        try {
            Files.setPosixFilePermissions(path, perms);
        } catch (UnsupportedOperationException | IOException ex) {
            Logger.getLogger(FreeMarkerUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
