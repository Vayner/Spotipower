package com.enderwolf.spotipower.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.enderwolf.spotipower.R;

import me.sbstensby.spotipowerhost.RemoteHostData;

import java.util.List;


/**
 * Adapter for the server list.
 * Created by chris on 10.12.2014.
 */
public class ServerListAdapter extends BaseAdapter {

    final private Context context;
    private LayoutInflater inflater;
    private List<RemoteHostData> remoteHostDataList;

    public ServerListAdapter(Context context, List<RemoteHostData> remoteHostDataList) {
        this.context = context;
        this.remoteHostDataList = remoteHostDataList;
    }

    @Override
    public int getCount() {
        return remoteHostDataList.size();
    }

    @Override
    public Object getItem(int location) {
        return remoteHostDataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_server, parent);
        }

        TextView title = (TextView) convertView.findViewById(R.id.serverTitle);

        title.setText(remoteHostDataList.get(position).name);

        return convertView;
    }
}



