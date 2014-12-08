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
    private Map<String, SettingsEntry> settingValues = new HashMap<>();

    // TODO change over to objects / class hierarchy?
    private Settings () {
        this.settingValues.put("Test1", new BooleanEntry("Test1", false));
        this.settingValues.put("Test2", new BooleanEntry("Test2", false));
        this.settingValues.put("Test3", new BooleanEntry("Test3", false));
        //this.settingValues.put("Numbers", new IntegerEntry("Numbers", 1337));
    }

    private void overwriteSettings(Settings settings) {
        for(Map.Entry<String, SettingsEntry> e : settings.settingValues.entrySet()) {
            this.settingValues.put(e.getKey(), e.getValue());
        }

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
     * @param entry
     */
    public void put(SettingsEntry entry) {
        if(this.settingValues.containsKey(entry.getName())) {
            this.settingValues.put(entry.getName(), entry);
        }
    }

    SettingsEntry[] getAsList() {
        SettingsEntry[] list = new SettingsEntry[this.settingValues.size()];
        list = this.settingValues.values().toArray(list);
        return list;
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
        Settings loaded = SaveSystem.LoadData(fileName, context);

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
        SaveSystem.SaveData(fileName, context, getSettings());
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
