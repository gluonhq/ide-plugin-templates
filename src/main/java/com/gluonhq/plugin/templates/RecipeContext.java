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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecipeContext {

    private static final Logger LOG = Logger.getLogger(RecipeContext.class.getName());

    private Configuration freemarker;
    private Map<String, Object> parameters;
    private String templateRoot;
    private File outputRoot;

    private final List<File> filesToOpen;

    public RecipeContext(Configuration freemarker,
                         Map<String, Object> parameters,
                         String templateRoot,
                         File outputRoot) {
        this.freemarker = freemarker;
        this.parameters = parameters;
        this.templateRoot = templateRoot;
        this.outputRoot = outputRoot;
        this.filesToOpen = new ArrayList<>();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<File> getFilesToOpen() {
        return filesToOpen;
    }

    public void copy(String from, File to, boolean open) {
        try {
            File file = copyTemplateResource(from, to);
            if (open) {
                filesToOpen.add(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyList(String list, String base, File to) {
        try {
            copyTemplateListResources(list, base, to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mkdir(File at) {
        at = getTargetFile(at);
        at.mkdirs();
    }
    
    public void process(String from, File to, boolean open) {
        try {
            from = getSourceResource(from);
            String json = FreeMarkerUtils.processFreemarkerTemplate(freemarker, parameters, from);

            File targetFile = getTargetFile(to);
            targetFile.getParentFile().mkdirs();

            writeStringToFile(json, targetFile);

            if (open) {
                filesToOpen.add(targetFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void permission(File at) {
        at = getTargetFile(at);
        FreeMarkerUtils.setExecutionPermission(at.toPath());
    }

    private File copyTemplateResource(String from, File to) throws IOException {
        from = getSourceResource(from);
        to = getTargetFile(to);

        LOG.log(Level.INFO, "Copying from " + from + " to " + to + ".");

        File destPath = to.getParentFile();
        destPath.mkdirs();

        File destFile = new File(destPath, to.getName());
        copyResourcesToFile(from, destFile);
        return destFile;
    }

    private void copyTemplateListResources(String list, String base, File to) throws IOException {
        list = getSourceResource(list);
        to = getTargetFile(to);

        LOG.log(Level.INFO, "Copying list " + list + " to " + to + ".");

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(Template.class.getResourceAsStream(list)));
            String line = reader.readLine();
            while (line != null) {
                if (line.isEmpty()) {
                    continue;
                }

                line = line.replaceAll("\\\\", "/");
                String from = getSourceResource(base + "/" + line);

                File destFile = new File(to, line);
                File destPath = destFile.getParentFile();
                destPath.mkdirs();

                copyResourcesToFile(from, destFile);

                line = reader.readLine();
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close BufferedReader.", e);
                }
            }
        }
    }

    public String getSourceResource(String resource) {
        if (resource.startsWith("/")) {
            return resource.substring(1);
        } else {
            // If it's a relative file path, get the data from the template data directory
            return templateRoot + "/" + resource;
        }
    }

    public File getTargetFile(File file) {
        if (file.isAbsolute()) {
            return file;
        }
        return new File(outputRoot, file.getPath());
    }

    private void copyResourcesToFile(String resource, File file) {
        LOG.log(Level.INFO, "Copying from " + resource + " to " + file + ".");

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bis = new BufferedInputStream(Template.class.getResourceAsStream(resource));

            byte[] bytes = new byte[1024];
            int bytesRead = bis.read(bytes);
            while (bytesRead > -1) {
                bos.write(bytes, 0, bytesRead);
                bytesRead = bis.read(bytes);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to write file " + file.getAbsolutePath(), e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close BufferedOutputStream", e);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close BufferedInputStream", e);
                }
            }
        }
    }

    private void writeStringToFile(String string, File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(string);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to write file " + file.getAbsolutePath(), e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close BufferedOutputStream", e);
                }
            }
        }
    }
}
