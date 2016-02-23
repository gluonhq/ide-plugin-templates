package com.gluonhq.plugin.templates;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerUtils {

    public static Map<String, Object> createParameterMap(Map<String, Object> parameters) {
        final Map<String, Object> parameterMap = new HashMap<String, Object>();

        // Parameters supplied by user
        parameterMap.putAll(parameters);

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

}
