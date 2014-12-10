package com.enderwolf.spotipower.event;

import com.enderwolf.spotipower.Song;

/**
 * Event used to queue a song
 * Created by vayner on 07.12.2014.
 */

//TODO Handle song on network system so that the server gets it.
public class SongQueuedEvent {
    public final Song song;

    public SongQueuedEvent(Song song) {
        this.song = song;
    }
}
