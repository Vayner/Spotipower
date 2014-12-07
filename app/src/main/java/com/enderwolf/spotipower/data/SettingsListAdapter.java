package com.enderwolf.spotipower.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

import java.util.*;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class SettingsListAdapter extends BaseAdapter implements Observer {

    private Settings settings;
    private Context context;
    private Map.Entry<String, Object>[] list;
    private ArrayList<View> views = new ArrayList<>();

    public SettingsListAdapter(Context context, final Settings settings) {
        super();

        this.context = context;
        list = settings.getAsList();

        for (final Map.Entry<String, Object> entry : list) {
            boolean value = (Boolean) entry.getValue();

            ToggleButton b = new ToggleButton(context);
            b.setChecked(value);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settings.put(entry.getKey(), ((ToggleButton) view).isChecked());
                }
            });

            views.add(b);
        }
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return list[position].hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return views.get(i);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
