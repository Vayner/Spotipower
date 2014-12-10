package com.enderwolf.spotipower.event;

/**
 * Event for broadcasting media button actions
 * Created by vayner on 03.12.2014.
 */
public class MediaButtonEvent {
    public final ButtonType type;
    public final boolean pressed;

    public MediaButtonEvent(ButtonType type, boolean pressed) {
        this.type = type;
        this.pressed = pressed;
    }


    public enum ButtonType {
        PLAY,
        PAUSE,
        STOP,
        NEXT,
        PREVIOUS,
        FORWARD,
        BACKWARD,
        LOOP,
        REPEAT,
        RANDOM
    }
}
