package com.enderwolf.spotipower.data;

import android.content.Context;
import com.enderwolf.spotipower.utility.SaveSystem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by !Tulingen on 07.12.2014.
 */
public class Settings extends Observable implements Serializable {
    private Map<String, String> settingValues = new HashMap<>();

    private Settings () {
    }

    private void overwriteSettings(Settings settings) {
        for(Map.Entry<String, String> e : settings.settingValues.entrySet()) {
            this.settingValues.put(e.getKey(), e.getValue());
        }

        this.notifyObservers();
    }

    private static Settings settings = null;

    public static void loadSettings(String fileName, Context context) {
        getSettings().overwriteSettings(SaveSystem.<Settings>LoadData(fileName, context));
    }

    public static void saveSettings(String fileName, Context context) {
        SaveSystem.SaveData(fileName, context, getSettings());
    }

    public static Settings getSettings() {
        if(settings == null) {
            settings = new Settings();
        }

        return settings;
    }
}
