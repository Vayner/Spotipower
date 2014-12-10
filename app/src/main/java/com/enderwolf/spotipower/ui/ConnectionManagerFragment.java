package com.enderwolf.spotipower.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.event.SongQueuedClientEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class ConnectionManagerFragment extends Fragment {

    private Spinner spinnerListWithServers;
    private ListView listViewOfServers;
    List<Map<String, String>> serverList = new ArrayList<Map<String,String>>();
    private AlertDialog.Builder dialogRequestJoin;
    SimpleAdapter adapter;

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

        final ListView servers = (ListView) root.findViewById(R.id.listOfServers);


                serverList.add(createNewServer("server", "Server 1"));
                serverList.add(createNewServer("server", "Server 2"));
                serverList.add(createNewServer("server", "Server 3"));

            dialogRequestJoin = new AlertDialog.Builder(getActivity());

                adapter = new SimpleAdapter(getActivity(), serverList, android.R.layout.simple_list_item_1, new String[]{"server"}, new int[]{android.R.id.text1});

                servers.setAdapter(adapter);

        servers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){

               /* Toast.makeText(getApplicationContext(), "You clicked " + songList.get(position).getName(),
                        Toast.LENGTH_LONG).show(); */

                HashMap map =  (HashMap) adapter.getItem(position);

                dialogRequestJoin.setMessage(String.valueOf(map.get("server")))
                        .setPositiveButton("Join this server", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

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




        }
