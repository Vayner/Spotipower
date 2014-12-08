package me.sbstensby.spotipowerhost;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: HostRecieverInterface.java
 * Created by stensby on 08/12/14.
 */
public interface HostRecieverInterface {
    public void queueAdd(String uri);
    public void queueRemove(int index);
    public void queueReplace(String uri);

    public void controlPause();
    public void controlResume();
    public void controlSkip();

    public boolean joinOP();
}
