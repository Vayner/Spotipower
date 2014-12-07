package me.sbstensby.spotipowerhost;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Package: me.sbstensby.spotipowerhost
 * Project: Spotipower
 * Filename: HostReceiver.java
 * Created by stensby on 07/12/14.
 */
public class HostReceiver implements Runnable {
    private static class Holder {
        static final HostReceiver INSTANCE = new HostReceiver();
    }

    public static HostReceiver getInstance() {
        return Holder.INSTANCE;
    }

    private HostReceiver() {
    }


    @Override
    public void run() {
        try {

            // Makes a ServerSocket on port 36250, used to receive inquiries.
            ServerSocket serverSocket = new ServerSocket (36250);
            Socket socket = new Socket();

            // While there is communications
            while ((socket = serverSocket.accept())!=null) {

                // Makes an object to read from the stream.
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (socket.getInputStream()));

                // Reads a line from the client.
                String inputTmp = bufferedReader.readLine();

                // Split on /
                String[] splitTmp = {""};
                if(inputTmp != null){
                    splitTmp = inputTmp.split(":");
                }

                switch(splitTmp[0]) {
                    case "QUEUE":
                        //Things to do with the queue.
                        switch (splitTmp[1]) {
                            case "ADD":
                                //Add a song to the queue.
                                break;

                            case "REMOVE":
                                //Remove a song from the queue.
                                break;

                            case "REPLACE":
                                //Replace the queue playlist.
                                break;

                            default: break;
                        }
                        break;

                    case "CONTROL":
                        //Things to do with playback control.
                        switch (splitTmp[1]) {
                            case "PAUSE":
                                //Pause playback.
                                break;

                            case "RESUME":
                                //Resume playback.
                                break;

                            case "SKIP":
                                //Skip the current song.
                                break;

                            case "VOLUME":
                                //Change the volume.
                                switch (splitTmp[2]) {
                                    case "UP":
                                        break;

                                    case "DOWN":
                                        break;
                                }
                                break;

                            default: break;
                        }
                        break;

                    default: break;
                }


                // Close sockets
                socket.close();
                bufferedReader.close();
            }
            serverSocket.close();

            // Exceptions
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
