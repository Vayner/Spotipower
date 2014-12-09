package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class StringSelector extends LinearLayout {
    private EditText textField;
    private TextView text;

    public StringSelector(Context context, String name, String value) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);

        this.text = new TextView(context);
        this.text.setInputType(InputType.TYPE_NULL);
        this.text.setText(name);

        LayoutParams paramsText = generateDefaultLayoutParams();
        paramsText.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsText.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        this.addView(this.text, paramsText);

        this.textField = new EditText(context);
        this.textField.setInputType(InputType.TYPE_CLASS_TEXT);
        this.textField.setText(String.valueOf(value));

        LayoutParams paramsNumberField = generateDefaultLayoutParams();
        paramsNumberField.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsNumberField.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        this.addView(this.textField, paramsText);
    }

    public EditText getTextField() {
        return this.textField;
    }

    public TextView getText() {
        return this.text;
    }
}
