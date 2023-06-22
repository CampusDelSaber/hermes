package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;

public class NavegationController {
    private RelativeLayout layout;
    private static NavegationController instanceNavigationController;
    private NavegationController(Context context){
        layout = ((AppCompatActivity)context).findViewById(R.id.distance_time_view);
    }

    public void showDistanceAndTimeView(){
        layout.setVisibility(View.VISIBLE);
    }

    public static NavegationController  getInstance(Context context){
        if(instanceNavigationController == null){
            instanceNavigationController = new NavegationController(context);
        }
        return instanceNavigationController;
    }

    public RelativeLayout getLayout() {
        return layout;
    }
}
