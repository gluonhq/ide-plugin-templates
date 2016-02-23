package ${packageName};

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
<#if desktopEnabled>import javafx.scene.image.Image;</#if>
import javafx.stage.Stage;

public class ${mainClassName} extends MobileApplication {

    public static final String BASIC_VIEW = HOME_VIEW;

    @Override
    public void init() {
        addViewFactory(BASIC_VIEW, () -> new BasicView(BASIC_VIEW));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.getRandom().assignTo(scene);
<#if desktopEnabled>

        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));</#if>
    }
}
