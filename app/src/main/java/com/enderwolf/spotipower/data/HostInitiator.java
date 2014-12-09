package com.enderwolf.spotipower.data;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;
import me.sbstensby.spotipowerhost.HostServer;

/**
 * Package: com.enderwolf.spotipower.data
 * Project: Spotipower
 * Filename:
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
            //TODO: get a proper name!
            Log.i("HostInitiator", "host now!");
            HostServer.getInstance().startHosting("She's crazy like a fool. What about daddy cool?");
        }
        else if (!(Boolean) Settings.getSettings().get("Hosting").getValue() && HostServer.getInstance().isHosting()) {
            HostServer.getInstance().stopHosting();
        }
    }
}
