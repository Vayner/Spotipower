package com.enderwolf.spotipower;


import java.util.ArrayList;

/**
 * Created by chris on 03.12.2014.
 */
public class Song {

    private String id;
    private String name;
    private String albumName;
    private String songUrl;
    private String thumbnailUrl;



    private ArrayList<String> artists;
    private int rating;


    public Song(){

        rating = 0;

    };

    public Song(String id, String name, String SongUrl){
        this.id = id;
        this.name = name;
        this.songUrl = SongUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumName() { return this.albumName; }

    public void setAlbumName(String albumName) { this.albumName = albumName; }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public ArrayList<String> getArtist() {
        return artists;
    }

    public void setArtist(ArrayList<String> artists) {
        this.artists = artists;
    }

    public String getStringOfArtists()
    {
        StringBuilder sb = new StringBuilder(); // Sava memory churn and make it easier for the gb collector

        for(int  i =0; i < artists.size(); i++)
        {
            sb.append(artists.get(i));
            if(i < artists.size()-1) // If the last artist. dont and comma but add . instead.
            {
                sb.append(", ");
            }else { sb.append("."); }
        }

        return sb.toString();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


}