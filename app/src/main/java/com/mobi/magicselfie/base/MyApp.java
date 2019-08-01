package com.mobi.magicselfie.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;


/**
 * Created by waiarl on 2019/5/30.
 */
public class MyApp extends Application {

    private static MyApp instance;
    private Bitmap currentBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initSdk();
    }

    private void initSdk() {
        //  WhatIsSdk.getInstance().init(this);

    }

    public static MyApp getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initAdSdk(base);
    }

    private void initAdSdk(Context base) {
      /*  String flurry = "DMZ7Q5CPFYRHM2636SWJ";
        String flurry_test = "ZNXDK3MJVYZ433JPV9K4";
        final String title = getString(R.string.app_name);
        final String key= BuildConfig.DEBUG?flurry_test:flurry;

        new WhatIsConfig.Builder()
                .setFlurryKey(key)
                .setNotifyContent(title)  //通知栏的content
                .setNotifyTitle(title)  //通知栏的title
                .setNotifyIcon(R.mipmap.ic_launcher)  //通知栏图标
                .setMainActivity(Welcome.class)  //快捷方式的入口
                .setShortCutName(title)  //快捷方式名称
                .setShortCutIcon(R.mipmap.ic_launcher)  //快捷方式icon
                .build();

        WhatIsSdk.getInstance().initDaemon(base);*/
    }

    public void setCurrentBitmap(Bitmap bitmap) {
        if (currentBitmap != null) {
            currentBitmap.recycle();
        }
        this.currentBitmap = bitmap;
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public boolean hasBitmap() {
       boolean has= currentBitmap!=null&&!currentBitmap.isRecycled();
        return has;
    }
}
