package com.gluonhq.plugin.down;

import impl.org.controlsfx.skin.ListSelectionViewSkin;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javax.swing.SwingUtilities;
import org.controlsfx.control.ListSelectionView;

public class PluginsFX extends BorderPane {

    private final ListSelectionView<Plugin> list;
    private final Button buttonCancel;
    private final Button buttonOk;
    
    private PluginsBean pluginsBean;
    
    static {
        Font.loadFont(PluginsFX.class.getResource("fontawesome-webfont.ttf").toExternalForm(), 14);
    }

    public PluginsFX() {
        final Label label = new Label("Select the required plugins");
        label.getStyleClass().add("title");
        setTop(label);
        label.setMaxWidth(Double.MAX_VALUE);
        
        list = new ListSelectionView<>();
        list.setCellFactory(p -> new ListCell<Plugin>() {
            private final VBox vBox; 
            private final Label name;
            private final Label description;
            {
                name = new Label();
                name.getStyleClass().add("name");
                description = new Label();
                description.getStyleClass().add("description");
                vBox = new VBox(5, name, description);
                vBox.setPadding( new Insets(5,5,5,5));
                setGraphic(vBox);
                setText(null);
            }

            @Override
            protected void updateItem(Plugin item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    name.setText(item.getName());
                    description.setText(item.getDescription()); 
                } else {
                    name.setText("");
                    description.setText("");
                }
            }
            
        });
        
        setCenter(list);
        
        buttonCancel = new Button("Cancel");
        buttonCancel.setCancelButton(true);
        ButtonBar.setButtonData(buttonCancel, ButtonData.CANCEL_CLOSE); 
        buttonCancel.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> pluginsBean.setPlugins(null));
        });
        buttonOk = new Button("Apply");
        buttonOk.setDefaultButton(true);
        ButtonBar.setButtonData(buttonOk, ButtonData.APPLY); 
 
        buttonOk.setDisable(true);
        buttonOk.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> pluginsBean.setPlugins(list.getTargetItems()));
        });
        
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(buttonCancel, buttonOk);
        buttonBar.setPadding(new Insets(10));
        setBottom(buttonBar);
        
        setPrefSize(800, 600);
        getStylesheets().add(PluginsFX.class.getResource("plugins.css").toExternalForm());
        
        /**
         * Hack to load the button icons in case it fails (IntelliJ)
         */
        sceneProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                nv.windowProperty().addListener((obs2, ov2, nv2) -> {
                    Platform.runLater(() -> {

                        setButtonGraphic(".move-to-target-button",     '\uf105');   // ANGLE_RIGHT
                        setButtonGraphic(".move-to-target-all-button", '\uf101' );  // ANGLE_DOUBLE_RIGHT
                        setButtonGraphic(".move-to-source-button",     '\uf104');   // ANGLE_LEFT
                        setButtonGraphic(".move-to-source-all-button", '\uf100' );  // ANGLE_DOUBLE_LEFT

                        // This solves blurry jfxpanel on Mac:
                        ListView source = ((ListSelectionViewSkin) list.getSkin()).getSourceListView();
                        source.getSelectionModel().selectFirst();
                        source.requestFocus();
                    });
                });
            }
        });
    }

    private void setButtonGraphic( String buttonStyleClass, char graphic ) {
        Button button = (Button) lookup("buttonStyleClass");
        if ( button != null ) {
            button.getStyleClass().add("buttons");
            button.setGraphic(new Label(String.valueOf(graphic)));
        }
    }
    
    public void loadPlugins(PluginsBean pluginsBean) {
        this.pluginsBean = pluginsBean;
        List<Plugin> target = pluginsBean.getPlugins();
        List<Plugin> source = Stream.of(Plugin.values())
                .filter(p -> !target.contains(p))
                .collect(Collectors.toList());
        list.setTargetItems(FXCollections.observableArrayList(target));
        list.setSourceItems(FXCollections.observableArrayList(source));
        list.getTargetItems().addListener((Observable o) -> {
            buttonOk.setDisable(false);
        });
    }
}
