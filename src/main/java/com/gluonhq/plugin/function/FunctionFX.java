package com.gluonhq.plugin.function;

import com.gluonhq.plugin.templates.TemplateUtils;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingUtilities;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class FunctionFX extends BorderPane {

    private static final Logger LOGGER = Logger.getLogger(FunctionFX.class.getName());
    
    @FXML
    private TextField functionText;

    @FXML
    private TextField packageText;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;

    private Function function;
    
    public FunctionFX() {
        function = new Function();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("function.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading function.fxml", ex);
        }
    }
    
    public void initialize() {
        ValidationSupport validationSupport = new ValidationSupport();

        validationSupport.registerValidator(functionText, Validator.combine(
                Validator.createEmptyValidator("A valid function name is required"),
                Validator.<String>createPredicateValidator(s -> ! s.contains(" "), "The function name can't have spaces")));
        
        validationSupport.registerValidator(packageText, Validator.combine(
                Validator.createEmptyValidator("A valid package name is required"),
                Validator.<String>createPredicateValidator(TemplateUtils::isValidPackageName, "The package name is not valid")));
        
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); 
        ButtonBar.setButtonData(applyButton, ButtonBar.ButtonData.APPLY); 
        
        cancelButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> {
                function.setFunctionName(null);
                function.setPackageName(null);
            }));
        applyButton.setOnAction(e -> 
            SwingUtilities.invokeLater(() -> {
                function.setFunctionName(TemplateUtils.upperCaseWord(functionText.getText()));
                function.setPackageName(packageText.getText().toLowerCase(Locale.ROOT));
            }));
        applyButton.disableProperty().bind(validationSupport.invalidProperty());
    }

    public Function getFunction() {
        return function;
    }
    
}
