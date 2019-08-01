package com.mobi.magicselfie.base;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * Created by waiarl on 2019-08-01.
 */
public class StringUtils {
    public static String getString(@StringRes int res) {
        return MyApp.getInstance().getString(res);
    }

    @NonNull
    public static String getString(@StringRes int resId, Object... formatArgs) {
        return MyApp.getInstance().getString(resId, formatArgs);
    }
}
