package ${packageName}.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ${packageName}.${mainClassName};
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ${views[loop_index].name}Presenter {

    @FXML
    private View ${views[loop_index].cssName};

    @FXML
    private Label label;

    public void initialize() {
        ${views[loop_index].cssName}.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(${mainClassName}.MENU_LAYER)));
                appBar.setTitleText("${views[loop_index].name}");
                appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> 
                        System.out.println("Search")));
            }
        });
    }
    
    @FXML
    void buttonClick() {
        label.setText("Hello JavaFX Universe!");
    }
    
}
