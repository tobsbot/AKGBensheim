package de.akg_bensheim.akgbensheim.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * A simple class for checking network states.
 * @author tobiaserthal
 *
 */
public class ConnectionDetector {

    private Context context;
    private ConnectivityManager connectivity;
    private static ConnectionDetector mInstance = null;

    /**
     * Creates a new Connection detector
     * @param context The current application context
     */
    private ConnectionDetector(Context context) {
        this.context = context;
    }

    /**
     * Returns the current dalvik vm's instance of the ConnectionDetector object. If not available, creates a new one.
     * <p>Use the application context as suggested by CommonsWare.
     * this will ensure that you dont accidentally leak an Activitys
     * context.</p>(see this article for more information: 
     * <a href="http://android-developers.blogspot.de/2009/01/avoiding-memory-leaks.html">Avoiding memory leaks</a>)
     */
    public static ConnectionDetector getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new ConnectionDetector(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Determines whether the current device is connected to the Internet
     * @return
     */
    public boolean isInternetConnected() {
        connectivity = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    /**
     * Determine whether the current Internet connection is available over wifi
     * @return <ul><li>true: Device is connected over wifi</li><li>false: Device is connected over mobile network</li></ul>
     */
    public boolean isWifiConnection() {
        connectivity = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();

        if(!isInternetConnected())
            return false;

        try {
            return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        } catch(Exception e) {
            Log.e("ConnectionDetector", "Error while trying to find out connection type. returning 'false'.", e);
            return false;
        }
    }

    /**
     * Combines other class methods with the user settings to check whether the app should use internet
     * @param pref_key_only_wifi The SharedPreference key for the user settings (only_wifi).<br />Must be of boolean type.
     * @return <ul><li>true: App is allowed to use connection</li><li>false: It isn't allowed.</li></ul>
     */
    public boolean allowedToUseConnection(String pref_key_only_wifi) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean onlyWifi = sharedPreferences.getBoolean(pref_key_only_wifi, false);
        boolean isInternetConnected = isInternetConnected();
        boolean isWifiConnected = isWifiConnection();

        return (isWifiConnected && onlyWifi) || (!onlyWifi && isInternetConnected);

    }

}