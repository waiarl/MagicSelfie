package com.mobi.magicselfie.base;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * Created by waiarl on 2019-08-01.
 */
public class Utils {
    public static int dip2Px(int dps) {
        return Math.round(MyApp.getInstance().getResources().getDisplayMetrics().density * dps);
    }

    public static boolean hasReadAndWritePermission() {
        boolean gr = ContextCompat.checkSelfPermission(MyApp.getInstance(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean gw = ContextCompat.checkSelfPermission(MyApp.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return gr && gw;
    }
    public static boolean hasCameraPermission() {
        boolean g = ContextCompat.checkSelfPermission(MyApp.getInstance(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        return g;
    }
}
