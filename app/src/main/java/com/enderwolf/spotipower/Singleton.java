package com.enderwolf.spotipower;


/**
 * Created by chris on 12.11.2014.
 */
public class Singleton {

    public static Singleton mInstance = null;

    public Playlist playlist = new Playlist("Queue");

    public Playlist getPlaylist() {
        return this.playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        if(mInstance == null) {
            mInstance = new Singleton();
        }

        return mInstance;
    }

}