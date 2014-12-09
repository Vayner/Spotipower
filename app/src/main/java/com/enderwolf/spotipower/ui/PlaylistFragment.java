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
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.adapter.CustomeSongList;
import com.enderwolf.spotipower.app.AppController;
import com.enderwolf.spotipower.event.SongQueuedClientEvent;
import com.enderwolf.spotipower.event.SongUpdateEvent;
import com.enderwolf.spotipower.utility.ParseCompleteCallback;
import com.enderwolf.spotipower.utility.Parser;

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

    //Test search
    private static final String url = "https://api.spotify.com/v1/search?query=flame&offset=0&limit=10&type=track";
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new SongQueuedClientEvent(Songs.get(position)));

            }
        });
        Parser.ParseSearch(url, new ParseCompleteCallback() {
            @Override
            public void OnParseComplete(Playlist playlist) {
                Songs = playlist;
                showPlaylist(playlist);
            }
        });

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

    public void showPlaylist(Playlist playlist) {


        adapter = new CustomeSongList(this.getActivity(), playlist);
        listView.setAdapter(adapter);
    }
}
