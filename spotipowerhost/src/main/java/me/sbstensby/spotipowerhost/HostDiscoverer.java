package me.sbstensby.spotipowerhost;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
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
 * Listens for three seconds (todo better solution)
 * for the reply "SPOTIPOWER_HOST_HERE:[hostname]"
 * returns an ArrayList<RemoteHostData> of hosts via the returnInterface.
 */
public class HostDiscoverer implements Runnable {
    private static HostDiscoverer instance = null;
    private HostDiscovererInterface returnInterface = null;
    private Thread thread;
    private ArrayList<RemoteHostData> hostList;
    private Context mContext;
    private static final byte[] sendData = "SPOTIPOWER_HOST_DISCOVERY".getBytes();

    private HostDiscoverer() {
        hostList = new ArrayList<>();
    }

    public static void init(Context mContext) {
        instance = new HostDiscoverer();
        instance.mContext = mContext;
    }

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
            serverSocet.setSoTimeout(100000);

            Socket socket = new Socket();

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

    public ArrayList<RemoteHostData> getHostList() {
        return hostList;
    }

    public void requestHosts() {
        //Start listening
        thread = new Thread(this);
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

                }
            }
        }.start();
    }

    /**
     * Gets the broadcast address.
     *
     * Modified from https://code.google.com/p/boxeeremote/wiki/AndroidUDP
     *
     * @return the broadcast address.
     * @throws IOException
     */
    private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) throw new IOException("Could not get WifiManager");

        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) throw new IOException("could not get dhcp");

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}