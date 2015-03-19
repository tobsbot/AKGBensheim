package de.akg_bensheim.akgbensheim.preferences;

import android.os.Bundle;
import android.preference.Preference;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.akg_bensheim.akgbensheim.BuildConfig;
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

        Preference pref_licence = findPreference("pref_key_licence");
        pref_licence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsDialogs.showLicence(getActivity());
                return true;
            }
        });

        Preference pref_about = findPreference("pref_key_version");
        pref_about.setSummary(
                getResources().getString(
                        R.string.pref_summary_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.BUILD_TYPE,
                        BuildConfig.VERSION_CODE
                )
        );

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
