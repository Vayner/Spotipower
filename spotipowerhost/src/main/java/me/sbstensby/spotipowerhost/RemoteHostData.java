package me.sbstensby.spotipowerhost;

import java.net.InetAddress;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: RemoteHostData.java
 * Created by stensby on 02/12/14.
 *
 * "Struct" for saving info about remote hosts. Implements some self explanatory
 * functions.
 */
public class RemoteHostData {
    public final String name;
    public final InetAddress address;

    /**
     * Constructor
     * @param name The name of the host.
     * @param address InetAddress of the host.
     */
    public RemoteHostData(String name, InetAddress address) {
        this.address = address;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof RemoteHostData)) {
            return false;
        }

        RemoteHostData other = (RemoteHostData) o;

        return other.address.equals(this.address);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.address.hashCode();
    }
}
