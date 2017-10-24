package com.gluonhq.plugin.code;

import static com.gluonhq.plugin.templates.ProjectConstants.DEFAULT_CLOUDLINK_HOST;
import com.gluonhq.plugin.templates.TemplateUtils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.swing.SwingUtilities;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class CodeFX extends BorderPane {

    private static final Logger LOGGER = Logger.getLogger(CodeFX.class.getName());
    
    @FXML
    private GridPane grid;

    @FXML
    private ChoiceBox<String> functionsBox;

    @FXML
    private TextField functionGivenNameText;

    @FXML
    private TextField resultTypeText;
    
    @FXML
    private MenuButton menuButton;

    @FXML
    private ChoiceBox<String> typeBox;

    @FXML
    private Label errorLabel;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;
    
    private Code code;
    
    public CodeFX() {
        code = new Code();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("code.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading code.fxml", ex);
        }
    }
    
    public void initialize() {
        ValidationSupport validationSupport = new ValidationSupport();

        validationSupport.registerValidator(functionGivenNameText, Validator.combine(
                Validator.createEmptyValidator("A valid function name is required"),
                Validator.<String>createPredicateValidator(s -> ! s.contains(" "), "The function name can't have spaces")));
        
        validationSupport.registerValidator(resultTypeText, Validator.combine(
                Validator.createEmptyValidator("A valid result type is required"),
                Validator.<String>createPredicateValidator(s -> ! s.contains(" "), "The result type is not valid")));
        
        
        functionsBox.valueProperty().addListener(o -> {
            final String remoteFunction = functionsBox.getValue();
            if (remoteFunction != null) {
                functionGivenNameText.setText(remoteFunction);
                resultTypeText.setText("String"); // TODO: ComboBox with primitives?
            } else {
                functionGivenNameText.clear();
                resultTypeText.clear();
            }
        });
        final ObservableList<String> list = FXCollections.observableArrayList("GluonObservableObject", "GluonObservableList");
        
        typeBox.setItems(list);
        typeBox.setValue(list.get(0));
        
        applyButton.disableProperty().bind(validationSupport.invalidProperty());
        functionsBox.prefWidthProperty().bind(functionGivenNameText.widthProperty());
        typeBox.prefWidthProperty().bind(functionGivenNameText.widthProperty());
        
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); 
        ButtonBar.setButtonData(applyButton, ButtonBar.ButtonData.APPLY); 
        
        functionGivenNameText.disableProperty().bind(functionsBox.valueProperty().isNull());
        resultTypeText.disableProperty().bind(functionGivenNameText.disableProperty());
        menuButton.disableProperty().bind(functionGivenNameText.disableProperty());
        typeBox.disableProperty().bind(functionGivenNameText.disableProperty());
        errorLabel.setVisible(false);
        
        menuButton.getItems().forEach(item -> item.setOnAction(e -> resultTypeText.setText(item.getText())));
        
        cancelButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> {
                code.setFunctionName(null);
                code.setFunctionGivenName(null);
                code.setResultType(null);
                code.setReturnedType(null);
            }));
        applyButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> {
                code.setFunctionName(functionsBox.getValue());
                code.setFunctionGivenName(functionGivenNameText.getText());
                code.setResultType(resultTypeText.getText());
                code.setReturnedType(typeBox.getValue());
            }));
    }
    
    public void loadRemoteFunctions(String applicationIdeKey) {
        setCursor(Cursor.WAIT);
        grid.setDisable(true); 
        buttonBar.setDisable(true);
        errorLabel.setVisible(false);
        new Thread(getRemoteFunctionsTask(applicationIdeKey))
                .start();
    }
    
    private Task<List<String>> getRemoteFunctionsTask(String applicationIdeKey) {
        return new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {
                HttpURLConnection openConnection = null;
                try {
                    if (applicationIdeKey != null) {
                        URL url = new URL(DEFAULT_CLOUDLINK_HOST + "/3/data/ide/functions");
                        openConnection = (HttpURLConnection) url.openConnection();
                        openConnection.addRequestProperty("Authorization", "GluonIde " + applicationIdeKey);
                        int status = openConnection.getResponseCode();
                        switch (status) {
                            case 200:
                            case 201:
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
                List<String> list = getValue();
                if (list != null) {
                    functionsBox.getItems().setAll(list);
                } else {
                    functionsBox.getItems().clear();
                }
                errorLabel.setVisible(list == null || list.isEmpty());
                grid.setDisable(false);
                buttonBar.setDisable(false);
                setCursor(Cursor.DEFAULT);
            }

            @Override
            protected void failed() {
                functionsBox.getItems().clear();
                errorLabel.setVisible(true);
                grid.setDisable(false);
                buttonBar.setDisable(false);
                setCursor(Cursor.DEFAULT);
            }
            
        };
    }

    public Code getCode() {
        return code;
    }
}
