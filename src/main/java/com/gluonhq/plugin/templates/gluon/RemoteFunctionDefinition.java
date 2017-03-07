package com.gluonhq.plugin.templates.gluon;

import java.util.ArrayList;
import java.util.List;

public class RemoteFunctionDefinition {
    
    public final String name;

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public boolean isHasParameters() {
        return hasParameters;
    }
    public final List<String> parameters;
    public final boolean hasParameters;
    
    public RemoteFunctionDefinition(String name, List<String> params) {
        this.name = name;
        this.parameters = params == null? new ArrayList(0): params;
        
        this.hasParameters = this.parameters.size() > 0;
        System.out.println("created function named "+name+", hasparams = "+hasParameters+" and params "+parameters);
    }
    
}
