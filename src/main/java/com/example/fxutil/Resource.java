package com.example.fxutil;

import java.net.URL;
import java.util.logging.Logger;

public class Resource {

    private final String path;
    private final URL url;

    public Resource(String path) {
        this.path = path;
        this.url = getResourceURL(path);
    }

    public String getPath() {
        return path;
    }

    public URL getUrl() {
        return url;
    }

    private static URL getResourceURL(String path) {
        try {
            return Resource.class.getResource(path);
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return null;
        }
    }
}
