package com.enderwolf.spotipower.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.event.MediaButtonEvent;
import com.enderwolf.spotipower.event.PlayBackUpdateEvent;
import com.enderwolf.spotipower.event.SongUpdateEvent;
import com.spotify.sdk.android.playback.PlayerState;

import de.greenrobot.event.EventBus;

public class MiniPlayer extends Fragment {

    private ProgressBar progressBar;
    private ImageButton playPauseButton;

    private Song currentSong = null;
    private TextView title;

    private DisplayMode playPauseToggle = DisplayMode.PLAY;

    private enum DisplayMode {
        PLAY(R.drawable.ic_play_arrow_white_24dp, MediaButtonEvent.ButtonType.PLAY),
        PAUSE(R.drawable.ic_pause_white_24dp, MediaButtonEvent.ButtonType.PAUSE);

        DisplayMode (int imageId, MediaButtonEvent.ButtonType type) {
            this.imageId = imageId;
            this.type = type;
        }

        public final int imageId;
        public final MediaButtonEvent.ButtonType type;

        public static DisplayMode getOpposite (DisplayMode mode) {
            return (mode == PLAY)? PAUSE : PLAY;
        }

        public static DisplayMode getFromPlayerState (PlayerState state) {
            return (state.playing)? PLAY : PAUSE;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MiniPlayer.
     */
    // TODO: Rename and change types and number of parameters
    public static MiniPlayer newInstance() {
        MiniPlayer fragment = new MiniPlayer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public MiniPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mini_player, container, false);

        ImageButton next = (ImageButton) root.findViewById(R.id.mini_next);
        playPauseButton = (ImageButton) root.findViewById(R.id.mini_play);
        ImageButton prev = (ImageButton) root.findViewById(R.id.mini_prev);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MediaButtonEvent(MediaButtonEvent.ButtonType.NEXT, true));
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MediaButtonEvent(playPauseToggle.type, true));

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MediaButtonEvent(MediaButtonEvent.ButtonType.PREVIOUS, true));
            }
        });

        progressBar = (ProgressBar) root.findViewById(R.id.mini_progress);

        title = (TextView) root.findViewById((R.id.nameOfSong));

        return root;
    }

    public void onEvent(PlayBackUpdateEvent event) {
        int progress = (int) (((float) event.state.positionInMs / (float) event.state.durationInMs) * 100.f);
        System.out.println("progress bar value  " + String.valueOf(progress));
        progressBar.setProgress(progress);
        setDisplayMode(DisplayMode.getFromPlayerState(event.state));
    }

    public void onEvent(SongUpdateEvent event){
        if(!event.song.equals(currentSong)){
            currentSong = event.song;
            title.setText(currentSong.getName());
        }
    }

    private void setDisplayMode(DisplayMode mode) {
        if(this.playPauseToggle == mode) {
            return;
        }

        this.playPauseToggle = mode;
        this.playPauseButton.setImageResource(mode.imageId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
