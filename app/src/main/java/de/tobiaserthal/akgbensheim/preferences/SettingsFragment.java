package de.tobiaserthal.akgbensheim.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.tobiaserthal.akgbensheim.BuildConfig;
import de.tobiaserthal.akgbensheim.R;
import de.tobiaserthal.akgbensheim.utils.ConnectionDetector;

public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private Preference pref_website;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        pref_website = getPreferenceManager().findPreference(Keys.WEBSITE);
        pref_website.setEnabled(ConnectionDetector.getInstance(getActivity())
                .allowedToUseConnection(Keys.ONLY_WIFI));

        findPreference(Keys.LICENCE).setOnPreferenceClickListener(this);
        findPreference(Keys.CONTACT_SUPPORT).setOnPreferenceClickListener(this);
        findPreference(Keys.ONLY_WIFI).setOnPreferenceChangeListener(this);

        findPreference(Keys.VERSION).setSummary(
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
            case Keys.ONLY_WIFI:
                pref_website.setEnabled(ConnectionDetector.getInstance(getActivity())
                        .allowedToUseConnection(Keys.ONLY_WIFI));
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case Keys.LICENCE:
                SettingsDialogs.showLicence(getActivity());
                return true;
            case Keys.CONTACT_SUPPORT:
                Intent intent = new Intent(Intent.ACTION_SENDTO)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_EMAIL,
                                getResources().getString(R.string.app_developer_email))
                        .putExtra(Intent.EXTRA_SUBJECT,
                                getResources().getString(R.string.app_developer_email_subject));

                startActivity(
                        Intent.createChooser(intent,
                                getResources().getString(R.string.pref_contact_support))
                );
                return true;
            default:
                return false;
        }
    }
}
