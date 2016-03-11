package ${packageName};

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
<#if desktopEnabled>import javafx.scene.image.Image;</#if>
import javafx.stage.Stage;
<#if gluon_user_license_mobile?has_content>
import com.gluonhq.charm.glisten.license.License;

@License(key="${gluon_user_license_mobile?lower_case}")</#if>
public class ${mainClassName} extends MobileApplication {

    public static final String BASIC_VIEW = HOME_VIEW;

    @Override
    public void init() {
        addViewFactory(BASIC_VIEW, () -> new BasicView(BASIC_VIEW));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        <#if desktopEnabled>
        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));</#if>
    }
}
