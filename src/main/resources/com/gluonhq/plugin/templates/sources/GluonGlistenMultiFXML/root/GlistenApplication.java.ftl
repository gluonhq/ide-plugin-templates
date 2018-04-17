package ${packageName};

import ${packageName}.views.${primaryViewName}View;
import ${packageName}.views.${secondaryViewName}View;
import com.gluonhq.charm.glisten.application.MobileApplication;
<#if afterburnerEnabled>
import com.gluonhq.charm.glisten.mvc.View;
</#if>
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
        <#if afterburnerEnabled>
        addViewFactory(${primaryViewName?upper_case}_VIEW, () -> (View) new ${primaryViewName}View().getView());
        addViewFactory(${secondaryViewName?upper_case}_VIEW, () -> (View) new ${secondaryViewName}View().getView());
        <#else>
        addViewFactory(${primaryViewName?upper_case}_VIEW, () -> new ${primaryViewName}View().getView());
        addViewFactory(${secondaryViewName?upper_case}_VIEW, () -> new ${secondaryViewName}View().getView());
        </#if>

        DrawerManager.buildDrawer(this);
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        <#if cssProjectEnabled>scene.getStylesheets().add(${mainClassName}.class.getResource("style.css").toExternalForm());</#if>
        <#if desktopEnabled>
        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));</#if>
    }
}
