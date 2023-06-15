package com.isc.hermes.utils;

import android.view.animation.TranslateAnimation;

/**
 * This class manage the animations of the different components.
 */
public class Animations {
    public static TranslateAnimation entryAnimation;
    public static TranslateAnimation exitAnimation;

    /**
     * This class assign the values for the animations.
     */
    public static void loadAnimations() {
        entryAnimation = new TranslateAnimation(0, 0, 800, 0);
        entryAnimation.setDuration(600);
        entryAnimation.setStartOffset(50);

        exitAnimation = new TranslateAnimation(0, 0, 0, 800);
        exitAnimation.setDuration(600);
        exitAnimation.setStartOffset(50);
    }
}
