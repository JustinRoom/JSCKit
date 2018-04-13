package jsc.exam.jsckit;

import android.app.Application;

import jsc.kit.utils.BitmapCacheManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BitmapCacheManager.getInstance().init();
        // TODO: 4/13/2018

    }
}
