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

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import org.secuso.privacyfriendlyfoodtracker.helpers.FirstLaunchManager;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;

/**
 * @author Karola Marky
 * @version 20161022
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mainIntent = new Intent(SplashActivity.this, TutorialActivity.class);

        FirstLaunchManager firstStartPref = new FirstLaunchManager(this);

        if(firstStartPref.isFirstTimeLaunch()) {
            firstStartPref.initFirstTimeLaunch();
            mainIntent = new Intent(this, TutorialActivity.class);
        } else if(!KeyGenHelper.isKeyGenerated()) {
            firstStartPref.initFirstTimeLaunch();
            mainIntent = new Intent(this, TutorialActivity.class);
        } else {
            mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(mainIntent);
        finish();
    }

}
