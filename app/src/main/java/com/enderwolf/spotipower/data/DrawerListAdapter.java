package com.enderwolf.spotipower.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.enderwolf.spotipower.ui.component.DrawerEntryView;

/**
 * Adapter for the DrawerEntry list for displaying the list in the drawer.
 * Created by vayner on 10.12.14.
 */
public class DrawerListAdapter extends BaseAdapter {

    private DrawerEntry[] list;
    private final Context context;

    public DrawerListAdapter (Context context, DrawerEntry[] list) {
        super();

        this.context = context;
        this.list = list;
    }

    public void setList(DrawerEntry[] list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return list[i].text.hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return new DrawerEntryView(this.context, list[i]);
    }
}

