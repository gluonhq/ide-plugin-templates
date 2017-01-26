package com.gluonhq.plugin.templates.gluon;

import java.util.Locale;
import java.util.UUID;

public class ViewDefinition {

    private String identifier;
    private String name;
    private boolean createCss;

    public ViewDefinition() {
        this.identifier = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssName() {
        return name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
    }

    public boolean isCreateCss() {
        return createCss;
    }

    public void setCreateCss(boolean createCss) {
        this.createCss = createCss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewDefinition that = (ViewDefinition) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
