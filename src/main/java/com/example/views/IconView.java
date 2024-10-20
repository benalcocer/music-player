package com.example.views;

import com.example.fxutil.CustomStyleSheet;
import com.example.fxutil.Icon;
import com.example.fxutil.StyleSheetManager;

public class IconView extends ThemedImageView {
    private Icon icon;

    public IconView(Icon icon) {
        this(icon, 20, 20);
    }

    public IconView(Icon icon, double fitWidth, double fitHeight) {
        this.icon = icon;
        setFitWidth(fitWidth);
        setFitHeight(fitHeight);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        updateTheme(StyleSheetManager.getInstance().getTheme());
    }

    public Icon getIcon() {
        return icon;
    }

    @Override
    public void updateTheme(CustomStyleSheet currentTheme) {
        if (icon == null) {
            return;
        }
        switch (currentTheme) {
            case LIGHT_MODE -> {
                setImage(icon.getIconResource().getLightImage());
            }
            case DARK_MODE -> {
                setImage(icon.getIconResource().getDarkImage());
            }
        }
    }
}
