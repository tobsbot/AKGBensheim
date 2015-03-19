package de.akg_bensheim.akgbensheim.preferences;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.akg_bensheim.akgbensheim.R;
import de.akg_bensheim.akgbensheim.utils.FileUtils;

public class SettingsDialogs {

    /**
	 * Shows a Dialog which reads from Licence.html
	 * @param activity the activity the dialog has to be shown in.
	 */
	public static void showLicence(FragmentActivity activity) {
		FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_licence");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        new LicenceDialog().show(ft,"dialog_licence");
	}
	
	public static class LicenceDialog extends DialogFragment {

        public LicenceDialog() {
            super();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();

            @SuppressLint("InflateParams")
                View rootView = layoutInflater.inflate(R.layout.dialog_layout_licence, null);

            TextView aboutBodyView = (TextView) rootView.findViewById(R.id.dialog_view_body_licence);
            aboutBodyView.setText(Html.fromHtml(FileUtils.readAssetsTextFile(getActivity(), "licence.html")));
            aboutBodyView.setMovementMethod(new LinkMovementMethod());

            return new AlertDialog.Builder(getActivity())
                    .setView(rootView)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }
}