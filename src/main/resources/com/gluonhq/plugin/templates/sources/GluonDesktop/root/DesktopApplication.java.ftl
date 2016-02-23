package ${packageName};

import com.gluonhq.particle.application.ParticleApplication;
import javafx.scene.Scene;
import static org.controlsfx.control.action.ActionMap.actions;

public class ${mainClassName} extends ParticleApplication {

    public ${mainClassName}() {
        super("Basic Particle");
    }

    @Override
    public void postInit(Scene scene) {
        scene.getStylesheets().add(${mainClassName}.class.getResource("style.css").toExternalForm());

        setTitle( "Basic Particle" );

        getParticle().buildMenu("File -> [---, exit]");
        
        getParticle().getToolBarActions().addAll(actions("exit"));
    }
}