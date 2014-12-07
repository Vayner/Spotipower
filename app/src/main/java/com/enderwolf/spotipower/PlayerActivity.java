package com.enderwolf.spotipower;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.enderwolf.spotipower.data.Settings;
import com.enderwolf.spotipower.ui.MiniPlayer;
import com.enderwolf.spotipower.ui.PlayerFragment;
import com.enderwolf.spotipower.ui.SettingsFragment;
import com.spotify.sdk.android.Spotify;
import de.greenrobot.event.EventBus;

public class PlayerActivity extends Activity {

    private boolean dualPane = false;

    private MiniPlayer miniPlayer;
    private PlayerFragment playerFragment;

    private SettingsFragment settingsFragment;

    private ListView drawerList;
    private DrawerLayout layout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Settings.loadSettings(this);
        dualPane = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        this.initGui();

        //authenticate user
        MusicPlayer.initMusicPlayer(this);
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
        drawerToggle.onConfigurationChanged(newConfig);

        if(prevOrientation != dualPane) {
            this.setContentView(R.layout.activity_nav_drawer);

            settingsFragment = SettingsFragment.newInstance();
            playerFragment = PlayerFragment.newInstance();
            miniPlayer = MiniPlayer.newInstance();

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

    @Override
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

        settingsFragment = SettingsFragment.newInstance();
        playerFragment = PlayerFragment.newInstance();
        miniPlayer = MiniPlayer.newInstance();

        drawerList = (ListView) this.findViewById(R.id.left_drawer);
        layout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(
                this,                   /* host Activity */
                layout,                 /* DrawerLayout object */
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
        layout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        String[] list = {"Hey", "Test"};

        drawerList.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        ));

        if(dualPane)  {
            getFragmentManager().beginTransaction().replace(R.id.player_view, playerFragment).commit();
            getFragmentManager().beginTransaction().replace(R.id.content_view, settingsFragment).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.miniplayer_view, miniPlayer).commit();
            getFragmentManager().beginTransaction().replace(R.id.content_view, settingsFragment).commit();
        }
    }
}
