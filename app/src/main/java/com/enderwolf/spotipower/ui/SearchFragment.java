package com.enderwolf.spotipower.ui;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enderwolf.spotipower.Playlist;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.utility.ParseCompleteCallback;
import com.enderwolf.spotipower.utility.Parser;

public class SearchFragment extends Fragment {

    private PlaylistFragment playlistFragment;
    private PlaylistFragment trackFragment;
    private PlaylistFragment albumFragment;
    private PlaylistFragment artistFragment;

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

        playlistFragment = PlaylistFragment.newInstance();
        trackFragment = PlaylistFragment.newInstance();
        albumFragment = PlaylistFragment.newInstance();
        artistFragment = PlaylistFragment.newInstance();

        this.getFragmentManager().beginTransaction().replace(R.id.tab1, playlistFragment).commit();
        this.getFragmentManager().beginTransaction().replace(R.id.tab2, trackFragment).commit();
        this.getFragmentManager().beginTransaction().replace(R.id.tab3, albumFragment).commit();
        this.getFragmentManager().beginTransaction().replace(R.id.tab4, artistFragment).commit();

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Parser.ParseUri("spotify:track:4VXdDBoubJhBsTuO3V3qwt", new ParseCompleteCallback() {
            @Override
            public void OnParseComplete(Playlist playlist) {
                trackFragment.showPlaylist(playlist);
            }
        });
    }
}
