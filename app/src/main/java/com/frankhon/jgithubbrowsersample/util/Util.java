package com.frankhon.jgithubbrowsersample.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Frank Hon on 2019-07-27 01:26.
 * E-mail: frank_hon@foxmail.com
 */
public class Util {
    private Util() {
    }

    public static void dismissKeyboard(Context context, View view) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
