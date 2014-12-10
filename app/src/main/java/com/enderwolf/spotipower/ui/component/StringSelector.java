package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enderwolf.spotipower.data.Settings;
import com.enderwolf.spotipower.data.SettingsEntry;
import com.enderwolf.spotipower.data.StringEntry;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class StringSelector extends LinearLayout {
    private EditText textField;
    private TextView text;

    public StringSelector(Context context, final StringEntry entry, boolean autoUpdate) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);

        this.text = new TextView(context);
        this.text.setInputType(InputType.TYPE_NULL);
        this.text.setText(entry.getName());

        LayoutParams paramsText = generateDefaultLayoutParams();
        paramsText.width = 0;
        paramsText.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsText.weight = 1;
        paramsText.gravity = Gravity.RIGHT;

        this.addView(this.text, paramsText);

        this.textField = new EditText(context);
        this.textField.setText(entry.getValue());

        LayoutParams paramsEditText = generateDefaultLayoutParams();
        paramsEditText.width = 0;
        paramsEditText.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsEditText.weight = 1;
        paramsEditText.gravity = Gravity.LEFT;

        this.addView(this.textField, paramsEditText);

        if(autoUpdate)  {
            this.textField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    entry.setValue(editable.toString());
                    Settings.getSettings().put(entry);
                }
            });
        }
    }

    public StringSelector(Context context, final StringEntry entry) {
        this(context, entry, true);
    }

    public EditText getTextField() {
        return this.textField;
    }

    public TextView getText() {
        return this.text;
    }
}
