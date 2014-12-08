package com.enderwolf.spotipower;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.enderwolf.spotipower.event.MediaButtonEvent;
import com.enderwolf.spotipower.event.PlayBackUpdateEvent;
import com.enderwolf.spotipower.ui.PlayerFragment;
import com.enderwolf.spotipower.ui.SettingsFragment;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by vayner on 07.12.14.
 *
 * is the connection point between the application and the spotify player.
 */
public class MusicPlayer implements PlayerNotificationCallback, ConnectionStateCallback {
    private static final String REDIRECT_URI = "spotipower-login://callback";

    private static MusicPlayer musicPlayer = null;

    private Player player;
    private Timer timer;

    private Playlist queue;
    private int currentTrackIndex = 0;

    private MusicPlayer () {
        timer = new Timer();
        queue = new Playlist("queue");
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onNewCredentials(String s) {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }

    public void onEvent(MediaButtonEvent event) {
        switch (event.type) {
            case NEXT:

                break;

            case PREVIOUS:

                break;

            case PLAY:
                if(queue.size() == 0) {
                    return;
                }

                player.getPlayerState(new PlayerStateCallback() {
                    @Override
                    public void onPlayerState(PlayerState playerState) {
                        if (!playerState.playing && playerState.trackUri == null) {
                            player.play(queue.get(currentTrackIndex).getSongUrl());
                        } else {
                            player.resume();
                        }
                    }
                });
            break;

            case PAUSE:
                player.pause();
            break;

            case STOP:
                player.pause();
                player.seekToPosition(0);
            break;
        }
    }


    public static MusicPlayer getMusicPlayer() {
        if(musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }

        return musicPlayer;
    }

    public static void initMusicPlayer(PlayerActivity app) {
        SpotifyAuthentication.openAuthWindow(
            app.getString(R.string.spotify_client_id),
            "token",
            REDIRECT_URI,
            new String[]{"user-read-private", "streaming"},
            null,
            app
        );
    }

    public static void finalizeInitMusicPlayer(final PlayerActivity app, final Uri uri) {
        AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
        Spotify spotify = new Spotify(response.getAccessToken());
        musicPlayer = getMusicPlayer();
        musicPlayer.player = spotify.getPlayer(app, "My Company Name", app, new Player.InitializationObserver() {
            @Override
            public void onInitialized() {
                musicPlayer.player.addConnectionStateCallback(musicPlayer);
                musicPlayer.player.addPlayerNotificationCallback(musicPlayer);

                EventBus.getDefault().register(musicPlayer);

                musicPlayer.timer.schedule(new ProgressUpdate(musicPlayer.player), 1000, 1000);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }
}

class ProgressUpdate extends TimerTask {

    private Player player;

    ProgressUpdate(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                EventBus.getDefault().post(new PlayBackUpdateEvent(playerState));
            }
        });
    }
}
