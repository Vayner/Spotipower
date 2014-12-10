package com.enderwolf.spotipower.ui;
import me.sbstensby.spotipowerhost.RemoteHostData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.adapter.CustomServerList;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.sbstensby.spotipowerhost.HostDiscoverer;
import me.sbstensby.spotipowerhost.HostDiscovererInterface;
import me.sbstensby.spotipowerhost.RemoteHostData;

public class ConnectionManagerFragment extends Fragment implements HostDiscovererInterface {






    private ListView serList;
    private CustomServerList adapter;

    //Test search


    private List<RemoteHostData> hostDataList = null;
    private AlertDialog.Builder dialogRequestJoin;
    private List<RemoteHostData> remoteHostDatas = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConnectionManager.
     */
    public static ConnectionManagerFragment newInstance() {
        ConnectionManagerFragment fragment = new ConnectionManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public ConnectionManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_connection_manager, container, false);
        serList = (ListView) root.findViewById(R.id.listOfServers);


        RemoteHostData s1 = new RemoteHostData();
        s1.name = "somthing";
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            s1.address = addr;
            remoteHostDatas.add(s1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        dialogRequestJoin = new AlertDialog.Builder(getActivity());





        adapter = new CustomServerList(getActivity(), remoteHostDatas);
        serList.setAdapter(adapter);
        serList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){




                dialogRequestJoin.setMessage(remoteHostDatas.get(position).name)
                        .setPositiveButton("Join server", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                dialogRequestJoin.show();
            }
        });

        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private HashMap<String, String> createNewServer(String servername, String address) {
        HashMap<String, String> server = new HashMap<String, String>();
        server.put(servername, address);
        return server;
    }


    @Override
    public void notifyListUpdate() {
        if (this.isResumed()) {

        }
    }
}
