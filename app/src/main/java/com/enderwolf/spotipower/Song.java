package com.enderwolf.spotipower;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Song(String id, String name, String albumName, String SongUrl){
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.songUrl = SongUrl;
    }

    public Song(JSONObject songData) {
        try {
            JSONObject album = songData.getJSONObject("album");
            JSONArray artists = songData.getJSONArray("artists");
            JSONArray images = album.getJSONArray("images");

            this.id = songData.getString("id");
            this.name = songData.getString("name");
            this.albumName = album.getString("name");

            for (int j = 0; j < artists.length(); j++) {
                this.artists.add(artists.getJSONObject(j).getString("name"));
            }

            //Get medium quality [0 = bad] [ 1 = medium] [2 = good]
            this.thumbnailUrl = images.getJSONObject(1).getString("url");

        } catch (JSONException e) {
            Log.e("Song init", "Invalid JSON object given to constructor", e);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public String getSongUrl() {
        return songUrl;
    }

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

    public String getStringOfArtists() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < artists.size(); i++) {
            sb.append(artists.get(i));

            // If the last artist. dont and comma but add . instead.
            sb.append((i < artists.size() - 1)?  ", " : ".");
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