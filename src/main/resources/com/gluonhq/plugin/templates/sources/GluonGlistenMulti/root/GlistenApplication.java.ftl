package ${packageName};

import ${packageName}.views.${primaryViewName}View;
import ${packageName}.views.${secondaryViewName}View;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
<#if desktopEnabled>import javafx.stage.Stage;</#if>
<#if gluon_user_license_mobile?has_content>
import com.gluonhq.charm.glisten.license.License;

@License(key="${gluon_user_license_mobile?lower_case}")</#if>
public class ${mainClassName} extends MobileApplication {

    public static final String ${primaryViewName?upper_case}_VIEW = HOME_VIEW;
    public static final String ${secondaryViewName?upper_case}_VIEW = "${secondaryViewName} View";
    
    @Override
    public void init() {
        addViewFactory(${primaryViewName?upper_case}_VIEW, ${primaryViewName}View::new);
        addViewFactory(${secondaryViewName?upper_case}_VIEW, ${secondaryViewName}View::new);
        
        DrawerManager.buildDrawer(this);
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        <#if cssProjectEnabled>
        scene.getStylesheets().add(${mainClassName}.class.getResource("style.css").toExternalForm());</#if>
        <#if desktopEnabled>
        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));</#if>
    }
}
