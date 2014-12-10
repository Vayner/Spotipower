package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enderwolf.spotipower.data.DrawerEntry;

/**
 * Created by vayner on 10.12.14.
 */
public class DrawerEntryView extends LinearLayout {

    private ImageView iconView;
    private TextView textView;
    private DrawerEntry drawerEntry;

    public DrawerEntryView(Context context, DrawerEntry entry) {
        super(context);

        this.setPadding(3,5,3,5);
        this.setOrientation(LinearLayout.HORIZONTAL);

        this.drawerEntry = entry;


        this.iconView = new ImageView(context);
        this.iconView.setImageResource(this.drawerEntry.imageId);
        this.iconView.setMaxHeight(24);
        this.iconView.setMaxWidth(24);

        LinearLayout.LayoutParams iconParams = generateDefaultLayoutParams();
        iconParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        iconParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        iconParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.addView(this.iconView, iconParams);

        this.textView = new TextView(context);
        this.textView.setText(this.drawerEntry.text);
        this.textView.setTextSize(17.f);

        LinearLayout.LayoutParams textParams = generateDefaultLayoutParams();
        textParams.gravity = Gravity.CENTER;
        textParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        textParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.addView(this.textView, textParams);

    }
}
