/*
 * Copyright (c) 2021, 2022, Gluon Software
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
package com.gluonhq.service;

import com.gluonhq.plugin.templates.GluonProject;
import com.gluonhq.plugin.templates.GluonProjectTarget;

import javax.json.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateService {

    private static final Logger LOG = Logger.getLogger(TemplateService.class.getName());
    private static final String CONFIG_LOCATION = "https://download2.gluonhq.com/ideplugins/templatesv2/config.json";

    /**
     * A list of GluonProject generated from a remote config.json file.
     * @param configLocation Remote location of config.json
     * @return List of GluonProject
     */
    public List<GluonProject> getProjects(Path configLocation) {
        return createProjectsFromConfig(configLocation);
    }

    /**
     * Downloads a remote file from the provided location and stores it as a temp file with the provided tempFileName.
     * @param location Remote location of the file
     * @param tempFileName File name to be used for the file
     * @return Local path of the file
     */
    public Path downloadFile(String location, String tempFileName) {
        Path tempFile = null;
        try {
            ReadableByteChannel readChannel = Channels.newChannel(new URL(location).openStream());
            tempFile = Files.createTempFile(tempFileName, ".zip");
            FileOutputStream fileOS = new FileOutputStream(tempFile.toFile());
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Downloading Gluon Project failed", e);
        }
        return tempFile;
    }

    private List<GluonProject> createProjectsFromConfig(Path configLocation) {
        final List<GluonProject> gluonProjects = new ArrayList<>();
        JsonReader reader = null;
        try {
            if (Files.exists(configLocation)) {
                reader = Json.createReader(new FileReader(configLocation.toFile()));
            } else {
                final String jsonString = readJsonFromServer(CONFIG_LOCATION);
                Files.write(configLocation, jsonString.getBytes(StandardCharsets.UTF_8));
                reader = Json.createReader(new StringReader(jsonString));
            }
            JsonArray jsonArray = reader.readArray();
            for (JsonValue jsonValue : jsonArray) {
                JsonObject jsonObject = (JsonObject) jsonValue;
                final String id = jsonObject.getString("id");
                final String name = jsonObject.getString("name");
                final String description = jsonObject.getString("description");
                final String location = jsonObject.getString("location");
                final GluonProjectTarget target = GluonProjectTarget.valueOf(jsonObject.getString("target"));
                gluonProjects.add(new GluonProject(id, name, description, location, target));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return gluonProjects;
    }

    private static String readJsonFromServer(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
