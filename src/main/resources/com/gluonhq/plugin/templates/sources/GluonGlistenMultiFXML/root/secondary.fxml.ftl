<?xml version="1.0" encoding="UTF-8"?>

<?import com.gluonhq.charm.glisten.mvc.View?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.VBox?>


<View fx:id="${secondaryCSSName}" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="600.0" prefWidth="350.0"<#if afterburnerEnabled><#elseif cssSecondaryViewEnabled> stylesheets="@${secondaryCSSName}.css"</#if> xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="${packageName}.views.${secondaryViewName}Presenter">
   <center>
      <VBox alignment="CENTER" prefHeight="200.0" prefWidth="100.0" BorderPane.alignment="CENTER">
         <children>
            <Label text="This is ${secondaryViewName}!" />
         </children>
      </VBox>
   </center>
</View>
