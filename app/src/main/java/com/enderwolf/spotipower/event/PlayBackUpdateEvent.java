package com.enderwolf.spotipower.event;

import com.spotify.sdk.android.playback.PlayerState;

/**
 * Event for keeping ui elements up to date on the state of the Spotify player, like progress in song.
 * Created by vayner on 02.12.2014.
 */
public class PlayBackUpdateEvent {
    public final PlayerState state;

    public PlayBackUpdateEvent(PlayerState state) {
        this.state = state;
    }
}
