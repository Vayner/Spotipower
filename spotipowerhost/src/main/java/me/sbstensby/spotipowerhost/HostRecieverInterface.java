package me.sbstensby.spotipowerhost;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: HostRecieverInterface.java
 * Created by stensby on 08/12/14.
 */
public interface HostRecieverInterface {
    public void queueAdd(String uri); //Add uri to the queue.
    public void queueRemove(int index); //remove song[index] from the queue.
    public void queueReplace(String uri); //replace the queue with a playlist

    public void controlPause(); //Pause the playback
    public void controlResume(); //resume the playback.
    public void controlSkip(); //Skip the song.

    public boolean joinOP(); //User asks to join OP.
}
