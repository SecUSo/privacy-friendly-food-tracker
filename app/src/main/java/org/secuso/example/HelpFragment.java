package org.secuso.example;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by yonjuni on 17.06.16.
 */
public class HelpFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.help);
    }

}
