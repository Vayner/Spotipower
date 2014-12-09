package com.enderwolf.spotipower.ui;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.ui.component.PlaylistView;
import com.enderwolf.spotipower.utility.ParseCompleteCallback;
import com.enderwolf.spotipower.utility.Parser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements TabHost.OnTabChangeListener {

    private PlaylistView playlistView;
    private PlaylistView trackView;
    private PlaylistView albumView;
    private PlaylistView artistView;

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

        //this.getFragmentManager().beginTransaction().replace(R.id.tab1, playlistFragment).commit();
        //this.getFragmentManager().beginTransaction().replace(R.id.tab2, trackFragment).commit();
        //this.getFragmentManager().beginTransaction().replace(R.id.tab3, albumFragment).commit();
        //this.getFragmentManager().beginTransaction().replace(R.id.tab4, artistFragment).commit();

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d("onAttach", "getting playlist data");
        List<String> list = new ArrayList<>();
        list.add("spotify:track:2061tUGBzVsZvRdJS3D4hD");
        list.add("spotify:track:2061tUGBzVsZvRdJS3D4hD");
        list.add("spotify:track:2061tUGBzVsZvRdJS3D4hD");
        list.add("spotify:track:2061tUGBzVsZvRdJS3D4hD");

        Parser.ParseLookupList(list, new ParseCompleteCallback() {
            @Override
            public void OnParseComplete(Playlist playlist) {
                if (playlist == null) {
                    Log.e("ParseCompleteCallback", "Invalid playlist");
                    return;
                }

                trackView.showPlaylist(playlist);
            }
        });
    }

    @Override
    public void onTabChanged(String s) {

    }
}
