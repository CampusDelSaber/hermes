package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.isc.hermes.R;
import com.isc.hermes.model.Utils.MapPolyline;

public class InfoRouteController {
    private RelativeLayout layout;
    private static InfoRouteController instanceNavigationController;
    private InfoRouteController(Context context){
        layout = ((AppCompatActivity)context).findViewById(R.id.distance_time_view);
    }

    public void showDistanceAndTimeView(){
        layout.setVisibility(View.VISIBLE);
    }

    public static InfoRouteController getInstance(Context context){
        if(instanceNavigationController == null){
            instanceNavigationController = new InfoRouteController(context);
        }
        return instanceNavigationController;
    }

    public void showInfoRoute(MapPolyline mapPolyline, JsonArray jsonArray){

    }
    public RelativeLayout getLayout() {
        return layout;
    }
}
