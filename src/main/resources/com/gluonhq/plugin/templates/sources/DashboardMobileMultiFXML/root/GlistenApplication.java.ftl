package ${packageName};

<#list views as view>import ${packageName}.views.${view.name}View;
</#list>
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
<#if afterburnerEnabled>import com.gluonhq.charm.glisten.mvc.View;</#if>
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
<#if desktopEnabled>import javafx.stage.Stage;</#if>
<#if gluon_user_license_mobile?has_content>
import com.gluonhq.charm.glisten.license.License;

@License(key="${gluon_user_license_mobile?lower_case}")</#if>
public class ${mainClassName} extends MobileApplication {

    <#list views as view>public static final String ${view.name?upper_case}_VIEW = <#if view?index == 0>HOME_VIEW<#else>"${view.name} View"</#if>;
    </#list>
    public static final String MENU_LAYER = "Side Menu";

    @Override
    public void init() {
        <#if afterburnerEnabled><#list views as view>addViewFactory(${view.name?upper_case}_VIEW, () -> (View) new ${view.name}View().getView());
        </#list><#else><#list views as view>addViewFactory(${view.name?upper_case}_VIEW, () -> new ${view.name}View(${view.name?upper_case}_VIEW).getView());
        </#list></#if>
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(new DrawerManager().getDrawer()));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        <#if cssProjectEnabled>scene.getStylesheets().add(${mainClassName}.class.getResource("style.css").toExternalForm());</#if>
        <#if desktopEnabled>
        ((Stage) scene.getWindow()).getIcons().add(new Image(${mainClassName}.class.getResourceAsStream("/icon.png")));</#if>
    }
}
