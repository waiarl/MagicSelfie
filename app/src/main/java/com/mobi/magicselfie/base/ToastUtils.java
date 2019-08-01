package com.mobi.magicselfie.base;

import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by waiarl on 2019-08-01.
 */
public class ToastUtils {
    public static void showToast(String text) {
        Toast.makeText(MyApp.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@StringRes int StringRes) {
        showToast(MyApp.getInstance().getString(StringRes));
    }
}
