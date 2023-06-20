package com.isc.hermes.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardManager {
    private static volatile KeyBoardManager instance;

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

    public void closeKeyBoard(View view, Context activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}