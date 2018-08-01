package jsc.exam.jsckit;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import jsc.exam.jsckit.ui.component.AdvertisementViewActivity;
import jsc.kit.component.utils.CustomToast;
import jsc.kit.component.utils.SharePreferencesUtils;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private final String TAG = getClass().getSimpleName();
    private int mFinalCount;
    private boolean isOpenAdvertisement = false;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate()");
        super.onCreate();
        InstanceManager.getInstance().init(this);
        //初始化SharedPreferences工具
        SharePreferencesUtils.getInstance().init(this, "share_data");
        //注册activity生命周期监听
        if (isOpenAdvertisement)
            registerActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mFinalCount++;
        Log.e(TAG, "onActivityStarted: " + activity.getClass().getSimpleName() + ">>>" + mFinalCount);
        if (mFinalCount == 1 && !activity.getClass().getSimpleName().equals(LaunchActivity.class.getSimpleName())) {
            //说明从后台回到了前台
            long lastShowAdvertisementTimeStamp = SharePreferencesUtils.getInstance().getLong(Configration.SP_ADVERTISEMENT_LAST_SHOW_TIME, 0L);
            String picPathName = SharePreferencesUtils.getInstance().getString(Configration.SP_ADVERTISEMENT_PICTURE);
            long curTime = System.currentTimeMillis();
            if ((curTime - lastShowAdvertisementTimeStamp > 15 * 60 * 1000) && new File(picPathName).exists()) {
                SharePreferencesUtils.getInstance().saveLong(Configration.SP_ADVERTISEMENT_LAST_SHOW_TIME, curTime);
                activity.startActivity(new Intent(this, AdvertisementViewActivity.class));
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mFinalCount--;
        Log.e(TAG, "onActivityStopped: " + activity.getClass().getSimpleName() + ">>>" + mFinalCount);
        if (mFinalCount == 0) {
            //说明从前台回到了后台
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
