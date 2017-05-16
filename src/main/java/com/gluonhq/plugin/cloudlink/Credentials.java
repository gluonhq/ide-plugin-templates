package com.gluonhq.plugin.cloudlink;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Credentials {
    
    public static final String CREDENTIALS_PROPERTY = "CREDENTIALS";
    
    private String credentials;
    
    void setApplication(Application application) {
        String oldCredentials = credentials;
        credentials = createCloudLinkConfigText(application);
        pcs.firePropertyChange(CREDENTIALS_PROPERTY, oldCredentials, credentials);
    }
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
    
    public String getCredentials() {
        return credentials;
    }
    
    private String createCloudLinkConfigText(Application application) {
        if (application == null) {
            return null;
        }
        
        String cloudlinkConfigText = "{\n";
        cloudlinkConfigText += "  \"gluonCredentials\": {\n"
                + "    \"applicationKey\": \"" + application.getIdentifier() + "\",\n"
                + "    \"applicationSecret\": \"" + application.getSecret() + "\"\n"
                + "  }\n";

        cloudlinkConfigText += "}";

        return cloudlinkConfigText;
    }
}
