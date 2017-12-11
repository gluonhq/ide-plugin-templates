package com.gluonhq.plugin.cloudlink;

import com.gluonhq.plugin.templates.ProjectConstants;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountTask implements Callable<String> {
    
    private static final Logger LOGGER = Logger.getLogger(AccountTask.class.getName());
    
    private final String user;
    private final String password;
  
    public AccountTask(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public String call() {
        HttpURLConnection openConnection = null;
        if (user != null && password != null) {
            String key = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
            try {
                URL url = new URL(ProjectConstants.DEFAULT_CLOUDLINK_HOST + "/3/account/dashboard/applications");
                openConnection = (HttpURLConnection) url.openConnection();
                openConnection.addRequestProperty("Authorization", "Basic " + key);
                int status = openConnection.getResponseCode();
                if (status == 200) {
                    return key;
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
