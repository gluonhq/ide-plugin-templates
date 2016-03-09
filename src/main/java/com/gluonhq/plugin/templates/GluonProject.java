package com.gluonhq.plugin.templates;

public enum GluonProject {
    
    DESKTOP_SINGLE("GluonDesktop", "SingleViewProject", "Gluon Desktop - Create a Single View Project"),
    DESKTOP_MULTIVIEW("GluonDesktopMulti", "MultiViewProject", "Gluon Desktop - Create a Multi View Project"),
    DESKTOP_MULTIVIEWFXML("GluonDesktopMultiFXML", "MultiViewProjectFXML", "Gluon Desktop - Create a Multi View Project with FXML"),

    MOBILE_SINGLE("GluonGlisten", "SingleViewProject", "Gluon Mobile - Create a Single View Project"),
    MOBILE_MULTIVIEW("GluonGlistenMulti", "MultiViewProject", "Gluon Mobile - Create a Multi View Project"),
    MOBILE_MULTIVIEWFXML("GluonGlistenMultiFXML", "MultiViewProjectFXML", "Gluon Mobile - Create a Multi View Project with FXML");

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

    private GluonProject(String type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
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
}
