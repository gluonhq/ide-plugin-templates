package ${packageName}.views;

<#if afterburnerEnabled>
import com.airhacks.afterburner.views.FXMLView;
<#else>
import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
</#if>

public class ${primaryViewName}View <#if afterburnerEnabled>extends FXMLView { 

}<#else>{

    public View getView() {
        try {
            View view = FXMLLoader.load(${primaryViewName}View.class.getResource("${primaryCSSName}.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
</#if>
