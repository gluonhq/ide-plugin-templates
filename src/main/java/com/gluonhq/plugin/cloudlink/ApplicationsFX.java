package com.gluonhq.plugin.cloudlink;

import static com.gluonhq.plugin.templates.ProjectConstants.DEFAULT_CLOUDLINK_HOST;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.SwingUtilities;

public class ApplicationsFX extends BorderPane {

    private static final Logger LOGGER = Logger.getLogger(ApplicationsFX.class.getName());
    
    @FXML
    private TextField keyText;

    @FXML
    private TextField secretText;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;

    @FXML
    private Button logoutButton;
    
    @FXML
    private ChoiceBox<Application> currentAppBox;
    
    private Credentials credentials;
    private Application existingApp;
    
    public ApplicationsFX(String userKey, boolean keepLogged) {
        credentials = new Credentials(userKey, keepLogged, null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("applications.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading applications.fxml", ex);
        }
    }
    
    public void initialize() {
        currentAppBox.setConverter(new StringConverter<Application>() {
            
            @Override public String toString(Application app) {
                return app.getName();
            }

            @Override public Application fromString(String string) {
                return null;
            }
        });
        
        currentAppBox.valueProperty().addListener(o -> {
            final Application app = currentAppBox.getValue();
            if (app != null) {
                keyText.setText(app.getIdentifier());
                secretText.setText(app.getSecret());
            } else {
                keyText.clear();
                secretText.clear();
            }
            applyButton.setDisable(currentAppBox.getValue() == null || currentAppBox.getValue().equals(existingApp));
        });
        
        currentAppBox.prefWidthProperty().bind(keyText.widthProperty());
        
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); 
        ButtonBar.setButtonData(applyButton, ButtonBar.ButtonData.APPLY); 
        ButtonBar.setButtonData(logoutButton, ButtonBar.ButtonData.HELP); 
        
        cancelButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> credentials.setApplication(null)));
        applyButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> credentials.setApplication(currentAppBox.getValue())));
        logoutButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> credentials.setUserKey(false, null)));
        
        logoutButton.setVisible(credentials.isKeepLogged());
    }
    
    public void loadApplications(String jsonConfig) {
        setCursor(Cursor.WAIT);
        buttonBar.setDisable(true);
        new Thread(getApplicationsTask(jsonConfig))
                .start();
    }
    
    private Task<List<Application>> getApplicationsTask(String jsonConfig) {
        return new Task<List<Application>>() {

            @Override
            protected List<Application> call() throws Exception {
                HttpURLConnection openConnection = null;
                try {
                    URL url = new URL(DEFAULT_CLOUDLINK_HOST + "/3/account/dashboard/applications");
                    openConnection = (HttpURLConnection) url.openConnection();
                    openConnection.addRequestProperty("Authorization", "Basic " + credentials.getUserKey());

                    int status = openConnection.getResponseCode();
                    switch (status) {
                        case 200:
                        case 201:
                            List<Application> list = new ArrayList<>();
                            try (JsonReader createReader = Json.createReader(new InputStreamReader(openConnection.getInputStream()))) {
                                JsonArray readArray = createReader.readArray();

                                readArray.iterator().forEachRemaining(value -> {
                                    JsonObject obj = (JsonObject) value;
                                    Application app = new Application();
                                    app.setName(obj.getString("name"));
                                    app.setIdentifier(obj.getString("identifier"));
                                    app.setSecret(obj.getString("secret"));
                                    app.setIdeKey(obj.getString("ideKey"));
                                    list.add(app);
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
                return null;
            }
            
            @Override
            protected void succeeded() {
                List<Application> list = getValue();
                if (list != null) {
                    currentAppBox.getItems().setAll(list);
                    if (jsonConfig != null && !jsonConfig.isEmpty()) {
                        JsonObject object = Json.createReader(new StringReader(jsonConfig)).readObject()
                                .getJsonObject("gluonCredentials");
                        if (object != null) {
                            existingApp = currentAppBox.getItems().stream()
                                    .filter(app -> app.getIdentifier().equals(object.getString("applicationKey")) &&
                                            app.getSecret().equals(object.getString("applicationSecret")))
                                    .findFirst()
                                    .orElse(null);
                            currentAppBox.setValue(existingApp);
                        }
                    }
                } else {
                    currentAppBox.getItems().clear();
                }
                buttonBar.setDisable(false);
                setCursor(Cursor.DEFAULT);
            }

            @Override
            protected void failed() {
                currentAppBox.getItems().clear();
                buttonBar.setDisable(false);
                setCursor(Cursor.DEFAULT);
            }
            
        };
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
