package ${packageName}.views;

import com.gluonhq.charm.glisten.afterburner.AppView;
import static com.gluonhq.charm.glisten.afterburner.AppView.Flag.HOME_VIEW;
import static com.gluonhq.charm.glisten.afterburner.AppView.Flag.SHOW_IN_DRAWER;
import static com.gluonhq.charm.glisten.afterburner.AppView.Flag.SKIP_VIEW_STACK;
import com.gluonhq.charm.glisten.afterburner.AppViewRegistry;
import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.afterburner.DefaultDrawerManager;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.image.Image;
import java.util.Locale;
import ${packageName}.${mainClassName};

public class AppViewManager {

    public static final AppViewRegistry REGISTRY = new AppViewRegistry();

    public static final AppView ${primaryViewName?upper_case}_VIEW = view("${primaryViewName}", ${primaryViewName}Presenter.class, MaterialDesignIcon.HOME, SHOW_IN_DRAWER, HOME_VIEW, SKIP_VIEW_STACK);
    public static final AppView ${secondaryViewName?upper_case}_VIEW = view("${secondaryViewName}", ${secondaryViewName}Presenter.class, MaterialDesignIcon.DASHBOARD, SHOW_IN_DRAWER);
    
    private static AppView view(String title, Class<? extends GluonPresenter<?>> presenterClass, MaterialDesignIcon menuIcon, AppView.Flag... flags ) {
        return REGISTRY.createView(name(presenterClass), title, presenterClass, menuIcon, flags);
    }

    private static String name(Class<? extends GluonPresenter<?>> presenterClass) {
        return presenterClass.getSimpleName().toUpperCase(Locale.ROOT).replace("PRESENTER", "");
    }
    
    public static void registerViewsAndDrawer(MobileApplication app) {
        for (AppView view : REGISTRY.getViews()) {
            view.registerView(app);
        }

        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(${mainClassName}.class.getResourceAsStream("/icon.png"))));

        DefaultDrawerManager drawerManager = new DefaultDrawerManager(app, header, REGISTRY.getViews()); 
        drawerManager.installDrawer();
    }
}
