package com.enderwolf.spotipower;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    List<Song> Songs = new ArrayList<Song>();

    public Playlist(){}

    public void addSong(String id, String name){

        Songs.add(new Song(id, name));
        Log.d("debugg", name);

    }

    public void deleteSong(String id, String name){

        Song tempSong = new Song(id, name);
        for (int i = 0; i < Songs.size(); i++){
            if(tempSong.equals(Songs.get(i))){
                Songs.remove(i);
            }
        }
    }

    public void deleteSongs(){


        for (int i = 0; i < Songs.size(); i++){

            Songs.remove(i);

        }
    }

    public String displaySong(){
        StringBuilder sb = new StringBuilder();
        String song;

        for (int i=0; i<Songs.size(); i++) {
            sb.append("Song name: " + Songs.get(i).getName() + "\n");//"Song id: " + Songs.get(i).getId() + "\n");
        }


        return sb.toString();
    }


}
