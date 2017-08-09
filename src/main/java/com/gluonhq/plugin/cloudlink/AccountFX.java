package com.gluonhq.plugin.cloudlink;

import static com.gluonhq.plugin.templates.ProjectConstants.DEFAULT_CLOUDLINK_HOST;
import java.awt.Desktop;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
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
            errorLabel.setVisible(false);
            checking.set(true);
            setCursor(Cursor.WAIT);
            new Thread(getLoginTask()).start();
        });
        ButtonBar.setButtonData(loginButton, ButtonBar.ButtonData.OK_DONE); 
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); 
        
        cancelButton.setOnAction(e -> SwingUtilities.invokeLater(() -> credentials.setUserKey(false, null)));
    }    
    
    public Credentials getCredentials() {
        return credentials;
    }
    
    private Task<String> getLoginTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                HttpURLConnection openConnection = null;
                String key = Base64.getEncoder().encodeToString((userText.getText() + ":" + passText.getText()).getBytes());
                try {
                    URL url = new URL(DEFAULT_CLOUDLINK_HOST + "/3/account/dashboard/applications");
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
                return null;
            }

            @Override
            protected void succeeded() {
                checking.set(false);
                setCursor(Cursor.DEFAULT);
                final String value = getValue();
                if (value != null) {
                    SwingUtilities.invokeLater(() -> credentials.setUserKey(rememberCheck.isSelected(), value));
                } else {
                    errorLabel.setVisible(true);
                }
            }

            @Override
            protected void failed() {
                checking.set(false);
                setCursor(Cursor.DEFAULT);
                errorLabel.setVisible(true);
            }
        };
    
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
