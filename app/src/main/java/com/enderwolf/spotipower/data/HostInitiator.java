package com.enderwolf.spotipower.data;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import me.sbstensby.spotipowerhost.HostReceiver;
import me.sbstensby.spotipowerhost.HostServer;

/**
 * Package: com.enderwolf.spotipower.data
 * Project: Spotipower
 * Filename: HostInitiator.java
 * Created by stensby on 09/12/14.
 */
public class HostInitiator implements Observer{

    private static final HostInitiator instance = new HostInitiator();

    private HostInitiator() {
        Settings.getSettings().addObserver(this);
    }

    public static HostInitiator getInstance() {
        return instance;
    }

    @Override
    public void update(Observable observable, Object o) {

        Log.i("HostInitiator", "update");
        if ((Boolean) Settings.getSettings().get("Hosting").getValue() && !HostServer.getInstance().isHosting()) {
            Log.i("HostInitiator", "host now!");
            HostServer.getInstance().startHosting((String)Settings.getSettings().get("Host name").getValue());
        }
        else if (!(Boolean) Settings.getSettings().get("Hosting").getValue() && HostServer.getInstance().isHosting()) {
            HostServer.getInstance().stopHosting();
        }

        // Update the hostname
        if ((Boolean) Settings.getSettings().get("Hosting").getValue() && !HostServer.getInstance().getHostname().equals((String)Settings.getSettings().get("Host name").getValue())) {
            HostServer.getInstance().setHostname((String)Settings.getSettings().get("Host name").getValue());
        }
    }
}
