package com.messiaen.cryptotoolbox.feature.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class Keyboard {

    public static void clear(@NonNull Activity activity, @NonNull View view) {
        clearFocus(view);
        hideKeyboard(activity, view);
    }

    private static void clearFocus(@NonNull View view) {
        view.clearFocus();
    }

    private static void hideKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
