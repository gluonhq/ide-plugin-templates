package com.gluonhq.plugin.cloudlink;

import com.gluonhq.plugin.templates.ProjectConstants;
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
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ApplicationsTask implements Callable<List<Application>> {
    
    private static final Logger LOGGER = Logger.getLogger(ApplicationsTask.class.getName());
    
    private final String userKey;
  
    public ApplicationsTask(String userKey) {
        this.userKey = userKey;
    }

    @Override
    public List<Application> call() {
        HttpURLConnection openConnection = null;
        List<Application> applications = new ArrayList<>();
        if (userKey != null) {
            try {
                URL url = new URL(ProjectConstants.DEFAULT_CLOUDLINK_HOST + "/3/account/dashboard/applications");
                openConnection = (HttpURLConnection) url.openConnection();
                openConnection.addRequestProperty("Authorization", "Basic " + userKey);
                int status = openConnection.getResponseCode();
                if (status == 200 || status == 201) {
                    try (JsonReader createReader = Json.createReader(new InputStreamReader(openConnection.getInputStream()))) {
                        JsonArray readArray = createReader.readArray();
                        readArray.iterator().forEachRemaining(value -> {
                            JsonObject obj = (JsonObject) value;
                            Application app = new Application();
                            app.setName(obj.getString("name"));
                            app.setIdentifier(obj.getString("identifier"));
                            app.setSecret(obj.getString("secret"));
                            app.setIdeKey(obj.getString("ideKey"));
                            applications.add(app);
                        });
                    }
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
        return applications;
    }
    
}
