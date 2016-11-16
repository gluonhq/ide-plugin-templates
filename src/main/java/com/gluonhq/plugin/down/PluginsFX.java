package com.gluonhq.plugin.down;

import impl.org.controlsfx.skin.ListSelectionViewSkin;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javax.swing.SwingUtilities;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.glyphfont.FontAwesome;

public class PluginsFX extends BorderPane {

    private final static String GLUON_PLUGIN_URL = "http://docs.gluonhq.com/charm/javadoc/latest/com/gluonhq/charm/down/plugins/";
    private final static String GLUON_DOWN_URL = "http://docs.gluonhq.com/charm/latest/#_charm_down";

    private final ListSelectionView<Plugin> list;
    private final Button buttonCancel;
    private final Button buttonOk;
    
    private PluginsBean pluginsBean;
    private static final BooleanProperty SCENE_READY = new SimpleBooleanProperty();
    
    static {
        Font.loadFont(PluginsFX.class.getResource("fontawesome-webfont.ttf").toExternalForm(), 14);
    }

    public PluginsFX() {
        SCENE_READY.set(false);
        final Label label = new Label("Select the required plugins");
        label.getStyleClass().add("title");
        setTop(label);
        label.setMaxWidth(Double.MAX_VALUE);
        
        list = new ListSelectionView<>();
        list.setCellFactory(p -> new ListCell<Plugin>() {
            private Plugin item;
            private final VBox vBox; 
            private final Label name;
            private final Hyperlink help;
            private final Label description;
            {
                name = new Label();
                name.getStyleClass().add("name");
                help = new Hyperlink("More...");
                help.setOnAction(e -> openURL(GLUON_PLUGIN_URL + item.getUrl()));
                description = new Label();
                description.getStyleClass().add("description");
                HBox hBox = new HBox(5, description, help);
                hBox.setAlignment(Pos.CENTER_LEFT);
                vBox = new VBox(5, name, hBox);
                vBox.setPadding(new Insets(5));
                setGraphic(vBox);
                setText(null);
            }

            @Override
            protected void updateItem(Plugin item, boolean empty) {
                super.updateItem(item, empty);
                this.item = item;
                if (item != null && !empty) {
                    name.setText(item.getName());
                    description.setText(item.getDescription()); 
                    help.setVisible(true);
                } else {
                    name.setText("");
                    description.setText("");
                    help.setVisible(false);
                }
            }
            
        });
        
        setCenter(list);
        
        Button buttonHelp = new Button("Help");
        buttonHelp.getStyleClass().add("buttons");
        buttonHelp.setOnAction(e -> openURL(GLUON_DOWN_URL));

        buttonCancel = new Button("Cancel");
        buttonCancel.setCancelButton(true);
        ButtonBar.setButtonData(buttonCancel, ButtonData.CANCEL_CLOSE); 
        buttonCancel.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> pluginsBean.setPlugins(null));
        });
        buttonOk = new Button("Ok");
        buttonOk.setDefaultButton(true);
        ButtonBar.setButtonData(buttonOk, ButtonData.OK_DONE); 
 
        buttonOk.setDisable(true);
        buttonOk.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> pluginsBean.setPlugins(list.getTargetItems()));
        });
        
        ButtonBar buttonBar = new ButtonBar();
        Pane pane = new Pane();
        HBox.setHgrow(pane, Priority.ALWAYS);
        buttonBar.getButtons().addAll(buttonHelp, pane, buttonCancel, buttonOk);
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
                        setButtonGraphic(".move-to-target-button",     FontAwesome.Glyph.ANGLE_RIGHT.getChar()); 
                        setButtonGraphic(".move-to-target-all-button", FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT.getChar());
                        setButtonGraphic(".move-to-source-button",     FontAwesome.Glyph.ANGLE_LEFT.getChar());   
                        setButtonGraphic(".move-to-source-all-button", FontAwesome.Glyph.ANGLE_DOUBLE_LEFT.getChar());  

                        // This solves blurry jfxpanel on Mac:
                        if (list.getSkin() instanceof ListSelectionViewSkin) {
                            ListView source = ((ListSelectionViewSkin) list.getSkin()).getSourceListView();
                            source.getSelectionModel().selectFirst();
                            source.requestFocus();
                        }
                        
                        // sort lists after an item is added or removed
                        list.getSourceItems().addListener((ListChangeListener.Change<? extends Plugin> c) -> {
                            while (c.next()) {
                                if (c.wasAdded() || c.wasRemoved()) {
                                    list.getSourceItems().sort((p1, p2) -> p1.compareTo(p2));
                                }
                            }
                        });
                        list.getTargetItems().addListener((ListChangeListener.Change<? extends Plugin> c) -> {
                            while (c.next()) {
                                if (c.wasAdded() || c.wasRemoved()) {
                                    list.getTargetItems().sort((p1, p2) -> p1.compareTo(p2));
                                }
                            }
                        });

                        SCENE_READY.set(true);
                    });
                });
            }
        });
    }

    private void setButtonGraphic(String buttonStyleClass, char graphic) {
        Button button = (Button) lookup(buttonStyleClass);
        if (button != null) {
            button.getStyleClass().add("buttons");
            button.setGraphic(new Label(String.valueOf(graphic)));
        }
    }
    
    public PluginsBean loadBuildLines(List<String> lines) {
        this.pluginsBean = new PluginsBean(lines);
        
        List<Plugin> target = pluginsBean.getPlugins();
        List<Plugin> source = Stream.of(Plugin.values())
                .filter(p -> !target.contains(p))
                .collect(Collectors.toList());
        list.setTargetItems(FXCollections.observableArrayList(target));
        list.setSourceItems(FXCollections.observableArrayList(source));
        list.getTargetItems().addListener((Observable o) -> buttonOk.setDisable(false));
        
        return pluginsBean;
    }
    
    private void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    showError("Error launching browser: " + ex);
                }
            }
        }
    } 
    
    /**
     * Shows an error in a non-blocking dialog
     * @param message The message to display
     */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        if (SCENE_READY.get()) {
            alert.show();
        } else {
            SCENE_READY.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    SCENE_READY.removeListener(this);
                    // Delay it long enough until the JFXPanel shows the PluginsFX node
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> alert.show());
                    pause.play();
                }
            });
        }
    }
}
