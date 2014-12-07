package com.enderwolf.spotipower;

import java.util.ArrayList;

public class Playlist extends ArrayList<Song> {

    private String name;

    public Playlist(String name) {
        super();

        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
