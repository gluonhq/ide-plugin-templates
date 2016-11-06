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
        HBox hBox = new HBox(label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(10));
        setTop(hBox);
        
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
                HBox hBox = new HBox(description);
                hBox.setPadding(new Insets(0, 0, 0, 5));
                hBox.setAlignment(Pos.CENTER_LEFT);
                vBox = new VBox(5, name, hBox);
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
                        Button b1 = (Button) lookup(".move-to-target-button");
                        b1.getStyleClass().add("buttons");
                        b1.setGraphic(new Label(String.valueOf('\uf105'))); // ANGLE_RIGHT
                        
                        Button b2 = (Button) lookup(".move-to-target-all-button");
                        b2.getStyleClass().add("buttons");
                        b2.setGraphic(new Label(String.valueOf('\uf101'))); // ANGLE_DOUBLE_RIGHT
                        
                        Button b3 = (Button) lookup(".move-to-source-button");
                        b3.getStyleClass().add("buttons");
                        b3.setGraphic(new Label(String.valueOf('\uf104'))); // ANGLE_LEFT
                        
                        Button b4 = (Button) lookup(".move-to-source-all-button");
                        b4.getStyleClass().add("buttons");
                        b4.setGraphic(new Label(String.valueOf('\uf100'))); // ANGLE_DOUBLE_LEFT
                        
                        // This solves blurry jfxpanel on Mac:
                        ListView source = ((ListSelectionViewSkin) list.getSkin()).getSourceListView();
                        source.getSelectionModel().selectFirst();
                        source.requestFocus();
                    });
                });
            }
        });
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
