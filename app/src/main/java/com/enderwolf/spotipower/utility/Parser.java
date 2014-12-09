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
 * Created by vayner on 08.12.14.
 */
public class Parser {
    private static final String lookupAddres = "https://api.spotify.com/v1/tracks/?ids=";

    public static void ParseUriList (List<String> uris, final ParseCompleteCallback callback) {
        StringBuilder request = new StringBuilder(lookupAddres);

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

    public static void ParseUri (String uri, ParseCompleteCallback callback) {
        List<String> dataList = new ArrayList<String>();
        dataList.add(uri);

        ParseUriList(dataList, callback);
    }
}

class SongJSONParser implements Response.Listener<JSONObject> {

    private ParseCompleteCallback callback;

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
