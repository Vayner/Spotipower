package com.enderwolf.spotipower;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.enderwolf.spotipower.event.MediaButtonEvent;
import com.enderwolf.spotipower.event.PlayBackUpdateEvent;
import com.enderwolf.spotipower.ui.MiniPlayer;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlayerActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // TODO: find a way to transfer this outside of git
    //private final static String CLIENT_ID = getString(R.string.spotify_client_id);
    //private static final String CLIENT_ID = "8cf97e259dc14327b50a3316ae6c3b60";
    private static final String REDIRECT_URI = "spotipower-login://callback";

    // Music Player
    private Player musicPlayer;
    private PlayerState musicPlayerState;
    private List<String> musicPlayList;
    private int musicPlayListPos;

    private MiniPlayer miniPlayer;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        musicPlayList = new ArrayList<String>();

        musicPlayList.add("spotify:track:35SnuWHCBTJkfJYetXUF7X");
        musicPlayList.add("spotify:track:5WJROlNr1bY44AHLBAydU3");
        musicPlayList.add("spotify:track:5eS6pTvDNOvh2kyxeZtK3r");
        musicPlayList.add("spotify:track:2aIRtTfx8Uc94znIaTANdf");
        musicPlayList.add("spotify:track:4IdiGMOzEYXOh2897XOV8i");
        musicPlayListPos = 2;

        miniPlayer = MiniPlayer.newInstance();
        timer = new Timer();

        EventBus.getDefault().register(this);
        //authenticate user
        //TODO: scopes
        SpotifyAuthentication.openAuthWindow(getString(R.string.spotify_client_id), "token", REDIRECT_URI,
                        new String[]{"user-read-private", "streaming"}, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        musicPlayerState = playerState;
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

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


    public void onSearchForSongs(View view) {

        Intent myIntent = new Intent(PlayerActivity.this, ListOfQuedSongs.class);
        PlayerActivity.this.startActivity(myIntent);
        // Do something in response to button
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            musicPlayer = spotify.getPlayer(this, "My Company Name", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    musicPlayer.addConnectionStateCallback(PlayerActivity.this);
                    musicPlayer.addPlayerNotificationCallback(PlayerActivity.this);
                    musicPlayer.play("spotify:track:2G6d1OttEYLmDJ2KzpJxvm");

                    getFragmentManager().beginTransaction().replace(R.id.myMiniPlayer, miniPlayer).commit();
                    timer.schedule(new ProgressUpdate(), 1000, 1000);
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    public void onEvent(MediaButtonEvent event) {
        switch (event.type) {
            case NEXT:

            break;

            case PREVIOUS:

            break;

            case PLAY:
                musicPlayer.getPlayerState(new PlayerStateCallback() {
                    @Override
                    public void onPlayerState(PlayerState playerState) {
                        if (!playerState.playing && playerState.trackUri == null) {
                            musicPlayer.play(musicPlayList, musicPlayListPos);
                        } else {
                            musicPlayer.resume();
                        }
                    }
                });
            break;

            case PAUSE:
                musicPlayer.pause();
            break;

            case STOP:
                musicPlayer.pause();
                musicPlayer.seekToPosition(0);
            break;
        }
    }

    public void onPlayPressed() {


    }

    class ProgressUpdate extends TimerTask {
        @Override
        public void run() {
            musicPlayer.getPlayerState(new PlayerStateCallback() {
                @Override
                public void onPlayerState(PlayerState playerState) {
                    EventBus.getDefault().post(new PlayBackUpdateEvent(playerState));
                }
            });
        }
    }
}
