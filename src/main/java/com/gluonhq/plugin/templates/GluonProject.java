package com.gluonhq.plugin.templates;

import com.gluonhq.service.TemplateService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GluonProject {

    private static final Logger LOG = Logger.getLogger(GluonProject.class.getName());

    public static GluonProject DESKTOP_SINGLE = new GluonProject("GluonDesktop", "SingleViewProject", "Gluon Desktop - Create a Single View Project", GluonProjectTarget.IDE);
    public static GluonProject DESKTOP_MULTIVIEW = new GluonProject("GluonDesktopMulti", "MultiViewProject", "Gluon Desktop - Create a Multi View Project", GluonProjectTarget.IDE);
    public static GluonProject DESKTOP_MULTIVIEWFXML = new GluonProject("GluonDesktopMultiFXML", "MultiViewProjectFXML", "Gluon Desktop - Create a Multi View Project with FXML", GluonProjectTarget.IDE);

    public static GluonProject MOBILE_SINGLE = new GluonProject("GluonGlisten",
            "Gluon Mobile - Single View Project",
            "Creates a Gluon Mobile based Java application with a single view targeted for Android, iOS, desktop and embedded devices. The project uses the Gluon Client plugin to create native images for the aforementioned platforms.",
            "https://download2.gluonhq.com/ideplugins/templates/GluonGlisten.zip",
            GluonProjectTarget.IDE);
    public static GluonProject MOBILE_MULTIVIEW = new GluonProject("GluonGlistenMulti",
            "Gluon Mobile - Multi View Project",
            "Creates a Gluon Mobile based Java application, with multiple views, targeted for Android, iOS, desktop and embedded devices. The project uses the Gluon Client plugin to create native images for the aforementioned platforms.",
            "https://download2.gluonhq.com/ideplugins/templates/GluonGlistenMulti.zip",
            GluonProjectTarget.IDE);
    public static GluonProject MOBILE_MULTIVIEWFXML = new GluonProject("GluonGlistenMultiFXML",
            "Gluon Mobile - Multi View Project with FXML",
            "Creates a Gluon Mobile based Java application, with multiple views designed with FXML, targeted for Android, iOS, desktop and embedded devices. The project uses the Gluon Client plugin to create native images for the aforementioned platforms.",
            "https://download2.gluonhq.com/ideplugins/templates/GluonGlistenMultiFXML.zip",
            GluonProjectTarget.IDE);
    public static GluonProject MOBILE_MULTIVIEW_GAF = new GluonProject("GluonGlistenMultiFXMLGAf",
            "Gluon Mobile - Multiple View Project with Glisten Afterburner",
            "Creates a new Gluon Mobile Afterburner application with multiple Views created with FXML, targeted for Android, iOS, desktop and embedded devices. The project uses the Gluon Client plugin to create native images for the aforementioned platforms.",
            "https://download2.gluonhq.com/ideplugins/templates/GluonGlistenMultiFXMLGAf.zip",
            GluonProjectTarget.IDE);

    public static GluonProject FUNCTION = new GluonProject("GluonFunction", "GluonFunction", "Gluon Function - Create a Gluon Function Project", GluonProjectTarget.IDE);

    public static GluonProject DASHBOARD_MOBILE_MULTIVIEWFXML = new GluonProject("DashboardMobileMultiFXML", "MultiViewProjectFXML", "Gluon Dashboard - Create a Multi View Project with FXML", GluonProjectTarget.DASHBOARD);

    private Path projectLocation;

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
     * Remote location of the project containing the templates
     */
    private final String location;

    /**
     * A list of supported targets.
     */
    private final EnumSet<GluonProjectTarget> targets;

    public GluonProject(String type, String name, String description, GluonProjectTarget target) {
        this(type, name, description, null, target);
    }

    public GluonProject(String type, String name, String description, String location, GluonProjectTarget target) {
        this(type, name, description, location, EnumSet.of(target));
    }

    public GluonProject(String type, String name, String description, String location, EnumSet<GluonProjectTarget> targets) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.location = location;
        this.targets = targets;
    }

    public final String getType() {
        return type;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public boolean isTargetSupported(GluonProjectTarget target) {
        return targets.contains(target);
    }

    public static List<GluonProject> values() {
        return Arrays.asList(
                MOBILE_SINGLE,
                MOBILE_MULTIVIEW,
                MOBILE_MULTIVIEWFXML,
                MOBILE_MULTIVIEW_GAF,
                FUNCTION,
                DASHBOARD_MOBILE_MULTIVIEWFXML
        );
    }

    public Path getProjectLocation() {
        if (location != null && projectLocation == null) {
            projectLocation = downloadAndUnzipTemplates();
        }
        return projectLocation;
    }

    public boolean isDownloaded() {
        return projectLocation != null;
    }

    private Path createProjectDirectory(String type) throws IOException {
        try {
            final Path dir = Paths.get(System.getProperty("java.io.tmpdir"), type);
            if (Files.exists(dir)) {
                deleteDir(dir);
            }
            return Files.createDirectory(dir);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Directory creation failed", e);
            throw e;
        }
    }

    private void deleteDir(Path dir) throws IOException {
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private Path downloadAndUnzipTemplates() {
        Path projectDirectory = null;
        try {
            final TemplateService templateService = new TemplateService();
            final Path path = templateService.downloadFile(location, getType());
            if (path != null) {
                projectDirectory = createProjectDirectory(type);
                unzip(path, projectDirectory);
            }
        } catch (IOException e) {
            projectDirectory = null;
        }
        return projectDirectory;
    }

    private void unzip(Path sourceZip, Path targetDir) throws IOException {
        Path root = targetDir.normalize();
        try (InputStream is = Files.newInputStream(sourceZip);
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry = zis.getNextEntry();
            Files.deleteIfExists(root);
            while (entry != null) {
                Path path = root.resolve(entry.getName()).normalize();
                if (!path.startsWith(root)) {
                    throw new IOException("Invalid ZIP");
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(path);
                } else {
                    try (OutputStream os = Files.newOutputStream(path)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to unzip file", e);
            throw e;
        }
    }
}
