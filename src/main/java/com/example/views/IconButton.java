package com.example.views;

import com.example.fxutil.Icon;
import com.example.util.Cleaner;

public class IconButton extends CustomButton implements Cleaner {

    private final IconView iconView;

    public IconButton(Icon icon) {
        getStyleClass().add("icon-button");
        iconView = new IconView(icon, 24, 24);
        setGraphic(iconView);
    }

    public IconView getIconView() {
        return iconView;
    }

    @Override
    public void initialize() {
        iconView.initialize();
    }

    @Override
    public void cleanUp() {
        iconView.cleanUp();
    }
}
