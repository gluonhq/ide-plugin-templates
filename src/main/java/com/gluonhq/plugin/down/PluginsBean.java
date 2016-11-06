package com.gluonhq.plugin.down;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PluginsBean {
    
    public static final String PLUGINS_PROPERTY = "PLUGINS";
    
    private List<Plugin> plugins;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * Parse the build.gradle line containing "plugins" to get 
     * the list of Plugins
     * 
     * @param pluginsLine, a String with the current list of plugins
     */
    public void loadPlugins(String pluginsLine) {
        if (pluginsLine == null || pluginsLine.isEmpty()) {
            setPlugins(new ArrayList<>());
            return;
        }
        setPlugins(Stream.of(pluginsLine.split("\\,"))
                    .map(s -> s.substring(s.indexOf("'") + 1, s.lastIndexOf("'")))
                    .map(s -> Plugin.byName(s).orElse(null))
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList()));
    }
    
    public void setPlugins(List<Plugin> plugins) {
        List<Plugin> oldPlugins = this.plugins;
        this.plugins = plugins;
        pcs.firePropertyChange(PLUGINS_PROPERTY, oldPlugins, plugins);
    }
    
    /**
     * Return the "plugins" line for the build.gradle 
     * 
     * @return a String with all the selected plugins
     */
    public String savePlugins() {
        String pluginsLine = "\tplugins " + 
                            plugins
                                .stream()
                                .map(p -> "'"+p.getName()+"'")
                                .sorted()
                                .collect(Collectors.joining(", "));
        return pluginsLine;
    }
    
}
