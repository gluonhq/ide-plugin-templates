package ${packageName}.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ${packageName}.${mainClassName};
import javafx.fxml.FXML;

public class ${secondaryViewName}Presenter {

    @FXML
    private View ${secondaryCSSName};

    public void initialize() {
        ${secondaryCSSName}.setShowTransitionFactory(BounceInRightTransition::new);
        
        ${secondaryCSSName}.getLayers().add(new FloatingActionButton(MaterialDesignIcon.INFO.text, 
            e -> System.out.println("Info")));
        
        ${secondaryCSSName}.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(${mainClassName}.MENU_LAYER)));
                appBar.setTitleText("${secondaryViewName}");
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> 
                        System.out.println("Favorite")));
            }
        });
    }
}
