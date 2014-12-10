package com.enderwolf.spotipower.data;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.enderwolf.spotipower.ui.component.BooleanToggle;
import com.enderwolf.spotipower.ui.component.IntegerSelector;
import com.enderwolf.spotipower.ui.component.StringSelector;

import java.util.*;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class SettingsListAdapter extends BaseAdapter implements Observer {

    private Settings settings;
    private Context context;
    private List<SettingsEntry> list;

    public SettingsListAdapter(Context context, Settings settings) {
        super();

        this.context = context;
        this.settings = settings;
        list = new ArrayList<SettingsEntry>( Arrays.asList(this.settings.getAsList()) );
        Collections.sort(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getName().hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final SettingsEntry entry = list.get(i);
        Class type = entry.getType();

        if(type.equals(Boolean.TYPE)) {
            return new BooleanToggle(context, (BooleanEntry) entry);
        } else if (type.equals(Integer.TYPE)) {
            return new IntegerSelector(context, (IntegerEntry) entry);
        } else if (type.equals(String.class)) {
            return new StringSelector(context, (StringEntry) entry);
        } else {
            Log.w("SettingsListAdapter", "Unhandled SettingsEntry");

            TextView text = new TextView(context);
            text.setText("UNHANDLED SettingsEntry here, FIX IT!!");
            return text;
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
