package com.enderwolf.spotipower;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.enderwolf.spotipower.ui.IGuiPlayback;
import com.enderwolf.spotipower.ui.MiniPlayer;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;

import java.util.ArrayList;
import java.util.List;


public class PlayerActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback, IGuiPlayback {

    // TODO: find a way to transfer this outside of git
    //private final static String CLIENT_ID = getString(R.string.spotify_client_id);
    //private static final String CLIENT_ID = "8cf97e259dc14327b50a3316ae6c3b60";
    private static final String REDIRECT_URI = "spotipower-login://callback";

    // Music Player
    private Player mPlayer;
    private PlayerState mPlayerState;
    private List<String> mPlayList;
    private int mPlayListPos;

    private MiniPlayer miniPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mPlayList = new ArrayList<String>();

        mPlayList.add("spotify:track:35SnuWHCBTJkfJYetXUF7X");
        mPlayList.add("spotify:track:5WJROlNr1bY44AHLBAydU3");
        mPlayList.add("spotify:track:5eS6pTvDNOvh2kyxeZtK3r");
        mPlayList.add("spotify:track:2aIRtTfx8Uc94znIaTANdf");
        mPlayList.add("spotify:track:4IdiGMOzEYXOh2897XOV8i");
        mPlayListPos = 2;

        miniPlayer = MiniPlayer.newInstance();

        //authenticate user
        //TODO: scopes
        SpotifyAuthentication.openAuthWindow(getString(R.string.spotify_client_id), "token", REDIRECT_URI,
                        new String[]{"user-read-private", "streaming"}, null, this);
    }

    public void playPause(View view) {
        mPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                if (playerState.playing) {
                    mPlayer.pause();
                } else {
                    mPlayer.resume();
                }
            }
        });
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
        mPlayerState = playerState;
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
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "My Company Name", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    mPlayer.addConnectionStateCallback(PlayerActivity.this);
                    mPlayer.addPlayerNotificationCallback(PlayerActivity.this);
                    mPlayer.play("spotify:track:2G6d1OttEYLmDJ2KzpJxvm");

                    getFragmentManager().beginTransaction().replace(R.id.myMiniPlayer, miniPlayer).commit();
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
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlayPressed() {
        mPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                if(!playerState.playing && playerState.trackUri == null) {
                    mPlayer.play(mPlayList, mPlayListPos);
                }
            }
        });

    }

    @Override
    public void onPausePressed() {
        mPlayer.pause();
    }

    @Override
    public void onStopPressed() {
        mPlayer.pause();
        mPlayer.seekToPosition(0);
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onPreviousPressed() {

    }

    @Override
    public void onForwardPressed() {

    }

    @Override
    public void onBackwardPressed() {

    }

    @Override
    public void onLoopPressed(boolean state) {

    }

    @Override
    public void onRandomPressed(boolean state) {

    }
}
