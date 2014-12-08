package com.enderwolf.spotipower.data;

import java.io.Serializable;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public abstract class SettingsEntry<T> implements Serializable {
    private final String name;
    private final Class type;

    protected SettingsEntry(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Class getType() {
        return this.type;
    }

    public abstract T getValue();

    public abstract void setValue(T value);
}

