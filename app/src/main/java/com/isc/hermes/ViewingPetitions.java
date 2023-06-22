package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.mapboxsdk.maps.Style;

public class ViewingPetitions extends AppCompatActivity {

    public void setMapStyle(){
        ViewingPetitions.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MapClickEventsManager.getInstance().getMapboxMap().setStyle(Style.DARK);
            }
        });
    }
}
