package com.enderwolf.spotipower.ui;
import me.sbstensby.spotipowerhost.Client;
import me.sbstensby.spotipowerhost.HostDiscoverer;
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

import java.util.List;

import me.sbstensby.spotipowerhost.HostDiscovererInterface;

public class ConnectionManagerFragment extends Fragment implements HostDiscovererInterface {

    private ListView serverListView;
    private CustomServerList adapter;
    private AlertDialog.Builder dialogRequestJoin;
    private List<RemoteHostData> remoteHostData = null;

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
    public void onResume() {
        super.onResume();
        HostDiscoverer.getInstance().setReturnInterface(this);
        HostDiscoverer.getInstance().requestHosts();
    }

    @Override
    public void onPause() {
        super.onPause();
        HostDiscoverer.getInstance().setReturnInterface(null);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_connection_manager, container, false);
        serverListView = (ListView) root.findViewById(R.id.listOfServers);


        dialogRequestJoin = new AlertDialog.Builder(getActivity());


        adapter = new CustomServerList(getActivity(), HostDiscoverer.getInstance().getHostList());
        serverListView.setAdapter(adapter);
        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {

                dialogRequestJoin.setMessage(HostDiscoverer.getInstance().getHostList().get(position).name)
                        .setPositiveButton("Join server", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Client.getInstance().setConnectedHost(HostDiscoverer.getInstance().getHostList().get(position));
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
    public void notifyListUpdate() {
        if (this.isResumed()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}