package com.enderwolf.spotipower.event;

import com.enderwolf.spotipower.Song;

/**
 * Created by chris on 09.12.2014.
 */
public class SongUpdateEvent {

    public final Song song;

    public SongUpdateEvent(Song song){
        this.song = song;
    }
}