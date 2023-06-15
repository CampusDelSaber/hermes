package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.view.MapDisplay;

public class OfflineModeSettingsView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapbox();
        setContentView(R.layout.offline_mode_settings_view);
        initMapView();
    }
}
