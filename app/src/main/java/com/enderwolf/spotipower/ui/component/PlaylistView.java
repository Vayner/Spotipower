package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.adapter.CustomeSongList;

/**
 * Created by !Tulingen on 09.12.2014.
 */
public class PlaylistView extends FrameLayout {

    private ListView listView;
    private CustomeSongList adapter;

    public PlaylistView(Context context) {
        super(context);

        inflate(context, R.layout.playlist_view, this);
        listView = (ListView) this.findViewById(R.id.listSearched);
    }

    public void showPlaylist(Playlist playlist) {
        adapter = new CustomeSongList(this.getContext(), playlist);
        listView.setAdapter(adapter);
    }
}
