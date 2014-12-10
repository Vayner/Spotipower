package com.enderwolf.spotipower.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Abstract representation of a type of setting value.
 * Created by vayner on 07.12.2014.
 */
public abstract class SettingsEntry<T> implements Serializable, Comparable<SettingsEntry> {
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

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return !(o == null || !(o instanceof SettingsEntry)) && ((SettingsEntry) o).name.equals(this.name);
    }

    @Override
    public int compareTo(@NonNull SettingsEntry settingsEntry) {
        return this.name.compareTo(settingsEntry.name);
    }
}

