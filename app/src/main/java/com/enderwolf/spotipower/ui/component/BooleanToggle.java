package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class BooleanToggle extends LinearLayout {
    
    private ToggleButton button;
    private TextView text;
    
    public BooleanToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BooleanToggle(Context context, String name, boolean value) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);

        button = new ToggleButton(context);
        button.setChecked(value);

        LayoutParams paramsButton = generateDefaultLayoutParams();
        paramsButton.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsButton.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        this.addView(button, paramsButton);

        text = new TextView(context);
        text.setInputType(InputType.TYPE_NULL);
        text.setText(name);

        LayoutParams paramsText = generateDefaultLayoutParams();
        paramsText.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsText.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        this.addView(text, paramsText);
    }

    public ToggleButton getButton() {
        return button;
    }

    public TextView getText() {
        return text;
    }
}
