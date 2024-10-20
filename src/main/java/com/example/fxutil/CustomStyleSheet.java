package com.example.fxutil;

import java.io.File;
import java.util.List;

public enum CustomStyleSheet {
    CSS_BASE,
    LIGHT_MODE,
    DARK_MODE;

    private static final List<CustomStyleSheet> themes = List.of(LIGHT_MODE, DARK_MODE);
    private Resource resource;
    private String stringValue;

    static {
        CSS_BASE.resource = new Resource("/css/cssBase.css");
        CSS_BASE.stringValue = "CSS Base";
        LIGHT_MODE.resource = new Resource("/css/lightMode.css");
        LIGHT_MODE.stringValue = "Light Mode";
        DARK_MODE.resource = new Resource("/css/darkMode.css");
        DARK_MODE.stringValue = "Dark Mode";
    }

    public String getStyleSheetLocation() {
        return resource.getUrl().toExternalForm();
    }

    public boolean exists() {
        try {
            if (resource.getUrl() != null) {
                File file = new File(resource.getUrl().getFile());
                return file.exists() && file.isFile();
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    public static List<CustomStyleSheet> getThemes() {
        return themes;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
