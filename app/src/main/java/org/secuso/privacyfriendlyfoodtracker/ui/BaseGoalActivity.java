/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.secuso.privacyfriendlyfoodtracker.ui;

import android.os.Bundle;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.BaseActivity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.EditText;

/**
 * Base code for tapped activity.
 *
 * @author Andre Lutz
 */
public class BaseGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_goal);
    }

    protected int getNavigationDrawerID() {
        return R.id.nav_goal;
    }

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
        EditText goalNumber = this.findViewById(R.id.goal_number);
        Context context = BaseGoalActivity.this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("goal", new Float(String.valueOf(goalNumber.getText())));
        editor.apply();
        finish();
    }


}
