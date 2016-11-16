package com.gluonhq.plugin.down;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PluginsBean {
    
    public static final String PLUGINS_PROPERTY = "PLUGINS";
    
    private List<Plugin> plugins;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    private int lineNumber;
    private int linesToReplace;
    private final List<String> lines;

    public PluginsBean(List<String> lines) {
        this.lines = lines;
        
        if (lines != null && !lines.isEmpty()) {
            lineNumber = IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).contains("plugins"))
                .findAny().orElse(-1);
            if (lineNumber > -1) {
                int index = lineNumber;
                String pluginLine = lines.get(index++).trim();
                String next = pluginLine;
                while (next.endsWith(",") || next.endsWith("\\")) {
                    next = lines.get(index++).trim();
                    pluginLine = pluginLine.replaceAll("\\\\", "").trim()
                            .concat(" ")
                            .concat(next.replaceAll("\\\\", "").trim());
                }
                linesToReplace = index - lineNumber;
                loadPlugins(pluginLine);
            } else {
                PluginsFX.showError("Error: plugins not found");
                // no plugins line. Add it before downConfig end bracket
                int downConfigLine = IntStream.range(0, lines.size())
                    .filter(i -> lines.get(i).contains("downConfig"))
                    .findFirst().orElse(-1);
                if (downConfigLine > 1) {
                    lineNumber = IntStream.range(downConfigLine, lines.size())
                        .filter(i -> lines.get(i).contains("}"))
                        .findAny().orElse(-1);
                } else {
                    PluginsFX.showError("Error: downConfig not found");
                }
                loadPlugins(null);
            }
        } else {
            // no build.gradle file
            PluginsFX.showError("Error: Build.gradle can't be read");
            loadPlugins(null);
        }
    }
        
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Plugin> plugins) {
        List<Plugin> oldPlugins = this.plugins;
        this.plugins = plugins;
        pcs.firePropertyChange(PLUGINS_PROPERTY, oldPlugins, plugins);
    }
    
    private void loadPlugins(String pluginsLine) {
        if (pluginsLine == null || pluginsLine.isEmpty()) {
            setPlugins(new ArrayList<>());
            return;
        }
        setPlugins(Stream.of(pluginsLine.split("\\,"))
                    .filter(s -> s.contains("'"))
                    .map(s -> s.substring(s.indexOf("'") + 1, s.lastIndexOf("'")))
                    .map(s -> Plugin.byName(s).orElse(null))
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList()));
    }
    
    /**
     * Return the build file in a list of lines, containing the changes with the 
     * selected files
     * 
     * @return a List of String with the new build.gradle, including the selected plugins
     */
    public List<String> savePlugins() {
        if (lineNumber == -1 || lines == null || lines.isEmpty()) {
            return lines;
        }
        
        // add missing dependencies
        List<Plugin> dependencies = plugins
                                    .stream()
                                    .map(Plugin::getDependencies)
                                    .flatMap(List::stream)
                                    .distinct()
                                    .map(s -> Plugin.byName(s).orElse(null))
                                    .filter(Objects::nonNull)
                                    .filter(p -> !plugins.contains(p))
                                    .collect(Collectors.toList());
        
        List<Plugin> finalPlugins = Stream.concat(plugins.stream(), dependencies.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        String pluginsLine = "        plugins " + 
                            finalPlugins
                                .stream()
                                .map(p -> "'" + p.getName() + "'")
                                .sorted()
                                .collect(Collectors.joining(", "));
        
        List<String> list = IntStream.range(0, lineNumber)
                .mapToObj(lines::get)
                .collect(Collectors.toList());
        list.add(pluginsLine);
        list.addAll(IntStream.range(lineNumber + linesToReplace, lines.size())
                .mapToObj(lines::get)
                .collect(Collectors.toList()));
        return list;
    }
    
}
