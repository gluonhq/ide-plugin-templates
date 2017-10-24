package com.gluonhq.plugin.code;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Code {
    
    public static final String CODE_PROPERTY = "CODE_TYPE";
    
    private String functionName;
    private String functionGivenName;
    private String resultType;
    private String returnedType;

    public Code() {
        this(null, null, null, null);
    }
    
    public Code(String functionName, String functionGivenName, String resultType, String returnedType) {
        this.functionName = functionName;
        this.functionGivenName = functionGivenName;
        this.resultType = resultType;
        this.returnedType = returnedType;
    }
    
    void setFunctionName(String functionName) {
        String oldFunctionName = getFunctionName();
        this.functionName = functionName;
        pcs.firePropertyChange(CODE_PROPERTY, oldFunctionName, functionName);
    }
    
    void setFunctionGivenName(String functionGivenName) {
        String oldFunctionGivenName = getFunctionGivenName();
        this.functionGivenName = functionGivenName;
        pcs.firePropertyChange(CODE_PROPERTY, oldFunctionGivenName, functionGivenName);
    }
    
    void setResultType(String resultType) {
        String oldResultType = getResultType();
        this.resultType = resultType;
        pcs.firePropertyChange(CODE_PROPERTY, oldResultType, resultType);
    }
    
    void setReturnedType(String returnedType) {
        String oldReturnedType = getReturnedType();
        this.returnedType = returnedType;
        pcs.firePropertyChange(CODE_PROPERTY, oldReturnedType, returnedType);
    }
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
    
    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionGivenName() {
        return functionGivenName;
    }

    public String getResultType() {
        return resultType;
    }

    public String getReturnedType() {
        return returnedType;
    }
    
}
