package com.enderwolf.spotipower.data;

import android.content.Context;
import com.enderwolf.spotipower.utility.SaveSystem;

import java.io.Serializable;
import java.util.*;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class Settings extends Observable implements Serializable {
    public static final String FILENAME = "settings.data";
    private static Settings settings = null;
    private Map<String, Object> settingValues = new HashMap<>();

    // TODO change over to objects / class hierarchy?
    private Settings () {
        settingValues.put("Test", false);
    }

    private void overwriteSettings(Settings settings) {
        for(Map.Entry<String, Object> e : settings.settingValues.entrySet()) {
            this.settingValues.put(e.getKey(), e.getValue());
        }

        this.notifyObservers();
    }

    /**
     *
     * @param key to the value
     * @return the value
     */
    public Object get(String key) {
        return this.settingValues.get(key);
    }

    /**
     * Checks if the key is a valid settings parameter and sets is value if it is.
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if(this.settingValues.containsKey(key)) {
            this.settingValues.put(key, value);
        }
    }

    Map.Entry<String, Object>[] getAsList() {
        Map.Entry<String, Object>[] data = new Map.Entry[this.settingValues.size()];
        data = this.settingValues.entrySet().toArray(data);

        return data;
    }

    /**
     * Loads the settings from the default file and overwrites the current settings with it.
     * @param context
     */
    public static void loadSettings(Context context) {
        loadSettings(FILENAME, context);
    }

    /**
     * Loads the settings from file and overwrites the current settings with it.
     * @param fileName
     * @param context
     */
    public static void loadSettings(String fileName, Context context) {
        Settings loaded = SaveSystem.<Settings>LoadData(FILENAME, context);

        if(loaded == null) {
            return;
        }

        getSettings().overwriteSettings(loaded);
    }

    /**
     * Saves the settings to the default file.
     * @param context
     */
    public static void saveSettings(Context context) {
        saveSettings(FILENAME, context);
    }

    /**
     * Saves the settings to file.
     * @param fileName
     * @param context
     */
    public static void saveSettings(String fileName, Context context) {
        SaveSystem.SaveData(FILENAME, context, getSettings());
    }

    /**
     * Gets the settings object
     * @return
     */
    public static Settings getSettings() {
        if(settings == null) {
            settings = new Settings();
        }

        return settings;
    }
}
