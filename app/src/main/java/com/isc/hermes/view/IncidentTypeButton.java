package com.isc.hermes.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.PointerIcon;
import android.widget.Button;

import com.isc.hermes.R;

public class IncidentTypeButton {

    public static Button getIncidentTypeButton(Context context,
                                               String type,
                                               int color,
                                               int iconID) {
        Button typeButton = new Button(context);
        typeButton.setText(type);
        typeButton.setRight(2);
        Drawable icon = Resources.getSystem().getDrawable(iconID);
        typeButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        typeButton.setBackgroundColor(color);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(12f);

        typeButton.setBackground(gd);

        return typeButton;
    }
}
