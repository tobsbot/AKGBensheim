package de.akg_bensheim.akgbensheim.preferences;

import android.os.Bundle;
import android.preference.Preference;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.akg_bensheim.akgbensheim.BuildConfig;
import de.akg_bensheim.akgbensheim.R;
import de.akg_bensheim.akgbensheim.utils.ConnectionDetector;

public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private Preference pref_website;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        pref_website = getPreferenceManager().findPreference("pref_key_website");
        pref_website.setEnabled(ConnectionDetector.getInstance(getActivity())
                .allowedToUseConnection("pref_key_only_wifi"));

        findPreference("pref_key_licence").setOnPreferenceClickListener(this);
        findPreference("pref_key_only_wifi").setOnPreferenceChangeListener(this);

        Preference pref_about = findPreference("pref_key_version");
        pref_about.setSummary(
                getResources().getString(
                        R.string.pref_summary_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.BUILD_TYPE,
                        BuildConfig.VERSION_CODE
                )
        );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "pref_key_only_wifi":
                pref_website.setEnabled(ConnectionDetector.getInstance(getActivity())
                        .allowedToUseConnection("pref_key_only_wifi"));
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "pref_key_licence":
                SettingsDialogs.showLicence(getActivity());
                return true;
            default:
                return false;
        }
    }
}
