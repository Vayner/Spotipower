package com.enderwolf.spotipower.event;

import com.spotify.sdk.android.playback.PlayerState;

/**
 * Created by !Tulingen on 02.12.2014.
 */
public class PlayBackUpdateEvent {
    public final PlayerState state;

    public PlayBackUpdateEvent(PlayerState state) {
        this.state = state;
    }
}
