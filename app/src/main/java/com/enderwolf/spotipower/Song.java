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
    private String songUri;

    private ArrayList<String> thumbnailUrl = new ArrayList<>();
    private ArrayList<String> artists = new ArrayList<>();

    public static Song newInstance(JSONObject songData) {
        Song song = new Song();

        try {
            JSONObject album = songData.getJSONObject("album");
            JSONArray artists = songData.getJSONArray("artists");
            JSONArray images = album.getJSONArray("images");

            song.id = songData.getString("id");
            song.songUri = "spotify:track:" + song.id;
            song.name = songData.getString("name");
            song.albumName = album.getString("name");

            for (int i = 0; i < artists.length(); i++) {
                song.artists.add(artists.getJSONObject(i).getString("name"));
            }

            for (int i = 0; i < images.length(); i++) {
                song.thumbnailUrl.add(images.getJSONObject(i).getString("url"));
            }

        } catch (JSONException e) {
            Log.e("Song init", "Invalid JSON object given to constructor", e);
            song = null;
        }

        return song;
    }

    private Song () {
        // To be used by factory
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

    public String getSongUri() {
        return songUri;
    }

    public String getThumbnailUrl(Quality quality) {
        return thumbnailUrl.get(quality.getQuality());
    }

    public ArrayList<String> getArtist() {
        return artists;
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

    public enum Quality {
        Small(2),
        Medium(1),
        Large(0);

        private int quality;

        Quality(int q) {
            this.quality = q;
        }

        public int getQuality() {
            return quality;
        }
    }
}