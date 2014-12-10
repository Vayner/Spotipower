package me.sbstensby.spotipowerhost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.net.ServerSocket;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: HostDiscoverer.java
 * Created by stensby on 02/12/14.
 *
 * Broadcasts UDP package "SPOTIPOWER_HOST_DISCOVERY" on port 36251
 * Listens for three seconds
 * for the reply "SPOTIPOWER_HOST_HERE:[hostname]"
 * Notifies the interface whenever it finds a new host.
 */
public class HostDiscoverer implements Runnable {
    private static HostDiscoverer instance = new HostDiscoverer();
    private HostDiscovererInterface returnInterface = null;
    private ArrayList<RemoteHostData> hostList = new ArrayList<>();

    private HostDiscoverer() {
        hostList = new ArrayList<>();
    }

    /**
     * Sets the return interface
     */
    public void setReturnInterface(HostDiscovererInterface returnInterface) {
        this.returnInterface = returnInterface;
    }

    public static HostDiscoverer getInstance() {
        return instance;
    }

    @Override
    public void run() {
        Log.i("HostDiscoverer", "START");

        // Makes a serversocket on port 23457, used to receive inquiries.
        ServerSocket serverSocet;
        try {
            serverSocet = new ServerSocket(36252);
            serverSocet.setSoTimeout(10000);

            Socket socket;

            // While there is communications
            while ((socket = serverSocet.accept())!=null) {

                // Makes an object to read from the stream.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Reads a line from the client.
                String inputTmp = bufferedReader.readLine();

                // Split on /
                String[] splitTmp = {""};
                if (inputTmp != null) {
                    splitTmp = inputTmp.split(":");
                }
                // Push back the hosts.

                if (splitTmp[0].equals("SPOTIPOWER_HOST_HERE")) {
                    Log.i("HostDiscoverer", "Found " + splitTmp[1] + " at " + socket.getInetAddress().toString().replaceFirst("/", ""));
                    RemoteHostData rh = new RemoteHostData(splitTmp[1], socket.getInetAddress());
                    if (hostList.indexOf(rh) == -1) {
                        hostList.add(rh);
                        if (returnInterface != null) {
                            returnInterface.notifyListUpdate();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i("HostDiscoverer", "STOPPED");
    }

    /**
     * Getter for the host list
     * @return the host list
     */
    public ArrayList<RemoteHostData> getHostList() {
        return hostList;
    }

    /**
     * The thread you want to call. Remember to set the return interface first.
     * Starts the necessary threads for finding hosts on the network.
     */
    public void requestHosts() {
        //Start listening
        Thread thread = new Thread(this);
        thread.start();

        //Send request to any hosts out there
        new Thread() {
            @Override
            public void run() {
                // Find the server using UDP broadcast
                try {
                    //Open a random port to send the package
                    DatagramSocket datagramSocket = new DatagramSocket();
                    datagramSocket.setBroadcast(true);

                    byte[] sendData = "SPOTIPOWER_HOST_DISCOVERY".getBytes();

                    //Try the 255.255.255.255 first
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 36251);
                        datagramSocket.send(sendPacket);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Broadcast the message over all the network interfaces
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = interfaces.nextElement();

                        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                            continue; // Don't want to broadcast to the loopback interface
                        }

                        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                            InetAddress broadcast = interfaceAddress.getBroadcast();
                            if (broadcast == null) {
                                continue;
                            }

                            // Send the broadcast package!
                            try {
                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 36251);
                                datagramSocket.send(sendPacket);
                            } catch (Exception e) {
                                e.printStackTrace();
                                datagramSocket.close();
                            }
                        }
                    }


                    //Close the port!
                    datagramSocket.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

}