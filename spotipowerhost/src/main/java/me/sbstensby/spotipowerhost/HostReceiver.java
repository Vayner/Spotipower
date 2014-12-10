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
    private BufferedReader bufferedReader;

    public static HostReceiver getInstance() {
        return Holder.INSTANCE;
    }

    private HostReceiver() {
        try {
            serverSocket = new ServerSocket(0);
            serverSocket.close();
            socket = new Socket();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReturnInterface (HostRecieverInterface _returnInterface) {
        this.returnInterface = _returnInterface;
    }

    public void startHosting() {
        hosting = true;
        try {
            if (!socket.isClosed()) socket.close();
            if (!serverSocket.isClosed()) serverSocket.close();
        } catch (IOException e) {
            //SOMETHING
        }
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
        Log.i("HostReciever", "START");
        try {

            // Makes a ServerSocket on port 36250, used to receive inquiries.
            serverSocket = new ServerSocket (36250);
            socket = new Socket();

            // While there is communications
            while (hosting && (socket = serverSocket.accept())!=null) {
                // Makes an object to read from the stream.
                bufferedReader = new BufferedReader (new InputStreamReader (socket.getInputStream()));

                // Reads a line from the client.
                String inputTmp = bufferedReader.readLine();

                // Split on :
                String[] splitTmp = {""};
                if(inputTmp != null){
                    splitTmp = inputTmp.split(":");
                    Log.i("HostReceiver", "RECIEVED: " + inputTmp);
                }

                switch(splitTmp[0]) {
                    case "QUEUE":
                        //Things to do with the queue.
                        if (!HostServer.getInstance().isOP(socket.getInetAddress()) && !splitTmp[1].equals("ADD")) {
                            //The user is not allowed in here.
                            break;
                        }
                        switch (splitTmp[1]) {
                            case "ADD":
                                //Add a song to the back of the queue.
                                returnInterface.queueAdd(splitTmp[2] + ":" + splitTmp[3] + ":" + splitTmp[4]);
                                break;

                            case "REMOVE":
                                //Remove a song from the queue.
                                returnInterface.queueRemove(Integer.getInteger(splitTmp[2]));
                                break;

                            case "REPLACE":
                                //Replace the queue playlist.
                                returnInterface.queueReplace(splitTmp[2] + ":" + splitTmp[3] + ":" + splitTmp[4]);
                                break;

                            default: break;
                        }
                        break;

                    case "CONTROL":
                        //Things to do with playback control.
                        if (!HostServer.getInstance().isOP(socket.getInetAddress())) {
                            //The user is not allowed in here.
                            break;
                        }
                        switch (splitTmp[1]) {
                            case "PAUSE":
                                //Pause playback.
                                returnInterface.controlPause();
                                break;

                            case "RESUME":
                                //Resume playback.
                                returnInterface.controlResume();
                                break;

                            case "SKIP":
                                //Skip the current song.
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
            }
            socket.close();
            bufferedReader.close();
            serverSocket.close();

            // Exceptions
        } catch (IOException | NullPointerException e) {
            Log.e("HostReciever", "Something is wrong here!");
            e.printStackTrace();
        }

        Log.i("HostReciever", "STOP");
    }
}
