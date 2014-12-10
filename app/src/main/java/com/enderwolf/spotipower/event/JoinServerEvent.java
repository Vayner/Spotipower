package com.enderwolf.spotipower.event;

import java.net.InetAddress;

/**
 * Package: com.enderwolf.spotipower.event
 * Project: Spotipower
 * Filename:
 * Created by stensby on 10/12/14.
 */
public class JoinServerEvent {
    public final InetAddress ip;
    public JoinServerEvent(InetAddress ip) {
        this.ip = ip;
    }
}
