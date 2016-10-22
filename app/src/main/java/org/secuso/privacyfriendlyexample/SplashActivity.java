package org.secuso.privacyfriendlyexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yonjuni on 22.10.16.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Starts the MainActivity immediately after the Splash Screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
