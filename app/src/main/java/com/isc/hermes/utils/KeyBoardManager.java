package com.isc.hermes.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * This class manages the keyboard.
 */
public class KeyBoardManager {
    private static volatile KeyBoardManager instance;

    /**
     * This method returns the instance of the KeyBoardManager class.
     * @return the instance of the KeyBoardManager class
     */
    public static KeyBoardManager getInstance() {
        if (instance == null) {
            synchronized (KeyBoardManager.class) {
                if (instance == null) {
                    instance = new KeyBoardManager();
                }
            }
        }
        return instance;
    }

    /**
     * Closes the virtual keyboard in the given view.
     *
     * @param view The view that has the current focus.
     * @param activity The context of the current activity.
     */
    public void closeKeyBoard(View view, Context activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}