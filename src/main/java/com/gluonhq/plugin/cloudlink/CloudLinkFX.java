package com.gluonhq.plugin.cloudlink;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.SwingUtilities;

public class CloudLinkFX extends BorderPane {

    private final ChoiceBox<Application> currentAppBox;
    private final TextField userField;
    private final PasswordField passField;

    private final TextField keyField, secretField;
    
    private final Button buttonCancel;
    private final Button buttonOk;
    
    private final Credentials credentials;
    private Application existingApp;
    
    public CloudLinkFX(String jsonConfig) {
        
        existingApp = null;
        credentials = new Credentials();
        
        final Label label = new Label("CloudLink configuration");
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add("title");
        
        setTop(label);
        
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.setMinSize(700, 400);
        grid.setPrefSize(700, 400);
        grid.setMaxSize(700, 400);
        
        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(25);
        ColumnConstraints col2Constraints = new ColumnConstraints();
        col2Constraints.setPercentWidth(75);
        grid.getColumnConstraints().addAll(col1Constraints, col2Constraints);
        
        grid.add(new Label("User:"), 0, 0);
        userField = new TextField();
        userField.setPromptText("User");
        grid.add(userField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        passField = new PasswordField();
        passField.setPromptText("Password");
        grid.add(passField, 1, 1);
        
        currentAppBox = new ChoiceBox<>();
        currentAppBox.setPrefWidth(300);
        currentAppBox.setConverter(new StringConverter<Application>() {
            
            @Override public String toString(Application app) {
                return app.getName();
            }

            @Override public Application fromString(String string) {
                return null;
            }
        });
        
        Button login = new Button("Login");
        login.disableProperty().bind(userField.textProperty().isEmpty().or(passField.textProperty().isEmpty()));
        login.setOnAction(e -> {
            loadApplications(userField.getText(), passField.getText())
                    .ifPresent(currentAppBox.getItems()::addAll);
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
        });
        
        grid.add(login, 0, 2);
        grid.add(new Label("Applications: "), 0, 3);
        grid.add(currentAppBox, 1, 3);
        
        grid.add(new Label("Application Key:"), 0, 4);
        keyField = new TextField();
        grid.add(keyField, 1, 4);
        
        grid.add(new Label("Application Secret:"), 0, 5);
        secretField = new TextField();
        grid.add(secretField, 1, 5);
        
        buttonCancel = new Button("Cancel");
        buttonOk = new Button("Apply");
        buttonOk.setDisable(true);
        
        currentAppBox.valueProperty().addListener(o -> {
            final Application app = currentAppBox.getValue();
            if (app != null) {
                keyField.setText(app.getIdentifier());
                secretField.setText(app.getSecret());
            }
            buttonOk.setDisable(currentAppBox.getValue() == null || currentAppBox.getValue().equals(existingApp));
        });
        
        setCenter(grid);
        
        buttonCancel.setCancelButton(true);
        ButtonBar.setButtonData(buttonCancel, ButtonBar.ButtonData.CANCEL_CLOSE); 
        buttonCancel.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> credentials.setApplication(null));
        });
        
        buttonOk.setDefaultButton(true);
        ButtonBar.setButtonData(buttonOk, ButtonBar.ButtonData.APPLY); 
        buttonOk.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> credentials.setApplication(currentAppBox.getValue()));
        });
        
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(buttonCancel, buttonOk);
        buttonBar.setPadding(new Insets(10));
        setBottom(buttonBar);
        
        setPrefSize(800, 600);
        getStylesheets().add(CloudLinkFX.class.getResource("cloudlink.css").toExternalForm());
    }
    
    private static final Logger LOGGER = Logger.getLogger(CloudLinkFX.class.getName());
    private static final String DEFAULT_HOST = "https://cloud.gluonhq.com";
    private static String cloudLinkHost = DEFAULT_HOST;

    private Optional<List<Application>> loadApplications(String email, String password) {
        HttpURLConnection openConnection = null;
        try {
            URL url = new URL(cloudLinkHost + "/3/account/dashboard/applications");
            openConnection = (HttpURLConnection) url.openConnection();
            openConnection.addRequestProperty("Authorization", 
                    "Basic " + Base64.getEncoder().encodeToString((email + ":" + password).getBytes()));
            
            int status = openConnection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    JsonReader createReader = Json.createReader(new InputStreamReader(openConnection.getInputStream()));
                    JsonArray readArray = createReader.readArray();

                    List<Application> list = new ArrayList<>();
                    readArray.iterator().forEachRemaining(value -> {
                        JsonObject obj = (JsonObject) value;
                        Application app = new Application();
                        app.setName(obj.getString("name"));
                        app.setIdentifier(obj.getString("identifier"));
                        app.setSecret(obj.getString("secret"));
                        list.add(app);
                    });
                    return Optional.of(list);
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
        
        return Optional.empty();
    }
    
    public Credentials getCredentials() {
        return credentials;
    }
}
