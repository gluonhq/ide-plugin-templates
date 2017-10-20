package com.gluonhq.plugin.templates;

import java.util.EnumSet;

public enum GluonProject {

    DESKTOP_SINGLE("GluonDesktop", "SingleViewProject", "Gluon Desktop - Create a Single View Project", GluonProjectTarget.IDE),
    DESKTOP_MULTIVIEW("GluonDesktopMulti", "MultiViewProject", "Gluon Desktop - Create a Multi View Project", GluonProjectTarget.IDE),
    DESKTOP_MULTIVIEWFXML("GluonDesktopMultiFXML", "MultiViewProjectFXML", "Gluon Desktop - Create a Multi View Project with FXML", GluonProjectTarget.IDE),

    MOBILE_SINGLE("GluonGlisten", "SingleViewProject", "Gluon Mobile - Create a Single View Project", GluonProjectTarget.IDE),
    MOBILE_MULTIVIEW("GluonGlistenMulti", "MultiViewProject", "Gluon Mobile - Create a Multi View Project", GluonProjectTarget.IDE),
    MOBILE_MULTIVIEWFXML("GluonGlistenMultiFXML", "MultiViewProjectFXML", "Gluon Mobile - Create a Multi View Project with FXML", GluonProjectTarget.IDE),
    MOBILE_MULTIVIEW_GAF("GluonGlistenMultiFXMLGAf", "MultiViewProjectFXMLGAf", "Gluon Mobile - Create a Glisten-Afterburner Project", GluonProjectTarget.IDE),

    FUNCTION("GluonFunction", "GluonFunction", "Gluon Function - Create a Gluon Function Project", GluonProjectTarget.IDE),

    DASHBOARD_MOBILE_MULTIVIEWFXML("DashboardMobileMultiFXML", "MultiViewProjectFXML", "Gluon Dashboard - Create a Multi View Project with FXML", GluonProjectTarget.DASHBOARD);
    
    /**
     * type of project. 
     * This has to be defined in the build.gradle script projects array
     */
    private final String type;
    
    /**
     * name used to define the wizard
     */
    private final String name;
    
    /**
     * Short description to show in the wizard
     */
    private final String description;

    /**
     * A list of supported targets.
     */
    private final EnumSet<GluonProjectTarget> targets;

    GluonProject(String type, String name, String description, GluonProjectTarget target) {
        this(type, name, description, EnumSet.of(target));
    }

    GluonProject(String type, String name, String description, EnumSet<GluonProjectTarget> targets) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.targets = targets;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }
    
    public final String getType() {
        return type;
    }

    public boolean isTargetSupported(GluonProjectTarget target) {
        return targets.contains(target);
    }
}
