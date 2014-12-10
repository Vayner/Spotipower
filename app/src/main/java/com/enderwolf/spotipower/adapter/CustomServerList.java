package com.enderwolf.spotipower.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.app.AppController;

import me.sbstensby.spotipowerhost.RemoteHostData;

import java.util.List;


/**
 * Created by chris on 10.12.2014.
 */
public class CustomServerList extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<RemoteHostData> remoteHostData;

    public CustomServerList(Context context, List<RemoteHostData> remoteHostDatas) {

        this.context = context;
        this.remoteHostData = remoteHostDatas;
    }

    @Override
    public int getCount() {
        return remoteHostData.size();
    }

    @Override
    public Object getItem(int location) {
        return remoteHostData.get(location);
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
            convertView = inflater.inflate(R.layout.list_server, null);
        }



        TextView title = (TextView) convertView.findViewById(R.id.serverTitle);

        title.setText(remoteHostData.get(position).name);

        return convertView;
    }
}



