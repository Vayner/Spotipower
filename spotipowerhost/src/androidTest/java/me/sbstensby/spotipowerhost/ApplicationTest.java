package me.sbstensby.spotipowerhost;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

        assertTrue(Client.getInstance() != null);
        assertTrue(HostDiscoverer.getInstance() != null);
        assertTrue(HostDiscoveryListener.getInstance() != null);
        assertTrue(HostReceiver.getInstance() != null);
        assertTrue(HostServer.getInstance() != null);


        try {
            RemoteHostData dat = new RemoteHostData("qwerty", InetAddress.getByName("127.0.0.1"));
            assertEquals("qwerty", dat.name);
            assertEquals(InetAddress.getByName("127.0.0.1"), dat.address);
        } catch (UnknownHostException e) {
            assertTrue(false);
        }
    }
}