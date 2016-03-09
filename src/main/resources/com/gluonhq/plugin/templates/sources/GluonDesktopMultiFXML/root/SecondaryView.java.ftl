package ${packageName}.views;

import com.gluonhq.particle.annotation.ParticleView;
import com.gluonhq.particle.view.FXMLView;
import ${packageName}.controllers.${secondaryViewName}Controller;

@ParticleView(name = "${secondaryCSSName}", isDefault = false)
public class ${secondaryViewName}View extends FXMLView {
    
    public ${secondaryViewName}View() {
        super(${secondaryViewName}View.class.getResource("${secondaryCSSName}.fxml"));
    }
    
    @Override
    public void start() {
        ((${secondaryViewName}Controller) getController()).postInit();
    }
    
    @Override
    public void stop() {
        ((${secondaryViewName}Controller) getController()).dispose();
    }
    
}