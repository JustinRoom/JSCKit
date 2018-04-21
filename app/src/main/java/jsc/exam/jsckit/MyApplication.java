package jsc.exam.jsckit;

import android.app.Application;
import android.graphics.Color;

import jsc.kit.utils.BitmapCacheManager;
import jsc.kit.utils.CustomToast;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InstanceManager.getInstance().init(this);
        // TODO: 4/13/2018
        CustomToast.getInstance().init(this);
    }
}
