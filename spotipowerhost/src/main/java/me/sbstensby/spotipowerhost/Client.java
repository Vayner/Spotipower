package me.sbstensby.spotipowerhost;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: Client.java
 * Created by stensby on 07/12/14.
 *
 * Holds data needed when connected to remote host.
 * Is a singleton.
 */
public class Client {
    private static class Holder {
        static final Client INSTANCE = new Client();
    }
    private RemoteHostData connectedHost = null; //Stored data about the host

    public static Client getInstance() {
        return Holder.INSTANCE;
    }

    public String getHostName() {
        return connectedHost.name;
    }

    public InetAddress getHostAddress() {
        return connectedHost.address;
    }

    /**
     * sets the host on the Client.
     * @param host the host to be set.
     */
    public void setConnectedHost(RemoteHostData host) {
        this.connectedHost = host;
    }

    /**
     * Sends a song to the conected host, for queueing.
     * @param _uri The spotify URI of the song that should be added.
     */

    public void sendSong(String _uri) {
        this.sendMessage("QUEUE:ADD:"+_uri);
    }

    /**
     * sends a message to the connected host.
     * @param _message The message to be sent.
     */

    public void sendMessage(String _message) {
        final String message = _message;
        if(connectedHost == null) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(Client.getInstance().getHostAddress(), 36250);
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    socket.close();
                } catch (IOException e) {
                    //Unable to send message.
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Client() {

    }
}
