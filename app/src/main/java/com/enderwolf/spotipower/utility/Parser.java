package com.enderwolf.spotipower.utility;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON parser for Spotify request results. Generates a Playlist if the JSON was valid, null otherwise.
 * Created by vayner on 08.12.14.
 */
public class Parser {
    private static final String lookupAddress = "https://api.spotify.com/v1/tracks/?ids=";

    public static void ParseLookupList(List<String> uris, final ParseCompleteCallback callback) {
        StringBuilder request = new StringBuilder(lookupAddress);

        for (String uri : uris) {
            request.append(uri.substring(uri.lastIndexOf(':') + 1));
            request.append(',');
        }

        request.deleteCharAt(request.length() - 1);

        JsonObjectRequest getRequest = new JsonObjectRequest (
            Request.Method.GET,
            request.toString(),
            null,
            new SongJSONParser(callback),
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Parser", "Error: " + error.getMessage());
                }
            }
        );

        AppController.getInstance().addToRequestQueue(getRequest);
    }

    public static void ParseLookup(String uri, ParseCompleteCallback callback) {
        List<String> dataList = new ArrayList<>();
        dataList.add(uri);

        ParseLookupList(dataList, callback);
    }

    public static void ParseSearch(final String search, final SearchConstructor.Type searchType, ParseCompleteCallback callback) {
        JsonObjectRequest getRequest = new JsonObjectRequest (
            Request.Method.GET,
            search,
            null,
            new SearchTrackJSONParser(callback),
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Parser", "Error: " + error.getMessage());
                }
            }
        );

        AppController.getInstance().addToRequestQueue(getRequest);
    }
}

class SongJSONParser implements Response.Listener<JSONObject> {

    private final ParseCompleteCallback callback;

    public SongJSONParser(ParseCompleteCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(JSONObject response) {
        Playlist playlist = new Playlist("");

        try {
            JSONArray type = response.getJSONArray(("tracks"));
            for (int i = 0; i < type.length(); i++) {
                JSONObject typeItem = type.getJSONObject(i);

                Song song = Song.newInstance(typeItem);

                playlist.add(song);
            }
        }

        catch (JSONException e) {
            Log.e("SongJSONParser", "Invalid JSON", e);
            playlist = null;
        }

        callback.OnParseComplete(playlist);
    }
}

class SearchTrackJSONParser implements Response.Listener<JSONObject> {

    private final ParseCompleteCallback callback;

    public SearchTrackJSONParser(ParseCompleteCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(JSONObject response) {
        Playlist playlist = new Playlist("");

        try {
            JSONObject type = response.getJSONObject("tracks");
            JSONArray items = type.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject typeItem = items.getJSONObject(i);

                Song song = Song.newInstance(typeItem);

                playlist.add(song);
            }
        }

        catch (JSONException e) {
            Log.e("SongJSONParser", "Invalid JSON", e);
            playlist = null;
        }

        callback.OnParseComplete(playlist);
    }
}
