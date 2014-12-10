package com.enderwolf.spotipower.data;

import android.content.Context;

import com.enderwolf.spotipower.utility.SaveSystem;

import java.io.Serializable;
import java.util.*;

/**
 * Backbone of the settings system, handles all settings, loading, saving and notification on changes.
 * Created by vayner on 07.12.2014.
 */
public class Settings extends Observable implements Serializable {
    public static final String FILENAME = "settings.data";
    private static Settings settings = null;
    private final SortedMap<String, SettingsEntry> settingValues = new TreeMap<>();

    private static final SettingsEntry[] overrideDefault = {
        new BooleanEntry("Hosting", false)
    };

    private static final SettingsEntry[] defaultValues = {
        new BooleanEntry("Hosting", false),
        new StringEntry("Host name", "MyHostName"),
    };

    private Settings () {
        for(SettingsEntry entry : defaultValues) {
            this.put(entry, false, true);
        }
    }

    private void overwriteSettings(Settings settings) {
        for(Map.Entry<String, SettingsEntry> e : settings.settingValues.entrySet()) {
            this.put(e.getValue(), false, false);
        }

        for(SettingsEntry entry : overrideDefault) {
            this.put(entry, false, true);
        }

        this.setChanged();
        this.notifyObservers();
    }

    /**
     *
     * @param key to the value
     * @return the value
     */
    public SettingsEntry get(String key) {
        return this.settingValues.get(key);
    }

    /**
     * Checks if the key is a valid settings parameter and sets is value if it is.
     * @param entry the entry to add / update
     */
    public void put(SettingsEntry entry) {
        this.put(entry, true, false);
    }

    private void put(SettingsEntry entry, boolean notify, boolean overrideCheck) {
        if(overrideCheck || this.settingValues.containsKey(entry.getName())) {
            this.settingValues.put(entry.getName(), entry);
            if(notify) {
                this.setChanged();
                this.notifyObservers();
            }
        }
    }

    SettingsEntry[] getAsList() {
        SettingsEntry[] list = new SettingsEntry[this.settingValues.size()];
        list = this.settingValues.values().toArray(list);
        return list;
    }

    /**
     * Loads the settings from the default file and overwrites the current settings with it.
     * @param context context reference for filesystem access
     */
    public static void loadSettings(Context context) {
        loadSettings(FILENAME, context);
    }

    /**
     * Loads the settings from file and overwrites the current settings with it.
     * @param fileName the file to load from
     * @param context context reference for filesystem access
     */
    public static void loadSettings(String fileName, Context context) {
        Settings loaded = SaveSystem.LoadData(fileName, context);

        if(loaded == null) {
            return;
        }

        getSettings().overwriteSettings(loaded);
    }

    /**
     * Saves the settings to the default file.
     * @param context context reference for filesystem access
     */
    public static void saveSettings(Context context) {
        saveSettings(FILENAME, context);
    }

    /**
     * Saves the settings to file.
     * @param fileName the file to save to
     * @param context context reference for filesystem access
     */
    public static void saveSettings(String fileName, Context context) {
        SaveSystem.SaveData(fileName, context, getSettings());
    }

    /**
     * Gets the settings object
     * @return the settings singleton
     */
    public static Settings getSettings() {
        if(settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}
