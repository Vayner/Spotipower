package com.enderwolf.spotipower;


/**
 * Created by chris on 12.11.2014.
 */
public class Singleton {

    public static Singleton mInstance = null;

    public Playlist playlist = new Playlist();

    public Playlist getPlaylist(){

        return playlist;
    }

    public void setPlaylist(Playlist _playlist){

        playlist = _playlist;
    }

    private Singleton() {
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

}