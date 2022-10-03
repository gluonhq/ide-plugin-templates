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
