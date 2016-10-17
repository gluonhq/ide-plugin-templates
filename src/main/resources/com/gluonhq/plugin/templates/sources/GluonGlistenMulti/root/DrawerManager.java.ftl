package ${packageName};

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import static ${packageName}.${mainClassName}.MENU_LAYER;
import static ${packageName}.${mainClassName}.${primaryViewName?upper_case}_VIEW;
import static ${packageName}.${mainClassName}.${secondaryViewName?upper_case}_VIEW;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.image.Image;

public class DrawerManager {

    private final NavigationDrawer drawer;

    private final Map<String, Item> mapViews;
    
    private static boolean updating = false;
    
    public DrawerManager() {
        this.drawer = new NavigationDrawer();
        this.mapViews = new HashMap<>();
        
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);
        
        final Item primaryItem = new Item("${primaryViewName}", MaterialDesignIcon.HOME.graphic());
        primaryItem.selectedProperty().addListener((obs, ov, nv) -> {
            if (!updating && nv) {
                MobileApplication.getInstance().switchView(${primaryViewName?upper_case}_VIEW, ViewStackPolicy.SKIP);
            }
        });
        this.mapViews.put(${primaryViewName?upper_case}_VIEW, primaryItem);
        
        final Item secondaryItem = new Item("${secondaryViewName}", MaterialDesignIcon.DASHBOARD.graphic());
        secondaryItem.selectedProperty().addListener((obs, ov, nv) -> {
            if (!updating && nv) {
                MobileApplication.getInstance().switchView(${secondaryViewName?upper_case}_VIEW);
            }
        });
        this.mapViews.put(${secondaryViewName?upper_case}_VIEW, secondaryItem);

        final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
        quitItem.selectedProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
            }
        });
        
        drawer.getItems().addAll(primaryItem, secondaryItem);
        if (!Platform.isIOS()) {
            drawer.getItems().add(quitItem);
        }
        
        drawer.addEventHandler(NavigationDrawer.ITEM_SELECTED, 
                e -> MobileApplication.getInstance().hideLayer(MENU_LAYER));
        
        MobileApplication.getInstance().viewProperty().addListener((obs, oldView, newView) -> {
            Optional.ofNullable(oldView)
                    .flatMap(v -> Optional.ofNullable(mapViews.get(v.getName())))
                    .ifPresent(item -> item.setSelected(false));
            Optional.ofNullable(mapViews.get(newView.getName()))
                    .ifPresent(this::updateItem);
        });
        updateItem(primaryItem);
    }
    
    private void updateItem(Item item) {
        updating = true;
        item.setSelected(true);
        updating = false;
        drawer.setSelectedItem(item);
    }
    
    public NavigationDrawer getDrawer() {
        return drawer;
    }
}
