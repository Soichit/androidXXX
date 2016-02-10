package edu.uw.soichit.xxx;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;
import com.github.danielnilsson9.colorpickerview.preference.ColorPreference;


public class SettingsActivity extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener {


    // Give your color picker dialog unique IDs if you
    // have multiple dialog. This will make it possible
    // for you to distinguish between them when you
    // get a result back in your ColorPickerDialogListener.
    private static final int DIALOG_ID = 0;
    private static final int PREFERENCE_DIALOG_ID = 1;


    private ExamplePreferenceFragment mPreferenceFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ExamplePreferenceFragment())
                .commit();


        mPreferenceFragment = new ExamplePreferenceFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, mPreferenceFragment).commit();
    }


    public void onClickColorPickerDialog(MenuItem item) {

        // The color picker menu item has been clicked. Show
        // a dialog using the custom ColorPickerDialogFragment class.

        ColorPickerDialogFragment f = ColorPickerDialogFragment
                .newInstance(DIALOG_ID, null, null, Color.BLACK, true);

        f.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        f.show(getFragmentManager(), "d");

    }


    @Override
    public void onColorSelected(int dialogId, int color) {
        switch(dialogId) {
            case PREFERENCE_DIALOG_ID:
                // We got result back from preference picker dialog in
                // ExamplePreferenceFragment. We forward it to the
                // fragment handling that particular preference.

                ((ColorPickerDialogFragment.ColorPickerDialogListener)mPreferenceFragment)
                        .onColorSelected(dialogId, color);

                break;
            case DIALOG_ID:
                // We got result from the other dialog, the one that is
                // shown when clicking on the icon in the action bar.

                //Toast.makeText(SettingsActivity.this, "Selected Color: " + colorToHexString(color), Toast.LENGTH_SHORT).show();
                break;
        }


    }

    @Override
    public void onDialogDismissed(int dialogId) {

        switch(dialogId) {
            case PREFERENCE_DIALOG_ID:
                // We got result back from preference picker dialog in
                // ExamplePreferenceFragment. We forward it to the
                // fragment handling that particular preference.

                ((ColorPickerDialogFragment.ColorPickerDialogListener)mPreferenceFragment)
                        .onDialogDismissed(dialogId);

                break;
        }
    }



    private static String colorToHexString(int color) {
        return String.format("#%06X", 0xFFFFFFFF & color);
    }

    public static class ExamplePreferenceFragment extends PreferenceFragment implements
            ColorPickerDialogFragment.ColorPickerDialogListener {


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main);

            // Find preference and add code to handle showing the ColorPickerDialogFragment
            // once requested.
            ColorPreference pref = (ColorPreference) findPreference("color");
            pref.setOnShowDialogListener(new ColorPreference.OnShowDialogListener() {

                @Override
                public void onShowColorPickerDialog(String title, int currentColor) {

                    // Preference was clicked, we need to show the dialog.
                    ColorPickerDialogFragment dialog = ColorPickerDialogFragment
                            .newInstance(PREFERENCE_DIALOG_ID, "Color Picker", null, currentColor, false);

                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);

                    // PLEASE READ!
                    // Show the dialog, the result from the dialog
                    // will end up in the parent activity since
                    // there really isn't any good way for fragments
                    // to communicate with each other. The recommended
                    // ways is for them to communicate through their
                    // host activity, thats what we will do.
                    // In our case, we must then make sure that MainActivity
                    // implements ColorPickerDialogListener because that
                    // is expected by ColorPickerDialogFragment.
                    //
                    // We also make this fragment implement ColorPickerDialogListener
                    // and when we receive the result in the activity's
                    // ColorPickerDialogListener when just forward them
                    // to this fragment instead.
                    dialog.show(getFragmentManager(), "pre_dialog");
                }
            });
        }

        @Override
        public void onColorSelected(int dialogId, int color) {
            switch (dialogId) {
                case PREFERENCE_DIALOG_ID:
                    // We have our result from the dialog, save it!
                    ColorPreference pref = (ColorPreference) findPreference("color");
                    pref.saveValue(color);
                    break;
            }
        }

        @Override
        public void onDialogDismissed(int dialogId) {
            // Nothing to do.
        }

    }

}