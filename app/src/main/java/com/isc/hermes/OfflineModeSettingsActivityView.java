package com.isc.hermes;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


/**
 * This class control the offline mode settings UI.
 */
public class OfflineModeSettingsActivityView extends AppCompatActivity {
    /**
     * Method for creating a configuration of the activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_mode_settings_view);
    }

    /**
     * This method create a view
     * @param name the name of the downloaded region
     * @param size the size path of the downloaded region
     * @param ExpireTime the time whith the region expire
     */
    protected void createNewCardMapDownloaded(String name, String size, String ExpireTime){
        // create a view , and aded to vbox

    }

    /**
     * @param view
     */
    public void download_new_map(View view) {
        // code to download new map
    }

    /**
     * This method close the activity offline settings.
     *
     * @param view view
     */
    public void back_to_map(View view) {
        finish();
    }
}
