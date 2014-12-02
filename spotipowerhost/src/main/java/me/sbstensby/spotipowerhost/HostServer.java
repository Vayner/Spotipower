package me.sbstensby.spotipowerhost;

import android.util.Log;

/**
 * Package: com.enderwolf.spotipower
 * Project: Spotipower
 * Filename: HostServer.java
 * Created by stensby on 02/12/14.
 * As only one can run at once, this class is a singleton.
 * Implements Runnable, as it is designed to run asynchronously.
 */
public class HostServer implements Runnable{
    /**
     * private holder class for instance of class.
     */
    private static class Holder {
        static final HostServer INSTANCE = new HostServer();
    }

    private boolean hosting;
    private String hostname;
    private Thread thread;

    /**
     * Get instance of the Singleton.
     * @return instance.
     */
    public static HostServer getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * private constructor for singleton.
     */
    private HostServer() {
    }


    /**
     * Asynchronous runner of the class. Asks gets input and interprets.
     */
    @Override
    public void run() {
        Log.i("HostServer", "START");
        HostDiscoveryListener.getInstance().startHosting();
        while (this.hosting) {
            //Do some shit!
        }
        Log.i("HostServer", "STOP");
    }

    /**
     * @return Is the server hosting?
     */
    public boolean isHosting() {
        return hosting;
    }

    /**
     * Starts hosting the server.
     * @param hostname The hostname of the server to be hosted.
     */
    public void startHosting(String hostname) {
        this.hostname = hostname;
        this.hosting = true;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stop hosting the server.
     */
    public void stopHosting() {
        HostDiscoveryListener.getInstance().stopHosting();
        this.hosting = false;
    }

    /**
     * Gets the hostname of the server.
     * @return The hostname.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the hostname of the hosted server.
     * @param hostname The new hostname.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
