package me.sbstensby.spotipowerhost;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: HostDiscoveryListener.java
 * Created by stensby on 01/12/14.
 * Listens on port 36251 for the UDP package "SPOTIPOWER_HOST_DISCOVERY".
 * Returns ("HOST_HERE:" + hostname) on 36252.
 * As only one can run at once, this class is a singleton.
 * Implements Runnable, as it is designed to run asynchronously.
 */
public class HostDiscoveryListener implements Runnable {

    /**
     * private holder class for instance of class.
     */
    private static class Holder {
        static final HostDiscoveryListener INSTANCE = new HostDiscoveryListener();
    }

    private DatagramSocket socket;
    private Thread thread;
    private boolean hosting;

    /**
     * Private constructor, as this will only be used by Holder.
     */
    private HostDiscoveryListener() {
    }

    /**
     * Get instance of the Singleton.
     * @return instance.
     */
    public static HostDiscoveryListener getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Function designed to run asynchronously, listening for clients asking the IP of the host.
     */
    @Override
    public void run() {
        Log.i("HostDiscoveryListener", "START");
        try {
            socket = new DatagramSocket(36251, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (hosting) {
                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.equals("SPOTIPOWER_HOST_DISCOVERY")) {
                    Log.i("HostDiscoveryListener", "Pinged by " + packet.getAddress().toString());
                    Socket socketSender = new Socket(packet.getAddress(), 36252);
                    BufferedWriter bufferedWriter = new BufferedWriter (new OutputStreamWriter(socketSender.getOutputStream()));
                    bufferedWriter.write("SPOTIPOWER_HOST_HERE:" + HostServer.getInstance().getHostname());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    socketSender.close();
                }
            }
        } catch (IOException e) {
            Log.e("HostDiscoveryListener", e.getMessage());

        }
        Log.i("HostDiscoveryListener", "STOP");
    }

    public void startHosting() {
        hosting = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopHosting() {
        hosting = false;
        socket.close();
    }
}
