package com.gluonhq.plugin.cloudlink;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Credentials {
    
    public static final String CREDENTIALS_PROPERTY = "CREDENTIALS";
    public static final String USERKEY_PROPERTY = "USER_KEY";
    
    private String userKey;
    private boolean keepLogged;
    private String credentials;
    private String ideKey;

    public Credentials() {
        this(null, false, null);
    }
    
    public Credentials(String userKey, boolean keepLogged, String credentials) {
        this.userKey = userKey;
        this.keepLogged = keepLogged;
        this.credentials = credentials;
    }
    
    void setUserKey(boolean keepLogged, String userKey) {
        this.keepLogged = keepLogged;
        pcs.firePropertyChange(USERKEY_PROPERTY, getUserKey(), userKey);
        this.userKey = userKey;
    }
    
    void setIdeKey(String ideKey) {
        this.ideKey = ideKey;
    }
    
    void setApplication(Application application) {
        String oldCredentials = credentials;
        if (application != null) {
            setIdeKey(application.getIdeKey());
            credentials = createCloudLinkConfigText(application);
        } else {
            credentials = null;
        }
        pcs.firePropertyChange(CREDENTIALS_PROPERTY, oldCredentials, credentials);
    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
    
    public String getUserKey() {
        return userKey;
    }

    public String getIdeKey() {
        return ideKey;
    }
    
    public boolean isKeepLogged() {
        return keepLogged;
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
