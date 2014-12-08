package me.sbstensby.spotipowerhost;


import android.util.Log;

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
    private boolean hosting;
    private Thread thread;
    private ServerSocket serverSocket;
    private Socket socket;
    private HostRecieverInterface returnInterface;

    public static HostReceiver getInstance() {
        return Holder.INSTANCE;
    }

    private HostReceiver() {
    }

    public void setReturnInterface (HostRecieverInterface _returnInterface) {
        this.returnInterface = _returnInterface;
    }

    public void startHosting() {
        hosting = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopHosting() {
        hosting = false;
        try {
            serverSocket.close();
            socket.close();
        } catch (IOException e) {
            //SOMETHING
        }
    }


    @Override
    public void run() {
        try {

            // Makes a ServerSocket on port 36250, used to receive inquiries.
            serverSocket = new ServerSocket (36250);
            socket = new Socket();

            // While there is communications
            while (hosting && (socket = serverSocket.accept())!=null) {

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
                        if (!HostServer.getInstance().isOP(socket.getInetAddress()) && !splitTmp[1].equals("ADD")) {
                            //The user is not allowed in here.
                            Log.i("HostReciever", "QUEUE_NO_OP_NO_ADD");
                            break;
                        }
                        switch (splitTmp[1]) {
                            case "ADD":
                                //Add a song to the back of the queue.
                                Log.i("HostReciever", "ADD");
                                returnInterface.queueAdd(splitTmp[2] + ":" + splitTmp[3] + ":" + splitTmp[4]);
                                break;

                            case "REMOVE":
                                //Remove a song from the queue.
                                Log.i("HostReciever", "REMOVE");
                                returnInterface.queueRemove(Integer.getInteger(splitTmp[2]));
                                break;

                            case "REPLACE":
                                //Replace the queue playlist.
                                Log.i("HostReciever", "REPLACE");
                                returnInterface.queueReplace(splitTmp[2] + ":" + splitTmp[3] + ":" + splitTmp[4]);
                                break;

                            default: break;
                        }
                        break;

                    case "CONTROL":
                        //Things to do with playback control.
                        if (!HostServer.getInstance().isOP(socket.getInetAddress())) {
                            //The user is not allowed in here.
                            Log.i("HostReciever", "CONTROL_NOT_OP");
                            break;
                        }
                        switch (splitTmp[1]) {
                            case "PAUSE":
                                //Pause playback.
                                Log.i("HostReciever", "PAUSE");
                                returnInterface.controlPause();
                                break;

                            case "RESUME":
                                //Resume playback.
                                Log.i("HostReciever", "RESUME");
                                returnInterface.controlResume();
                                break;

                            case "SKIP":
                                //Skip the current song.
                                Log.i("HostReciever", "SKIP");
                                returnInterface.controlSkip();
                                break;

                            default: break;
                        }
                        break;

                    case "JOINOP":
                        //A user asks to join with OP permissions.
                        if (returnInterface.joinOP()) {
                            HostServer.getInstance().addOP(socket.getInetAddress());
                            Log.i("HostReciever", "joinedOP");
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
