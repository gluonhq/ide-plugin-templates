package com.gluonhq.plugin.cloudlink;

import com.gluonhq.plugin.dialogs.DialogUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javax.json.JsonObject;
import javax.swing.SwingUtilities;

public class ApplicationsFX extends BorderPane {

    private static final Logger LOGGER = Logger.getLogger(ApplicationsFX.class.getName());
    
    @FXML
    private TextField keyText;

    @FXML
    private TextField secretText;

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
    private final boolean allowDisableApply;
    private CompletableFuture<Void> futureTask;
    
    public ApplicationsFX(String userKey, boolean keepLogged, boolean allowDisableApply) {
        this.allowDisableApply = allowDisableApply;
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
        currentAppBox.prefWidthProperty().bind(keyText.widthProperty());
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
            applyButton.setDisable(currentAppBox.getValue() == null || (allowDisableApply && currentAppBox.getValue().equals(existingApp)));
        });
        
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); 
        ButtonBar.setButtonData(applyButton, ButtonBar.ButtonData.APPLY); 
        ButtonBar.setButtonData(logoutButton, ButtonBar.ButtonData.HELP); 
        
        cancelButton.setOnAction(e -> {
            if (futureTask != null && ! futureTask.isDone()) {
                futureTask.cancel(true);
            }
            SwingUtilities.invokeLater(() -> credentials.setApplication(null));
        });
        applyButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> credentials.setApplication(currentAppBox.getValue())));
        logoutButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> credentials.setUserKey(false, null)));
        
        logoutButton.setVisible(credentials.isKeepLogged());
    }
    
    public Credentials getCredentials() {
        return credentials;
    }
    
    public void loadApplications(String jsonConfig) {
        disableDialog();
        
        futureTask = DialogUtils.supplyAsync(new ApplicationsTask(credentials.getUserKey()))
                .exceptionally(ex -> {
                    LOGGER.log(Level.SEVERE, "Error retrieving applications", ex);
                    restoreDialog(null);
                    return null;
                })
                .thenAccept(applicationsList -> {
                    if (applicationsList != null) {
                        if (jsonConfig != null && !jsonConfig.isEmpty()) {
                            JsonObject object = Json.createReader(new StringReader(jsonConfig)).readObject()
                                    .getJsonObject("gluonCredentials");
                            if (object != null) {
                                existingApp = applicationsList.stream()
                                        .filter(app -> app.getIdentifier().equals(object.getString("applicationKey")) &&
                                                app.getSecret().equals(object.getString("applicationSecret")))
                                        .findFirst()
                                        .orElse(null);
                            }
                        }
                    }
                    restoreDialog(applicationsList);
                });
    }
    
    private void disableDialog() {
        existingApp = null;
        disableControls(true);
        setCursor(Cursor.WAIT);
    }

    private void restoreDialog(List<Application> applicationsList) {
        Platform.runLater(() -> {
            disableControls(false);
            setCursor(Cursor.DEFAULT);
            if (applicationsList != null) {
                currentAppBox.getItems().setAll(applicationsList);
                currentAppBox.setValue(existingApp);
            } else {
                currentAppBox.getItems().clear();
            }
        });
    }
    
    private void disableControls(boolean disable) {
        currentAppBox.setDisable(disable);
        keyText.setDisable(disable);
        secretText.setDisable(disable);
        logoutButton.setDisable(disable);
        applyButton.setDisable(disable);
    }
    
}
