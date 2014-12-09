package com.enderwolf.spotipower.ui.component;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class IntegerSelector extends LinearLayout {
    private EditText numberField;
    private TextView text;

    public IntegerSelector(Context context, String name, Integer value) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);

        this.text = new TextView(context);
        this.text.setInputType(InputType.TYPE_NULL);
        this.text.setText(name);

        LayoutParams paramsText = generateDefaultLayoutParams();
        paramsText.width = 0;
        paramsText.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsText.weight = 1;

        this.addView(this.text, paramsText);

        this.numberField = new EditText(context);
        this.numberField.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.numberField.setText(String.valueOf(value));

        LayoutParams paramsNumberField = generateDefaultLayoutParams();
        paramsNumberField.width = 0;
        paramsNumberField.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsNumberField.weight = 1;

        this.addView(this.numberField, paramsNumberField);
    }

    public EditText getNumberField() {
        return this.numberField;
    }

    public TextView getText() {
        return this.text;
    }
}
