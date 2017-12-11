package com.gluonhq.plugin.cloudlink;

import com.gluonhq.plugin.dialogs.DialogUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingUtilities;

public class AccountFX extends BorderPane {
    
    private static final Logger LOGGER = Logger.getLogger(AccountFX.class.getName());
    
    @FXML
    private TextField userText;

    @FXML
    private PasswordField passText;

    @FXML
    private CheckBox rememberCheck;

    @FXML
    private Label errorLabel;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Hyperlink lostPassLink;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button cancelButton;
    
    private CompletableFuture<Void> futureTask;
    
    private final BooleanProperty checking = new SimpleBooleanProperty();

    private final Credentials credentials;

    public AccountFX() {
        this.credentials = new Credentials();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading account.fxml", ex);
        }
    }
    
    public void initialize() {
        errorLabel.setVisible(false);
        
        signUpLink.setOnAction(e -> browse("http://gluonhq.com/products/cloudlink/buy"));
        lostPassLink.setOnAction(e -> browse("https://gluonhq.com/my-account/lost-password/"));
        
        loginButton.disableProperty().bind(userText.textProperty().isEmpty()
                    .or(passText.textProperty().isEmpty())
                    .or(checking));
        loginButton.addEventFilter(ActionEvent.ACTION, e -> {
            e.consume();
            disableDialog();
            
            futureTask = DialogUtils.supplyAsync(new AccountTask(userText.getText(), passText.getText()))
                .exceptionally(ex -> {
                    LOGGER.log(Level.SEVERE, "Error retrieving account", ex);
                    restoreDialog(null);
                    return null;
                })
                .thenAccept(this::restoreDialog);
        });
        ButtonBar.setButtonData(loginButton, ButtonBar.ButtonData.OK_DONE); 
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); 
        
        cancelButton.setOnAction(e -> {
            if (futureTask != null && ! futureTask.isDone()) {
                futureTask.cancel(true);
            }
            SwingUtilities.invokeLater(() -> credentials.setUserKey(false, null));
        });
    }    
    
    public Credentials getCredentials() {
        return credentials;
    }
    
    private void disableDialog() {
        Platform.runLater(() -> {
            errorLabel.setVisible(false);
            checking.set(true);
            disableControls(true);
            setCursor(Cursor.WAIT);
        });
    }

    private void restoreDialog(String key) {
        if (key != null) {
            SwingUtilities.invokeLater(() -> credentials.setUserKey(rememberCheck.isSelected(), key));
        }
        Platform.runLater(() -> {
            disableControls(false);
            setCursor(Cursor.DEFAULT);
            checking.set(false);
            errorLabel.setVisible(key == null);
        });
    }
    
    private void disableControls(boolean disable) {
        userText.setDisable(disable);
        passText.setDisable(disable);
        rememberCheck.setDisable(disable);
    }
    
    private void browse(String uri) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)){
            try {
                desktop.browse(new URL(uri).toURI());
            } catch (MalformedURLException | URISyntaxException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
