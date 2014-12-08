package com.enderwolf.spotipower.utility;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Created by !Tulingen on 28.09.2014.
 * Stores serializable objects to file.
 */
@SuppressWarnings("ALL")
public class SaveSystem {

    public static <T extends Serializable> T LoadData (String fileName, Context context) {

        T data;

        try {
            FileInputStream fileStream = context.openFileInput(fileName);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);

            //noinspection unchecked
            data = (T) objectStream.readObject();

            objectStream.close();
            fileStream.close();

        } catch (FileNotFoundException e) {
            Log.e(fileName, "FileNotFoundException", e);
            data = null;
        } catch (StreamCorruptedException e) {
            Log.e(fileName, "StreamCorruptedException", e);
            data = null;
        } catch (IOException e) {
            Log.e(fileName, "IOException", e);
            data = null;
        } catch (ClassNotFoundException e) {
            Log.e(fileName, "ClassNotFoundException", e);
            data = null;
        }

        return data;
    }

    public static <T extends Serializable> boolean SaveData (String fileName, Context context, T data) {
        try {
            FileOutputStream fileStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

            objectStream.writeObject(data);

            objectStream.close();
            fileStream.close();

        } catch (FileNotFoundException e) {
            Log.e(fileName, "FileNotFoundException", e);
            return false;
        } catch (IOException e) {
            Log.e(fileName, "IOException", e);
            return false;
        }

        return true;
    }

}
