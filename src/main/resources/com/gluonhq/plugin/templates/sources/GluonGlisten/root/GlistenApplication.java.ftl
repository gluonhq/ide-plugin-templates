package ${packageName};

import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
<#if desktopEnabled>import javafx.scene.image.Image;</#if>
import javafx.stage.Stage;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class ${mainClassName} extends Application {

    private final AppManager appManager = AppManager.initialize(this::postInit);

    @Override
    public void init() {
        appManager.addViewFactory(HOME_VIEW, BasicView::new);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        appManager.start(primaryStage);
    }

    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        <#if desktopEnabled>
        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));</#if>
    }

    public static void main(String args[]) {
        launch(args);
    }
}
