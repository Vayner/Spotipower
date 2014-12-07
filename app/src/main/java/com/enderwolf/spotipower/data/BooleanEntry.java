package com.enderwolf.spotipower.data;

public class BooleanEntry extends SettingsEntry<Boolean> {
    private Boolean value;

    public BooleanEntry (String name, Boolean value) {
        super(name, Boolean.TYPE);

        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
