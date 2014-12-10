package com.enderwolf.spotipower.ui;

import com.enderwolf.spotipower.MusicPlayer;
import com.enderwolf.spotipower.Song;

import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.adapter.PlaylistListAdapter;
import com.enderwolf.spotipower.event.SongQueuedServerEvent;
import com.enderwolf.spotipower.event.SongUpdateEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PlaylistFragment extends Fragment {

//    private String TAG = getActivity().getCallingActivity().getClassName();

    private ListView listView;
    private PlaylistListAdapter adapter;

    //Test search

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playlist, container, false);
        listView = (ListView) root.findViewById(R.id.listSearched);

        adapter = new PlaylistListAdapter(getActivity(), MusicPlayer.getMusicPlayer().getPlaylist());
        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new SongUpdateEvent(Songs.get(position)));
            }
        });
        */

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(SongUpdateEvent event) {
        adapter = new PlaylistListAdapter(getActivity(), MusicPlayer.getMusicPlayer().getPlaylist());
        listView.setAdapter(adapter);
    }

    public void onEvent(SongQueuedServerEvent event) {
        adapter = new PlaylistListAdapter(getActivity(), MusicPlayer.getMusicPlayer().getPlaylist());
        listView.setAdapter(adapter);
    }
}

