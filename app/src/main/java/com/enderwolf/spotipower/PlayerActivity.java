package com.enderwolf.spotipower;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.enderwolf.spotipower.data.DrawerEntry;
import com.enderwolf.spotipower.data.DrawerListAdapter;
import com.enderwolf.spotipower.data.HostInitiator;
import com.enderwolf.spotipower.data.Settings;
import com.enderwolf.spotipower.ui.AboutFragment;
import com.enderwolf.spotipower.ui.ConnectionManagerFragment;
import com.enderwolf.spotipower.ui.MiniPlayer;
import com.enderwolf.spotipower.ui.PlayerFragment;
import com.enderwolf.spotipower.ui.PlaylistFragment;
import com.enderwolf.spotipower.ui.SearchFragment;
import com.enderwolf.spotipower.ui.SettingsFragment;
import com.spotify.sdk.android.Spotify;

import me.sbstensby.spotipowerhost.HostDiscoverer;

public class PlayerActivity extends Activity implements AdapterView.OnItemClickListener {

    private boolean dualPane = false;

    private MiniPlayer miniPlayer = null;
    private PlayerFragment playerFragment = null;
    private SearchFragment searchFragment = null;
    private PlaylistFragment playlistFragment = null;
    private ConnectionManagerFragment connectionManagerFragment = null;
    private SettingsFragment settingsFragment = null;
    private AboutFragment aboutFragment = null;

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private static DrawerEntry[] drawerEntries = {
        new DrawerEntry(R.drawable.ic_home_white_24dp, "Home"),
        new DrawerEntry(R.drawable.ic_search_white_24dp, "Search"),
        new DrawerEntry(R.drawable.ic_my_library_music_white_24dp, "Playlist"),
        new DrawerEntry(R.drawable.ic_wifi_tethering_white_24dp, "Connections"),
        new DrawerEntry(R.drawable.ic_settings_applications_white_24dp, "Settings"),
        new DrawerEntry(R.drawable.ic_help_white_24dp, "About"),
        new DrawerEntry(R.drawable.ic_vpn_key_white_24dp, "Log on")
    };

    private Fragment[] drawerDataToFragments = new Fragment[drawerEntries.length];
    private int drawerDataCurrent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Settings.loadSettings(this);
        HostDiscoverer.init(this);
        HostInitiator.getInstance();
        dualPane = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        this.initGui();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
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

        drawerToggle.onConfigurationChanged(newConfig);
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

        playerFragment = PlayerFragment.newInstance();
        miniPlayer = MiniPlayer.newInstance();
        searchFragment = SearchFragment.newInstance();
        playlistFragment = PlaylistFragment.newInstance();
        connectionManagerFragment = ConnectionManagerFragment.newInstance();
        settingsFragment = SettingsFragment.newInstance();
        aboutFragment = AboutFragment.newInstance();

        drawerList = (ListView) this.findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(
            this,                   /* host Activity */
            drawerLayout,           /* DrawerLayout object */
            R.drawable.ic_drawer,   /* nav drawer icon to replace 'Up' caret */
            R.string.app_name,      /* "open drawer" description */
            R.string.app_name       /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerList.setAdapter(new DrawerListAdapter(this, drawerEntries));

        drawerList.setOnItemClickListener(this);

        if(dualPane)  {

            drawerDataToFragments[0] = playlistFragment;
            drawerDataToFragments[1] = searchFragment;
            drawerDataToFragments[2] = playlistFragment;
            drawerDataToFragments[3] = connectionManagerFragment;
            drawerDataToFragments[4] = settingsFragment;
            drawerDataToFragments[5] = aboutFragment;

            getFragmentManager().beginTransaction().replace(R.id.player_view, playerFragment).commit();

        } else {

            drawerDataToFragments[0] = playerFragment;
            drawerDataToFragments[1] = searchFragment;
            drawerDataToFragments[2] = playlistFragment;
            drawerDataToFragments[3] = connectionManagerFragment;
            drawerDataToFragments[4] = settingsFragment;
            drawerDataToFragments[5] = aboutFragment;
        }

        getFragmentManager().beginTransaction().replace(R.id.content_view, drawerDataToFragments[drawerDataCurrent]).commit();
        getFragmentManager().beginTransaction().replace(R.id.miniplayer_view, miniPlayer).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        drawerLayout.closeDrawers();

        if(drawerDataCurrent == i) {
            return;
        }

        if(i == 6) {
            if(!MusicPlayer.getInit()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder
                    .setTitle("Log on to spotify?")
                    .setMessage("This will open the web-browser with a login screen for authentication.")
                    .setIcon(R.drawable.ic_vpn_key_white_24dp)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MusicPlayer.initMusicPlayer(PlayerActivity.this);
                        }
                    }).setNegativeButton("No", null).create().show();
            }

            return;
        }

        drawerDataCurrent = i;
        getFragmentManager().beginTransaction().replace(R.id.content_view, drawerDataToFragments[drawerDataCurrent]).addToBackStack(null).commit();
    }
}
