package com.gluonhq.plugin.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class UploadFunction {

    private static final String LINE_FEED = "\r\n";

    private static final String CONTENT_TYPE = "multipart/form-data";

    public static void upload(String gluonIdeKey, String functionName, String functionEntrypoint, File gfBundle) throws IOException {
        String spec = "https://stagingcloud.gluonhq.com/3/data/ide/functions/GLUON/" + functionName;

        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.addRequestProperty("Authorization", "GluonIde " + gluonIdeKey);

        connection.setDoOutput(true);

        String boundary = addMultipartBoundary(connection);

        try (OutputStream outputStream = connection.getOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
             FileInputStream fileInputStream = new FileInputStream(gfBundle)) {
            addMultipartFormField(boundary, writer, "entrypoint", functionEntrypoint);

            addMultipartFormField(boundary, writer, outputStream, "bundle", fileInputStream);

            writer.append(LINE_FEED).flush();
            writer.append("--").append(boundary).append("--").append(LINE_FEED);
        }

        InputStream finalInputStream;
        if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            InputStream inputStream = connection.getInputStream();
            PushbackInputStream pb = new PushbackInputStream(inputStream, 2);
            byte[] hdr = new byte[2];
            int bytesRead = pb.read(hdr);
            if (bytesRead >= 0) {
                pb.unread(hdr, 0, bytesRead);
            }
            if (bytesRead == 2 && hdr[0] == (byte) GZIPInputStream.GZIP_MAGIC && hdr[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)) {
                finalInputStream = new GZIPInputStream(pb);
            } else {
                finalInputStream = pb;
            }
        } else {
            finalInputStream = connection.getErrorStream();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalInputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static String addMultipartBoundary(HttpURLConnection connection) {
        String boundary = "---" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", CONTENT_TYPE + "; boundary=" + boundary);
        return boundary;
    }

    private static void addMultipartFormField(String boundary, Writer writer, String name, String value) throws IOException {
        writer.append("--").append(boundary).append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE_FEED)
                .append("Content-Type: text/plain; charset=UTF-8").append(LINE_FEED)
                .append(LINE_FEED)
                .append(value).append(LINE_FEED);
        writer.flush();
    }

    private static void addMultipartFormField(String boundary, Writer writer, OutputStream os, String name, InputStream data) throws IOException {
        writer.append("--").append(boundary).append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"raw\"").append(LINE_FEED)
                .append("Content-Type: application/octet-stream").append(LINE_FEED)
                .append("Content-Transfer-Encoding: binary").append(LINE_FEED)
                .append(LINE_FEED);
        writer.flush();

        byte[] bytes = new byte[8192];
        while (data.read(bytes) != -1) {
            os.write(bytes);
        }
        os.flush();

        writer.append(LINE_FEED);
        writer.flush();
    }
}
