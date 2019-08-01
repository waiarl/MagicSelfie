package com.mobi.magicselfie.base;

import android.view.ViewGroup;

/**
 * Created by waiarl on 2019/5/11.
 */
public class AddManager {
    private final String TAG = AddManager.class.getSimpleName();

    String ad_banner = "730001";
    String ad_insert = "710001";
    String ad_native = "720001";

    private static volatile AddManager manager;
    private boolean canShowNative = true;
   // private InterstitialLoader.InterstitialListener InsertAddListener;
    private AdListener insertListener;
    private Object mAdData;

    public static AddManager getInstance() {
        if (manager == null) {
            synchronized (AddManager.class) {
                if (manager == null) {
                    manager = new AddManager();
                }
            }
        }
        return manager;
    }

    private AddManager() {
        initSdk();
        initListener();
    }

    private void initSdk() {

    }

    private void initListener() {
       /* InsertAddListener = new InterstitialLoader.InterstitialListener() {
            @Override
            public void onError(String error) {
                Log.i(TAG, "onError:error=" + error);
            }

            @Override
            public void onAdLoaded(InterstitialLoader adData) {
                Log.i(TAG, "onAdLoaded");
                mAdData = (InterstitialLoader) adData;
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "onAdClosed");
                if (insertListener != null) {
                    insertListener.onAdClosed("", true);
                }
                insertListener = null;
            }

            @Override
            public void onAdClicked() {
                Log.i(TAG, "onAdClicked");

            }
        };*/
    }

    public void showAd(AdListener listener, boolean... justLoad) {
        this.insertListener = listener;
       /* InterstitialLoader loader = new InterstitialLoader(MyApp.getInstance(), ad_insert);
        loader.setListener(InsertAddListener);
        loader.loadAd();
        boolean noShow = justLoad != null && justLoad.length > 0 && !justLoad[0];
        if (hasNative() && !noShow) {
            try {
                ((InterstitialLoader) mAdData).showAd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

    public void showNativeAD(ViewGroup viewGroup, AdListener<Object> listener) {
/*
        NativeAdLoader mLoader = new NativeAdLoader(MyApp.getInstance(), ad_native, R.layout.activity_native_sample);
        mLoader.setListener(new NativeAdLoader.NativeListener() {
            @Override
            public void onAdLoaded(NativeAdLoader loader) {
                loader.showAd(viewGroup); //传入想要广告显示的layout
            }

            @Override
            public void onAdClicked() {
                Log.i(TAG, "native onAdClicked");

            }

            @Override
            public void onError(String error) {
                Log.i(TAG, "native onError:error=" + error);

            }
        });
        mLoader.loadAd();*/

    }

    public void showBannerAd(ViewGroup viewGroup, AdListener<Object> listener) {

       /* BannerAdLoader loader = new BannerAdLoader(MyApp.getInstance(), ad_banner);
        loader.setListener(new BannerAdLoader.BannerListener() {
            @Override
            public void onAdLoaded(BannerAdLoader loader) {
                loader.showAd(viewGroup);
            }

            @Override
            public void onAdClicked() {
                Log.i(TAG, "banner onAdClicked");

            }

            @Override
            public void onError(String error) {
                Log.i(TAG, "banner onError:error=" + error);

            }
        });
        loader.loadAd();
*/
    }

    public void setCanShowNative(boolean canShowNative) {
        this.canShowNative = canShowNative;
    }


    public boolean hasNative() {
        boolean has=false;
       //has = mAdData != null && mAdData instanceof InterstitialLoader && canShowNative;
        return has;
    }

    public void removeInsertAdListener() {
        insertListener = null;
    }

    public interface AdListener<T> {
        void onAdLoaded(String adUnitId, T adData);

        void onAdError(String adUnitId, String error);

        void onAdClicked(String adUnitId);

        void onAdClosed(String adUnitId, boolean finishReading);
    }

    public static abstract class SimpleAdListener<T> implements AdListener<T> {
        @Override
        public void onAdLoaded(String adUnitId, T adData) {

        }

        @Override
        public void onAdError(String adUnitId, String error) {

        }

        @Override
        public void onAdClicked(String adUnitId) {

        }

        @Override
        public void onAdClosed(String adUnitId, boolean finishReading) {

        }
    }


}
