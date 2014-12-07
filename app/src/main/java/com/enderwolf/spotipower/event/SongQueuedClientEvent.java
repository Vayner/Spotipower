package com.enderwolf.spotipower.event;

import com.enderwolf.spotipower.Song;

/**
 * Created by !Tulingen on 07.12.2014.
 */

//TODO Handle song on network system so that the server gets it.
public class SongQueuedClientEvent {
    public final Song song;

    public SongQueuedClientEvent (Song song) {
        this.song = song;
    }
}
