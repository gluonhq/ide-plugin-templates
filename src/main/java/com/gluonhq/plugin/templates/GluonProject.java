package com.gluonhq.plugin.templates;

public enum GluonProject {
    
    DESKTOP("GluonDesktop", "SingleViewProject", "Gluon Desktop - Create a Single View Project"),

    SINGLE("GluonGlisten", "SingleViewProject", "Gluon Mobile - Create a Single View Project"),
    MULTIVIEW("GluonGlistenMulti", "MultiViewProject", "Gluon Mobile - Create a Multi View Project"),
    MULTIVIEWFXML("GluonGlistenMultiFXML", "MultiViewProjectFXML", "Gluon Mobile - Create a Multi View Project with FXML");

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
