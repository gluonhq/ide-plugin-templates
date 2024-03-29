<?xml version="1.0" encoding="UTF-8"?>

<?import com.gluonhq.charm.glisten.control.Icon?>
<?import com.gluonhq.charm.glisten.mvc.View?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.VBox?>


<View fx:id="${primaryCSSName}" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="600.0" prefWidth="350.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="${packageName}.views.${primaryViewName}Presenter">
   <center>
      <VBox alignment="CENTER" prefHeight="200.0" prefWidth="100.0" spacing="15.0" BorderPane.alignment="CENTER">
         <children>
            <Label fx:id="label" text="%label.text.1" />
            <Button mnemonicParsing="false" onAction="#buttonClick" text="%button.text">
               <graphic>
                  <Icon content="LANGUAGE" />
               </graphic>
            </Button>
         </children>
      </VBox>
   </center>
</View>
