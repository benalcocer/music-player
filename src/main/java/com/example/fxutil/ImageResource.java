package com.example.fxutil;

import com.example.util.StringUtils;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public class ImageResource extends Resource {

    private Image image = null;

    private ImageResource(String path) {
        super(path);
    }

    public Image getImage() {
        return image;
    }

    public static ImageResource from(String path) {
        if (StringUtils.isSVGFileExtension(path)) {
            return fromSvgImage(path);
        } else {
            return fromImage(path);
        }
    }

    private static ImageResource fromImage(String path) {
        ImageResource imageResource = new ImageResource(path);
        if (imageResource.getUrl() != null) {
            try {
                imageResource.image = new Image(imageResource.getUrl().toExternalForm());
            } catch (Exception e) {
                Logger.getGlobal().severe(e.getMessage());
            }
        }
        return imageResource;
    }

    private static ImageResource fromSvgImage(String path) {
        ImageResource imageResource = new ImageResource(path);
        if (imageResource.getUrl() != null) {
            imageResource.image = ImageUtils.createSVGImage(imageResource.getUrl());
        }
        return imageResource;
    }
}
