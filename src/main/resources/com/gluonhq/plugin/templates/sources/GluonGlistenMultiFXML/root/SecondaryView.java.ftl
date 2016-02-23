package ${packageName}.views;

<#if afterburnerEnabled>
import com.airhacks.afterburner.views.FXMLView;
<#else>
import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
</#if>

public class ${secondaryViewName}View <#if afterburnerEnabled>extends FXMLView { 

}<#else>{

    private final String name;

    public ${secondaryViewName}View(String name) {
        this.name = name;
    }
    
    public View getView() {
        try {
            View view = FXMLLoader.load(${secondaryViewName}View.class.getResource("${secondaryCSSName}.fxml"));
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
</#if>