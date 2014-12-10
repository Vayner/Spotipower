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

import com.enderwolf.spotipower.data.IntegerEntry;
import com.enderwolf.spotipower.data.Settings;

/**
 * Visual component for manipulating a IntegerEntry
 * Created by vayner on 07.12.2014.
 */
public class IntegerSelector extends LinearLayout {
    private EditText numberField;
    private TextView text;

    public IntegerSelector(Context context) {
        super(context);
    }

    public IntegerSelector(Context context, final IntegerEntry entry, boolean autoUpdate) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);

        this.text = new TextView(context);
        this.text.setTextAppearance(context, android.R.style.TextAppearance_Holo_Medium);
        this.text.setInputType(InputType.TYPE_NULL);
        this.text.setText(entry.getName());
        this.text.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        this.text.setPadding(5,5,5,5);

        LayoutParams paramsText = generateDefaultLayoutParams();
        paramsText.width = 0;
        paramsText.height = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsText.weight = 1;
        paramsText.gravity = Gravity.END | Gravity.CENTER_VERTICAL;

        this.addView(this.text, paramsText);

        this.numberField = new EditText(context);
        this.numberField.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.numberField.setText(String.valueOf(entry.getValue()));

        LayoutParams paramsNumberField = generateDefaultLayoutParams();
        paramsNumberField.width = 0;
        paramsNumberField.height = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsNumberField.weight = 1;
        paramsNumberField.gravity = Gravity.START | Gravity.CENTER_VERTICAL;

        this.addView(this.numberField, paramsNumberField);

        if(autoUpdate) {
            this.numberField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String value = editable.toString();
                    value = (value.equals("")) ? "0" : value;

                    entry.setValue(Integer.valueOf(value));
                    Settings.getSettings().put(entry);
                }
            });
        }
    }

    public IntegerSelector(Context context, final IntegerEntry entry) {
        this(context, entry, true);
    }

    public EditText getNumberField() {
        return this.numberField;
    }

    public TextView getText() {
        return this.text;
    }
}
