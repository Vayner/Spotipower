package com.enderwolf.spotipower.data;

/**
 * Represents a integer settings object
 * Created by vayner on 08.12.14.
 */
public class IntegerEntry extends SettingsEntry<Integer> {
    private int value;

    public IntegerEntry(String name, int value) {
        super(name, Integer.TYPE);

        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }
}
