package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.enderwolf.spotipower.data.BooleanEntry;
import com.enderwolf.spotipower.data.Settings;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class BooleanToggle extends FrameLayout {

    private Switch button;
    private TextView text;

    public BooleanToggle(Context context, final BooleanEntry entry, boolean autoUpdate) {
        super(context);

        this.button = new Switch(context);
        this.button.setChecked(entry.getValue());
        this.button.setText(entry.getName());

        LayoutParams paramsButton = generateDefaultLayoutParams();
        paramsButton.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsButton.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsButton.gravity = Gravity.CENTER;

        this.addView(this.button, paramsButton);

        if(autoUpdate)  {
            this.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    entry.setValue(((Switch) view).isChecked());
                    Settings.getSettings().put(entry);
                }
            });
        }
    }
    public BooleanToggle(Context context, final BooleanEntry entry) {
        this(context, entry, true);
    }

    public Switch getButton() {
        return this.button;
    }

    public TextView getText() {
        return this.text;
    }
}
