package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.adapter.PlaylistListAdapter;

/**
 * Visual component for viewing a playlist.
 * Created by vayner on 09.12.2014.
 */
public class PlaylistView extends FrameLayout {

    private Playlist list = null;
    private ListView listView = null;
    private PlaylistListAdapter adapter = null;

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
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == View.VISIBLE) {
            display();
        }
    }

    private void display() {
        if(this.list != null) {
            adapter = new PlaylistListAdapter(this.getContext(), list);
            listView.setAdapter(adapter);
        }
    }

    public ListView getListView() {
        return this.listView;
    }
}
