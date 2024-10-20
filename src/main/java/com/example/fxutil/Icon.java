package com.example.fxutil;

public enum Icon {
    PLAY,
    PAUSE,
    VOLUME_MUTE,
    VOLUME,
    EYE_OPEN,
    EYE_CLOSED;

    private IconResource iconResource;

    static {
        PLAY.iconResource = new IconResource("play.svg");
        PAUSE.iconResource = new IconResource("pause.svg");
        VOLUME_MUTE.iconResource = new IconResource("volume_off.svg");
        VOLUME.iconResource = new IconResource("volume_up.svg");
        EYE_OPEN.iconResource = new IconResource("eye_open.svg");
        EYE_CLOSED.iconResource = new IconResource("eye_closed.svg");
    }

    public IconResource getIconResource() {
        return iconResource;
    }
}
