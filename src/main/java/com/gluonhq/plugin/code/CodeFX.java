package com.gluonhq.plugin.code;

import com.gluonhq.plugin.dialogs.DialogUtils;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingUtilities;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class CodeFX extends BorderPane {

    private static final Logger LOGGER = Logger.getLogger(CodeFX.class.getName());
    
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
    private Button cancelButton;

    @FXML
    private Button applyButton;
    
    private Code code;
    
    private CompletableFuture<Void> futureTask;
    
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
                resultTypeText.setText("String");
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
        
        functionGivenNameText.disableProperty().bind(functionsBox.valueProperty().isNull().or(functionsBox.disableProperty()));
        resultTypeText.disableProperty().bind(functionGivenNameText.disableProperty());
        menuButton.disableProperty().bind(functionGivenNameText.disableProperty());
        typeBox.disableProperty().bind(functionGivenNameText.disableProperty());
        errorLabel.setVisible(false);
        
        menuButton.getItems().forEach(item -> item.setOnAction(e -> resultTypeText.setText(item.getText())));
        
        cancelButton.setOnAction(e -> {
            if (futureTask != null && ! futureTask.isDone()) {
                futureTask.cancel(true);
            }
            SwingUtilities.invokeLater(() -> {
                code.setFunctionName(null);
                code.setFunctionGivenName(null);
                code.setResultType(null);
                code.setReturnedType(null);
            });
        });
        applyButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> {
                code.setFunctionName(functionsBox.getValue());
                code.setFunctionGivenName(functionGivenNameText.getText());
                code.setResultType(resultTypeText.getText());
                code.setReturnedType(typeBox.getValue());
            }));
    }
    
    public Code getCode() {
        return code;
    }
    
    public void loadRemoteFunctions(String applicationIdeKey) {
        disableDialog();
        
        futureTask = DialogUtils.supplyAsync(new CodeTask(applicationIdeKey))
                .exceptionally(ex -> {
                    LOGGER.log(Level.SEVERE, "Error retrieving functions", ex);
                    restoreDialog(null);
                    return null;
                })
                .thenAccept(this::restoreDialog);
    }
    
    private void disableDialog() {
        errorLabel.setVisible(false);
        functionsBox.getItems().clear();
        disableControls(true);
        setCursor(Cursor.WAIT);
    }

    private void restoreDialog(List<String> functionsList) {
        Platform.runLater(() -> {
            if (functionsList != null && ! functionsList.isEmpty()) {
                functionsBox.getItems().setAll(functionsList);
            }
            disableControls(false);
            setCursor(Cursor.DEFAULT);
            errorLabel.setVisible(functionsList == null || functionsList.isEmpty());
        });
    }
    
    private void disableControls(boolean disable) {
        functionsBox.setDisable(disable);
    }
    
}
