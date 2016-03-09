package ${packageName}.views;

import com.gluonhq.particle.annotation.ParticleView;
import com.gluonhq.particle.view.FXMLView;
import ${packageName}.controllers.${primaryViewName}Controller;

@ParticleView(name = "${primaryCSSName}", isDefault = true)
public class ${primaryViewName}View extends FXMLView {
    
    public ${primaryViewName}View() {
        super(${primaryViewName}View.class.getResource("${primaryCSSName}.fxml"));
    }
    
    @Override
    public void start() {
        ((${primaryViewName}Controller) getController()).postInit();
    }
    
    @Override
    public void stop() {
        ((${primaryViewName}Controller) getController()).dispose();
    }
    
}
