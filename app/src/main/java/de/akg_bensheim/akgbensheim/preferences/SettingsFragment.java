package de.akg_bensheim.akgbensheim.preferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.akg_bensheim.akgbensheim.R;
import de.akg_bensheim.akgbensheim.utils.ConnectionDetector;

public class SettingsFragment extends PreferenceFragment{
    private Preference pref_website;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        pref_website = findPreference("pref_key_website");
        pref_website.setEnabled(ConnectionDetector.getInstance(getActivity())
                .allowedToUseConnection("pref_key_only_wifi"));
        pref_website.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent webIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("http://www.akg-bensheim.de"));
                startActivity(webIntent);
                return true;
            }
        });

        Preference pref_licence = findPreference("pref_key_licence");
        pref_licence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsDialogs.showLicence(getActivity());
                return true;
            }
        });

        Preference pref_about = findPreference("pref_key_version");
        try {
            pref_about.setSummary(
                    String.format(
                            getResources().getString(R.string.pref_summary_version),
                            getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName
                    )
            );
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SettingsFragment", "Unable to resolve package name", e);
            e.printStackTrace();
        }

        Preference pref_only_wifi = findPreference("pref_key_only_wifi");
        pref_only_wifi.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                pref_website.setEnabled(ConnectionDetector.getInstance(getActivity())
                        .allowedToUseConnection("pref_key_only_wifi"));
                return true;
            }
        });
    }
}
