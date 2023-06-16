package com.isc.hermes.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * This class is used to generate the button view of the incidents.
 * Can make many buttons with the same style.
 */
public class IncidentTypeButton {

    /**
     * This method returns the button class, which was set with the type, color, and icon.
     *
     * @param context app context.
     * @param type incident type.
     * @param color color int for the button background.
     * @param iconName incident icon.
     * @return button stylized button.
     */
    @SuppressLint("ResourceType")
    public static Button getIncidentTypeButton(Context context,
                                               String type,
                                               int color,
                                               String iconName) {
        Button typeButton = new Button(context);

        setBackground(typeButton, color);
        setText(typeButton, type);
        setIcon(typeButton, iconName);
        setMargins(typeButton);

        return typeButton;
    }

    /**
     * This method set the background color and shape.
     *
     * @param button the button to set.
     * @param color color of the background.
     */
    private static void setBackground(Button button, int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(50);
        gd.setColor(color);

        button.setBackground(gd);
    }

    /**
     * This method set the icon on the button.
     *
     * @param button button to set.
     * @param iconName the name of the file.
     */
    private static void setIcon(Button button, String iconName) {
        button.setCompoundDrawablesWithIntrinsicBounds(
                button.getContext().getResources().getIdentifier(
                        iconName,
                        "drawable",
                        button.getContext().getPackageName()), 0, 0, 0);
        Drawable icon = button.getCompoundDrawables()[0];
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        button.setCompoundDrawables(icon, null, null, null);
        button.setCompoundDrawablePadding(40);
    }

    /**
     * This method set the margins and padding of the button.
     *
     * @param button the button to set.
     */
    private static void setMargins(Button button) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 0, 0, 10);
        layoutParams.setMarginStart(15);

        button.setLayoutParams(layoutParams);
        button.setPadding(50, 0, 50, 0);
    }

    /**
     * This method set the text of the button.
     *
     * @param button button to set.
     * @param type the type of incidents.
     */
    private static void setText(Button button, String type) {
        button.setText(type);
        button.setTextColor(Color.WHITE);
    }
}
