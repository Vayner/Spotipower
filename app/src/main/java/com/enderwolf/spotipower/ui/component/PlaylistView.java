package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.adapter.CustomeSongList;

/**
 * Created by !Tulingen on 09.12.2014.
 */
public class PlaylistView extends FrameLayout {

    private Playlist list = null;
    private ListView listView = null;
    private CustomeSongList adapter = null;

    public PlaylistView(Context context) {
        super(context);

        inflate(context, R.layout.playlist_view, this);
        listView = (ListView) this.findViewById(R.id.playlist_view);
    }

    public void showPlaylist(Playlist playlist) {
        list = playlist;

        if(this.getVisibility() == View.VISIBLE) {
            this.display();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == View.VISIBLE) {
            display();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void display() {
        adapter = new CustomeSongList(this.getContext(), list);
        listView.setAdapter(adapter);
    }
}
