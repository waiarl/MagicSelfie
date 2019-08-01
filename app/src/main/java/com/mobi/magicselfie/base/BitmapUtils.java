package com.mobi.magicselfie.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;

/**
 * Created by waiarl on 2019/1/17.
 */
public class BitmapUtils {


    public static Bitmap createBitmap(@NonNull Context context, float width, float height, String path) {
        final AssetManager am = context.getResources().getAssets();
        InputStream inputStream = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            inputStream = am.open(path);
            final Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            final int w = originalBitmap.getWidth();
            final int h = originalBitmap.getHeight();
            final float scw = width / w;
            final float sch = height / h;
            Matrix matrix = new Matrix();
            if (scw <= 0 && sch <= 0) {
                matrix.postScale(1, 1);
            } else if (scw == 0 || sch == 0) {
                final float sc = Math.max(scw, sch);
                matrix.postScale(sc, sc);
            } else {
                float max = Math.max(scw, sch);
                matrix.postScale(max, max);
            }
            final Bitmap bitmap = Bitmap.createBitmap(originalBitmap, 0, 0, w, h, matrix, true);
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap createBitmap(float width, float height, String path) {
        return createBitmap(MyApp.getInstance(), width, height, path);
    }





}
