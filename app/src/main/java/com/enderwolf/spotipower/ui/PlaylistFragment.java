package com.enderwolf.spotipower.ui;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.enderwolf.spotipower.Song;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.adapter.CustomeSongList;
import com.enderwolf.spotipower.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PlaylistFragment extends Fragment {

//    private String TAG = getActivity().getCallingActivity().getClassName();

    private ListView listView;
    private CustomeSongList adapter;

    //Testsearch
    private static final String url = "https://api.spotify.com/v1/search?query=flame&offset=0&limit=10&type=track";
    private ProgressDialog pDialog;
    private AlertDialog.Builder DialogRequestSong;
    private List<Song> Songs = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlaylistFragment.
     */
    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playlist, container, false);

        listView = (ListView) root.findViewById(R.id.listSearched);

        adapter = new CustomeSongList(getActivity(), Songs);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));

        /**
         *
         * Request a single JSONObject from a webresource via volley library.
         *
         * first JSONObject which is returned is with the tag "tracks"
         *
         * JSONObject[tracks] ->
         *             JSONArray[items]->
         *                      JSONObject[JSONArray[index]]->
         *                                          JSONObject[album] -> JSONObject[images] (medium quality) (want to get a sharp image) But not download to must bitrate.
         *                                          JSONArray[artists]-> JSONObject[artist] (artists.lenght())
         *
         *
         *
         *
         *
         */

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new SongParser(),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error.getMessage());
                        hidePDialog();
                    }
                }
        );
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getRequest);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //EventBus.getDefault().unregister(this);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    class SongParser implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            // display response
            hidePDialog();
            Log.d("Response", response.toString());

            try {
                JSONObject type = response.getJSONObject(("tracks"));
                JSONArray items = type.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject typeItem = items.getJSONObject(i);           // Every track
                    Song song = Song.newInstance(typeItem);

                    if(song != null) {
                        Songs.add(song);
                    }
                }
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void showPlaylist(Playlist playlist) {


        adapter = new CustomeSongList(this.getActivity(), playlist);
        listView.setAdapter(adapter);
    }
}
