package com.gluonhq.plugin.templates;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class OptInHelper {

    public static void optIn(String email, Boolean keepUpToDate, String ide, String id, String version, boolean update) {
        try {
            String java = System.getProperty("java.version");
            String os = System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version");
            String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8") + 
                                   "&subscribe=" + keepUpToDate + 
                                   "&os=" + URLEncoder.encode(os, "UTF-8") + 
                                   "&java=" + URLEncoder.encode(java, "UTF-8") + 
                                   "&type=" + ide +
                                   "&id=" + id +
                                   "&version=" + version +
                                   (update ? "&update=true" : "");
            URL url = new URL("http://usage.gluonhq.com/ul/log?" + urlParameters);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.connect();
            try (DataInputStream in = new DataInputStream(conn.getInputStream())) {
                while (in.read() > -1) { }
            }
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

}
