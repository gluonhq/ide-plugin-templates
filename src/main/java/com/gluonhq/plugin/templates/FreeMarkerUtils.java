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
            // Gluon subProject
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
