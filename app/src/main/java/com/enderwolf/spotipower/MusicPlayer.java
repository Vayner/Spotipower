package com.enderwolf.spotipower;

import android.net.Uri;
import android.util.Log;

import com.enderwolf.spotipower.event.MediaButtonEvent;
import com.enderwolf.spotipower.event.PlayBackUpdateEvent;
import com.enderwolf.spotipower.event.SongQueuedEvent;
import com.enderwolf.spotipower.event.SongQueuedServerEvent;
import com.enderwolf.spotipower.event.SongUpdateEvent;
import com.enderwolf.spotipower.utility.ParseCompleteCallback;
import com.enderwolf.spotipower.utility.Parser;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

import me.sbstensby.spotipowerhost.HostReceiver;
import me.sbstensby.spotipowerhost.HostRecieverInterface;

/**
 * Created by vayner on 07.12.14.
 *
 * is the connection point between the application and the Spotify player.
 */
public class MusicPlayer implements PlayerNotificationCallback, ConnectionStateCallback, HostRecieverInterface {
    private static final String REDIRECT_URI = "spotipower-login://callback";

    private static MusicPlayer musicPlayer = null;
    private static boolean init = false;

    private Player player;
    private final Timer timer;
    private final Playlist queue;

    private MusicPlayer () {
        timer = new Timer();
        queue = new Playlist("queue");
        HostReceiver.getInstance().setReturnInterface(this);
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
        if(eventType == EventType.PLAY) {
            EventBus.getDefault().post(new SongUpdateEvent(queue.get(0)));
        }
        if(eventType == EventType.TRACK_END && !playerState.playing) {
            if (queue.size() >= 2) {
                queue.remove(0);
                player.play(queue.get(0).getSongUri());
                EventBus.getDefault().post(new SongUpdateEvent(queue.get(0)));
            }
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
    }

    public void onEvent(SongQueuedEvent event){
        this.queue.add(event.song);
    }

    private void play() {
        if(queue.size() == 0) {
            return;
        }

        player.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                if (!playerState.playing && playerState.trackUri.equals("")) {
                    player.play(queue.get(0).getSongUri());
                } else {
                    player.resume();
                }

                EventBus.getDefault().post(new SongUpdateEvent(queue.get(0)));
            }
        });
    }

    public void onEvent(MediaButtonEvent event) {
        switch (event.type) {
            case NEXT:
                if (queue.size() >= 2) {
                    queue.remove(0);
                    player.play(queue.get(0).getSongUri());
                    EventBus.getDefault().post(new SongUpdateEvent(queue.get(0)));
                }
                break;

            case PREVIOUS:
                player.seekToPosition(0);
                break;

            case PLAY:
                play();
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

    public Playlist getPlaylist(){
        return (Playlist) queue.clone();
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
        Spotify spotify = new Spotify();
        Config playerConfig = new Config(app, response.getAccessToken(), app.getString(R.string.spotify_client_id));

        musicPlayer = getMusicPlayer();
        musicPlayer.player = spotify.getPlayer(playerConfig, app, new Player.InitializationObserver() {
            @Override
            public void onInitialized() {
                musicPlayer.player.addConnectionStateCallback(musicPlayer);
                musicPlayer.player.addPlayerNotificationCallback(musicPlayer);
                musicPlayer.timer.schedule(new ProgressUpdate(musicPlayer.player), 1000, 1000);
                EventBus.getDefault().register(musicPlayer);

                init = true;
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    public static boolean getInit() {
        return init;
    }

    /**
     * ------------------------------
     * Callbacks from HostReceiver.
     * ------------------------------
     */

    /**
     * Adds uri to queue
     * @param uri adds uri to queue
     */
    @Override
    public void queueAdd(String uri) {
        Parser.ParseLookup(uri.split(":")[2], new ParseCompleteCallback() {
            @Override
            public void OnParseComplete(Playlist playlist) {
                queue.add(playlist.get(0));
                EventBus.getDefault().post(new SongQueuedServerEvent());
            }
        });
    }

    /**
     * Removes song at index from playlist
     * @param index index of song to be removed
     */
    @Override
    public void queueRemove(int index) {
        queue.remove(index);
    }

    /**
     * Replaces the entire queue with a playlist.
     * @param uri the uri of the new playlist
     */
    @Override
    public void queueReplace(String uri) {

    }

    /**
     * pause playback.
     */
    @Override
    public void controlPause() {
        player.pause();
    }

    /**
     * resume playback
     */
    @Override
    public void controlResume() {
        player.resume();
    }

    /**
     * skip to the next song.
     */
    @Override
    public void controlSkip() {
        player.skipToNext();
    }

    /**
     * Asks if a user can join op
     * @return if the user can join OP.
     */
    @Override
    public boolean joinOP() {
        return true;
    }
}

class ProgressUpdate extends TimerTask {

    private final Player player;

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
