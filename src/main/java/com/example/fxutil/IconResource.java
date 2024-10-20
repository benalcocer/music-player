package com.example.fxutil;

import com.example.util.StringUtils;
import javafx.scene.image.Image;

public class IconResource {

    private static final String PATH_PREFIX = "/icons/";
    private static final String DARK_SUFFIX = "_dark";

    private final ImageResource lightResource;
    private final ImageResource darkResource;

    public IconResource(String iconFileName) {
        String[] strings = StringUtils.splitFileNameExtension(iconFileName);
        String fileName = strings[0];
        String extension = strings.length == 2 ? strings[1] : "";
        lightResource = ImageResource.from(PATH_PREFIX + iconFileName);
        darkResource = ImageResource.from(PATH_PREFIX + fileName + DARK_SUFFIX + "." + extension);
    }

    public Image getLightImage() {
        return lightResource.getImage();
    }

    public Image getDarkImage() {
        return darkResource.getImage();
    }
}
