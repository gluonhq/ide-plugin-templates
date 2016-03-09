package ${packageName};

import com.gluonhq.particle.application.ParticleApplication;
import javafx.scene.Scene;
import static org.controlsfx.control.action.ActionMap.actions;
<#if gluon_user_license_desktop?has_content>
import com.gluonhq.particle.annotation.License;

@License(key="${gluon_user_license_desktop ?lower_case}")</#if>
public class ${mainClassName} extends ParticleApplication {

    public ${mainClassName}() {
        super("Gluon Desktop Application");
    }

    @Override
    public void postInit(Scene scene) {
        scene.getStylesheets().add(${mainClassName}.class.getResource("style.css").toExternalForm());

        setTitle("Gluon Desktop Application");

        getParticle().buildMenu("File -> [signin,---, exit]", "Help -> [about]");
        
        getParticle().getToolBarActions().addAll(actions("signin"));
    }
}