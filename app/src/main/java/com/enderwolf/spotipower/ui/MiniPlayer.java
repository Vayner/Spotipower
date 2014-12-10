package com.enderwolf.spotipower.ui;

import android.app.Activity;
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
    private TextView timerCurrent;
    private TextView timerMax;

    private Song currentSong = null;
    private TextView title;

    private DisplayMode playPauseToggle = DisplayMode.PLAY;

    private enum DisplayMode {
        PLAY(R.drawable.ic_play_circle_outline_white_36dp, MediaButtonEvent.ButtonType.PLAY),
        PAUSE(R.drawable.ic_pause_circle_outline_white_36dp, MediaButtonEvent.ButtonType.PAUSE);

        DisplayMode (int imageId, MediaButtonEvent.ButtonType type) {
            this.imageId = imageId;
            this.type = type;
        }

        public final int imageId;
        public final MediaButtonEvent.ButtonType type;

        public static DisplayMode getFromPlayerState (PlayerState state) {
            if(state.trackUri.equals("")) {
                return PLAY;
            }

            return (state.playing)? PAUSE : PLAY;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MiniPlayer.
     */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mini_player, container, false);

        ImageButton next = (ImageButton) root.findViewById(R.id.mini_next);
        playPauseButton = (ImageButton) root.findViewById(R.id.mini_play);
        ImageButton prev = (ImageButton) root.findViewById(R.id.mini_prev);

        timerCurrent = (TextView) root.findViewById(R.id.currentSongPlayed);
        timerMax = (TextView) root.findViewById(R.id.songLenght);


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
        StringBuilder time = new StringBuilder();
        time.append(String.valueOf( event.state.positionInMs / 60000 ));
        time.append(':');

        String sec = String.valueOf( (event.state.positionInMs / 1000) % 60 );

        if(sec.length() == 1) {
            time.append('0');
        }

        time.append(sec);

        timerCurrent.setText(time.toString());
        progressBar.setProgress(progress);
        setDisplayMode(DisplayMode.getFromPlayerState(event.state));
    }

    public void onEvent(SongUpdateEvent event){
        if(!event.song.equals(currentSong)){
            currentSong = event.song;
            StringBuilder time = new StringBuilder();
            time.append(String.valueOf( currentSong.getTotalTime() / 60000 ));
            time.append(':');

            String sec = String.valueOf( (currentSong.getTotalTime() / 1000) % 60 );

            if(sec.length() == 1) {
                time.append('0');
            }

            time.append(sec);

            timerMax.setText(time.toString());
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
