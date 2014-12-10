package com.enderwolf.spotipower.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.adapter.CustomeSongList;
import com.enderwolf.spotipower.data.Settings;
import com.enderwolf.spotipower.event.SongQueuedClientEvent;
import com.enderwolf.spotipower.event.SongQueuedEvent;
import com.enderwolf.spotipower.ui.component.PlaylistView;
import com.enderwolf.spotipower.utility.ParseCompleteCallback;
import com.enderwolf.spotipower.utility.Parser;
import com.enderwolf.spotipower.utility.SearchConstructor;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.sbstensby.spotipowerhost.Client;

public class SearchFragment extends Fragment implements TabHost.OnTabChangeListener {



    private PlaylistView playlistView;
    private PlaylistView trackView;
    private PlaylistView albumView;
    private PlaylistView artistView;

    private Button searchButton;
    private EditText searchInput;

    private TabHost tabHost;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);




        tabHost = (TabHost) root.findViewById(R.id.tabHost);

        playlistView = new PlaylistView(this.getActivity());
        trackView = new PlaylistView(this.getActivity());
        albumView = new PlaylistView(this.getActivity());
        artistView = new PlaylistView(this.getActivity());

        trackView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Song song = (Song) adapterView.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true).setIcon(R.drawable.ic_queue_music_white_24dp).setTitle("Add this song to queue?");
                builder.setMessage(song.getName() + " - " + song.getAlbumName());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ((Boolean) Settings.getSettings().get("Hosting").getValue()) {
                            EventBus.getDefault().post(new SongQueuedEvent(song));
                        } else {
                            Client.getInstance().sendSong(song.getSongUri());
                        }
                    }
                }).setNegativeButton("No", null);

                builder.create().show();
            }
        });

        searchButton = (Button) root.findViewById(R.id.search_button);
        searchInput = (EditText) root.findViewById(R.id.search_input);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = searchInput.getText().toString();

                String playlistSearch = SearchConstructor.search(input, 0, 20, SearchConstructor.Type.Playlist);
                String trackSearch = SearchConstructor.search(input, 0, 20, SearchConstructor.Type.Track);
                String almubSearch = SearchConstructor.search(input, 0, 20, SearchConstructor.Type.Album);
                String artistSearch = SearchConstructor.search(input, 0, 20, SearchConstructor.Type.Artist);

                //search(playlistSearch, SearchConstructor.Type.Playlist, playlistView);
                search(trackSearch, SearchConstructor.Type.Track, trackView);
                //search(almubSearch, SearchConstructor.Type.Album, albumView);
                //search(artistSearch, SearchConstructor.Type.Artist, artistView);
            }
        });

        tabHost.setup();

        class FakeFactory implements TabHost.TabContentFactory {
            private View view;

            public FakeFactory(View view) {
                this.view = view;
            }

            @Override
            public View createTabContent(String s) {
                return view;
            }
        }

        TabHost.TabSpec playlists = tabHost.newTabSpec("Playlist");
        playlists.setIndicator("Playlist");
        playlists.setContent(new FakeFactory(playlistView));

        TabHost.TabSpec tracks = tabHost.newTabSpec("Track");
        tracks.setIndicator("Track");
        tracks.setContent(new FakeFactory(trackView));

        TabHost.TabSpec albums = tabHost.newTabSpec("Album");
        albums.setIndicator("Album");
        albums.setContent(new FakeFactory(albumView));

        TabHost.TabSpec artists = tabHost.newTabSpec("Artist");
        artists.setIndicator("Artist");
        artists.setContent(new FakeFactory(artistView));

        tabHost.addTab(playlists);
        tabHost.addTab(tracks);
        tabHost.addTab(albums);
        tabHost.addTab(artists);

        return root;
    }

    @Override
    public void onTabChanged(String s) {

    }

    private void search(String seachInput, SearchConstructor.Type searchType, final PlaylistView targetList) {
        Parser.ParseSearch(seachInput, searchType, new ParseCompleteCallback() {
            @Override
            public void OnParseComplete(Playlist playlist) {
                if (playlist == null) {
                    Log.e("ParseCompleteCallback", "Invalid playlist");
                    return;
                }

                targetList.showPlaylist(playlist);
            }
        });
    }
}
