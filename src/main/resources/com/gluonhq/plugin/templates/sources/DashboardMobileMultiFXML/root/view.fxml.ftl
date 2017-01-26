<?xml version="1.0" encoding="UTF-8"?>

<?import com.gluonhq.charm.glisten.control.Icon?>
<?import com.gluonhq.charm.glisten.mvc.View?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.VBox?>


<View fx:id="${views[loop_index].cssName}" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="600.0" prefWidth="350.0"<#if afterburnerEnabled><#elseif views[loop_index].createCss> stylesheets="@${views[loop_index].cssName}.css"</#if> xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="${packageName}.views.${views[loop_index].name}Presenter">
   <center>
      <VBox alignment="CENTER" prefHeight="200.0" prefWidth="100.0" spacing="15.0" BorderPane.alignment="CENTER">
         <children>
            <Label fx:id="label" text="Hello JavaFX World!" />
            <Button mnemonicParsing="false" onAction="#buttonClick" text="Change the World!">
               <graphic>
                  <Icon content="LANGUAGE" />
               </graphic>
            </Button>
         </children>
      </VBox>
   </center>
</View>
