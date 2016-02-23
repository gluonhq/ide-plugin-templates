package ${packageName}.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ${packageName}.${mainClassName};
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ${secondaryViewName}View extends View {

    public ${secondaryViewName}View(String name) {
        super(name);
        
        <#if cssSecondaryViewEnabled>
        getStylesheets().add(${secondaryViewName}View.class.getResource("${secondaryCSSName}.css").toExternalForm());</#if>
        
        Label label = new Label("This is ${secondaryViewName}!");

        VBox controls = new VBox(label);
        controls.setAlignment(Pos.CENTER);
        
        setCenter(controls);
        
        setShowTransitionFactory(BounceInRightTransition::new);
        
        getLayers().add(new FloatingActionButton(MaterialDesignIcon.INFO.text, 
            e -> System.out.println("Info")));
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(${mainClassName}.MENU_LAYER)));
        appBar.setTitleText("${secondaryViewName}");
        appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> System.out.println("Favorite")));
    }
    
}
