package de.akg_bensheim.akgbensheim.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tobiaserthal on 14.03.15.
 */
public class FileUtils {
    public static String readStreamToString(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder text = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null)
            text.append(line).append('\n');

        bufferedReader.close();
        inputStreamReader.close();

        return text.toString();
    }

    public static String readRawTextFile(Context context, int resId) {
        String returnString;
        try {
            returnString = readStreamToString(context.getResources().openRawResource(resId));
        } catch (IOException e) {
            returnString = "";
            Log.e("FileUtils", "IOException occurred while trying to read resource with id: " + resId + "!", e);
        }
        return returnString;
    }

    public static String readAssetsTextFile(Context context, String path) {
        String returnString;
        try {
            returnString = readStreamToString(context.getAssets().open(path));
        } catch (IOException e) {
            returnString = "";
            Log.e("FileUtils", "IOException occurred while trying to read asset with path: " + path + "!", e);
        }
        return returnString;
    }
}
