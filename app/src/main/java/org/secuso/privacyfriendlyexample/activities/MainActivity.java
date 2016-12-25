package org.secuso.privacyfriendlyexample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.database.DatabaseExporter;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.tutorial.PrefManager;
import org.secuso.privacyfriendlyexample.tutorial.TutorialActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PFASQLiteHelper database = new PFASQLiteHelper(getBaseContext());
//        database.addSampleData(new PFASampleDataType(0, "eins.de", "hugo1", 11));
//        database.addSampleData(new PFASampleDataType(0, "zwei.de", "hugo2", 12));
//        database.addSampleData(new PFASampleDataType(0, "drei.de", "hugo3", 13));
//        database.addSampleData(new PFASampleDataType(0, "vier.de", "hugo4", 14));

        DatabaseExporter porter = new DatabaseExporter(getBaseContext().getDatabasePath(PFASQLiteHelper.DATABASE_NAME).toString(), "PF_EXAMPLE_DB");

        try {
            porter.dbToJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Use the a button to display the welcome screen
        Button b = (Button) findViewById(R.id.button_welcomedialog);
        if(b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    WelcomeDialog welcomeDialog = new WelcomeDialog();
//                    welcomeDialog.show(getFragmentManager(), "WelcomeDialog");
                    PrefManager prefManager = new PrefManager(getBaseContext());
                    prefManager.setFirstTimeLaunch(true);
                    Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }

        overridePendingTransition(0, 0);
    }

    /**
     * This method connects the Activity to the menu item
     * @return ID of the menu item it belongs to
     */
    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }


//    public static class WelcomeDialog extends DialogFragment {
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//            LayoutInflater i = getActivity().getLayoutInflater();
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setView(i.inflate(R.layout.welcome_dialog, null));
//            builder.setIcon(R.mipmap.icon);
//            builder.setTitle(getActivity().getString(R.string.welcome));
//            builder.setPositiveButton(getActivity().getString(R.string.okay), null);
//            builder.setNegativeButton(getActivity().getString(R.string.viewhelp), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ((MainActivity)getActivity()).goToNavigationItem(R.id.nav_help);
//                }
//            });
//
//            return builder.create();
//        }
//    }

    public void onClick(View view) {
        switch(view.getId()) {
            // do something with all these buttons?
            default:
        }
    }
}
