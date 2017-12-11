package com.gluonhq.plugin.code;

import static com.gluonhq.plugin.templates.ProjectConstants.DEFAULT_CLOUDLINK_HOST;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

public class CodeTask implements Callable<List<String>> {
    
    private static final Logger LOGGER = Logger.getLogger(CodeTask.class.getName());
    
    private final String applicationIdeKey;
  
    public CodeTask(String applicationIdeKey) {
        this.applicationIdeKey = applicationIdeKey;
    }

    @Override
    public List<String> call() {
        HttpURLConnection openConnection = null;
        if (applicationIdeKey != null) {
            try {
                URL url = new URL(DEFAULT_CLOUDLINK_HOST + "/3/data/ide/functions");
                openConnection = (HttpURLConnection) url.openConnection();
                openConnection.addRequestProperty("Authorization", "GluonIde " + applicationIdeKey);
                int status = openConnection.getResponseCode();
                if (status == 200 || status == 201) {
                    List<String> list = new ArrayList<>();
                    try (JsonReader createReader = Json.createReader(new InputStreamReader(openConnection.getInputStream()))) {
                        JsonArray readArray = createReader.readArray();
                        readArray.iterator().forEachRemaining(value -> {
                            final String s = String.valueOf(value);
                            list.add(s.substring(1, s.length() - 1));
                        });
                    }
                    return list;
                }
            } catch (MalformedURLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } finally {
                if (openConnection != null) {
                    try {
                        openConnection.disconnect();
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
    
}
