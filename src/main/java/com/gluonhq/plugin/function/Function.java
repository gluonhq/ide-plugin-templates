package com.gluonhq.plugin.function;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;

public class Function implements Serializable {
    
    public static final String FUNCTION_NAME_PROPERTY = "FUNCTION_NAME";
    public static final String PACKAGE_NAME_PROPERTY = "PACKAGE_NAME";
    
    private String functionName;
    private String packageName;

    public Function() {
        this(null, null);
    }
    
    public Function(String functionName, String packageName) {
        this.functionName = functionName;
        this.packageName = packageName;
    }

    public String getFunctionName() {
        return functionName;
    }

    void setFunctionName(String functionName) {
        String oldFunctionName = getFunctionName();
        this.functionName = functionName;
        pcs.firePropertyChange(FUNCTION_NAME_PROPERTY, oldFunctionName, functionName);
    }

    public String getPackageName() {
        return packageName;
    }

    void setPackageName(String packageName) {
        String oldPackageName = getPackageName();
        this.packageName = packageName;
        pcs.firePropertyChange(PACKAGE_NAME_PROPERTY, oldPackageName, packageName);
    }

    public String getMethodName() {
        return "call";
    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.functionName);
        hash = 23 * hash + Objects.hashCode(this.packageName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Function other = (Function) obj;
        if (!Objects.equals(this.functionName, other.functionName)) {
            return false;
        }
        return Objects.equals(this.packageName, other.packageName);
    }
    
}
