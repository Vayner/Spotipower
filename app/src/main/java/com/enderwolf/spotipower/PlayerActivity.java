package com.enderwolf.spotipower;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.enderwolf.spotipower.data.Settings;
import com.enderwolf.spotipower.ui.MiniPlayer;
import com.enderwolf.spotipower.ui.PlayerFragment;
import com.enderwolf.spotipower.ui.SettingsFragment;
import com.spotify.sdk.android.Spotify;
import de.greenrobot.event.EventBus;

public class PlayerActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private boolean dualPane = false;

    private MiniPlayer miniPlayer;
    private PlayerFragment playerFragment;

    private SettingsFragment settingsFragment;

    private NavigationDrawerFragment drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Settings.loadSettings(this);
        dualPane = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        settingsFragment = SettingsFragment.newInstance();
        playerFragment = PlayerFragment.newInstance();
        miniPlayer = MiniPlayer.newInstance();
        drawer = new NavigationDrawerFragment();
        DrawerLayout layout = new DrawerLayout(this);
        drawer.setUp(R.id.container, layout);

        ListView drawerList = (ListView) this.findViewById(R.id.navigation_drawer);

        String[] list = {"Hey", "Test"};

        drawerList.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list
        ));

        //authenticate user
        MusicPlayer.initMusicPlayer(this);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        boolean prevOrientation = dualPane;
        dualPane = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;

        if(prevOrientation != dualPane) {
            this.setContentView(R.layout.activity_nav_drawer);
            this.initGui();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            MusicPlayer.finalizeInitMusicPlayer(this, uri);
        }
    }



    protected void onResume() {
        super.onResume();
        Settings.loadSettings(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Settings.saveSettings(this);
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    // TODO orientation things
    public void initGui() {

        if(dualPane)  {
            getFragmentManager().beginTransaction().replace(R.id.player_view, playerFragment).commit();
            getFragmentManager().beginTransaction().replace(R.id.content_view, settingsFragment).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.miniplayer_view, miniPlayer).commit();
            getFragmentManager().beginTransaction().replace(R.id.content_view, settingsFragment).commit();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }
}
