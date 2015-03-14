package de.akg_bensheim.akgbensheim.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tobiaserthal on 14.03.15.
 */
public class FileUtils {
    public static String readRawTextFile(Context context, int resId) {
        InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().openRawResource(resId));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder text = new StringBuilder("");

        try {
            while (( line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            Log.e("FileUtils", "IOException occurred while trying to read resource with id: " + resId + "!", e);
        }
        return text.toString();
    }
}
