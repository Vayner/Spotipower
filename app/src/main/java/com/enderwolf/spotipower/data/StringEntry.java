package com.enderwolf.spotipower.data;

/**
 * Represents a string settings object
 * Created by vayner on 09.12.14.
 */
public class StringEntry extends SettingsEntry<String> {
    private String value;

    protected StringEntry(String name, String value) {
        super(name, String.class);

        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
