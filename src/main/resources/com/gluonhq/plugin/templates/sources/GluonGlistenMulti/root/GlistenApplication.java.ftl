package ${packageName};

import ${packageName}.views.${primaryViewName}View;
import ${packageName}.views.${secondaryViewName}View;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class ${mainClassName} extends Application {

    public static final String ${primaryViewName?upper_case}_VIEW = HOME_VIEW;
    public static final String ${secondaryViewName?upper_case}_VIEW = "${secondaryViewName} View";

    private final AppManager appManager = AppManager.initialize(this::postInit);

    @Override
    public void init() {
        appManager.addViewFactory(${primaryViewName?upper_case}_VIEW, ${primaryViewName}View::new);
        appManager.addViewFactory(${secondaryViewName?upper_case}_VIEW, ${secondaryViewName}View::new);
        
        DrawerManager.buildDrawer(appManager);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        appManager.start(primaryStage);
    }

    private void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        <#if cssProjectEnabled>
        scene.getStylesheets().add(${mainClassName}.class.getResource("style.css").toExternalForm());
        </#if>
        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));
    }

    public static void main(String args[]) {
        launch(args);
    }
}
