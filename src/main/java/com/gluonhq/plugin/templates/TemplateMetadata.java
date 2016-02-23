package com.gluonhq.plugin.templates;

import javax.json.JsonArray;
import javax.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static com.gluonhq.plugin.templates.Template.*;

public class TemplateMetadata {

    private JsonObject json;

    public TemplateMetadata(JsonObject json) {
        this.json = json;
    }

    public String getTitle() {
        return getStringNonEmpty(KEY_NAME);
    }

    public String getDescription() {
        return getStringNonEmpty(KEY_DESCRIPTION);
    }

    public List<String> getExecutions() {
        List<String> executions = new ArrayList<>();
        if (json.containsKey(KEY_EXECUTIONS)) {
            JsonArray jsonExecutions = json.getJsonArray(KEY_EXECUTIONS);
            for (int i = 0; i < jsonExecutions.size(); i++) {
                executions.add(jsonExecutions.getString(i));
            }
        }
        return executions;
    }

    private String getStringNonEmpty(String key) {
        if (json.containsKey(key)) {
            String value = json.getString(key);
            if (!value.isEmpty()) {
                return value;
            }
        }
        return null;
    }
}
