package com.isc.hermes.view;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.PointerIcon;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.isc.hermes.R;
import com.isc.hermes.controller.IncidentFormController;

import java.util.Locale;

public class IncidentTypeButton {
    private static Button buttonSelected;

    @SuppressLint("ResourceType")
    public static Button getIncidentTypeButton(Context context,
                                               String type,
                                               int color,
                                               String iconName) {
        Button typeButton = getStylizedButton(context, type, color, iconName);

        return typeButton;
    }


    private static Button getStylizedButton(Context context,
                                            String type,
                                            int color,
                                            String iconName) {
        Button typeButton = new Button(context);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(50);
        gd.setColor(color);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 0, 0, 10);
        layoutParams.setMarginStart(15);

        typeButton.setPadding(50, 0, 50, 0);
        typeButton.setLayoutParams(layoutParams);
        typeButton.setBackground(gd);
        typeButton.setText(type);
        typeButton.setTextColor(Color.WHITE);
        typeButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()), 0, 0, 0);
        Drawable icon = typeButton.getCompoundDrawables()[0];
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        typeButton.setCompoundDrawables(icon, null, null, null);
        typeButton.setCompoundDrawablePadding(40);

        return typeButton;
    }
}
