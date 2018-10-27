package jsc.exam.jsckit;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.ui.component.AdvertisementViewActivity;
import jsc.kit.component.utils.SharePreferencesUtils;

public class MyApplication extends Application {

    private final String TAG = getClass().getSimpleName();
    private int mFinalCount;
    private List<String> advertisementFilter = new ArrayList<>();
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mFinalCount++;
            Log.e(TAG, "onActivityStarted: " + activity.getClass().getSimpleName() + ">>>" + mFinalCount);
            if (mFinalCount == 1 && canShowAdvertisement(activity.getClass())) {
                //说明从后台回到了前台
                long lastShowAdvertisementTimeStamp = SharePreferencesUtils.getInstance().getLong(Configration.SP_ADVERTISEMENT_LAST_SHOW_TIME, 0L);
                String picPathName = SharePreferencesUtils.getInstance().getString(Configration.SP_ADVERTISEMENT_PICTURE);
                long curTime = System.currentTimeMillis();
                if ((curTime - lastShowAdvertisementTimeStamp > 15 * 60 * 1000) && new File(picPathName).exists()) {
                    SharePreferencesUtils.getInstance().saveLong(Configration.SP_ADVERTISEMENT_LAST_SHOW_TIME, curTime);
                    activity.startActivity(new Intent(activity, AdvertisementViewActivity.class));
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
    };

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate()");
        super.onCreate();
        InstanceManager.getInstance().init(this);
        //初始化SharedPreferences工具
        SharePreferencesUtils.getInstance().init(this, "share_data");
        //注册activity生命周期监听
        if (BuildConfig.enableAdvertisementFeature) {
            registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            //启动界面不显示广告页
            addToAdvertisementFilter(LaunchActivity.class);
        }
    }

    /**
     * Add activity to advertisement filter.
     *
     * @param clazz clazz
     */
    public void addToAdvertisementFilter(Class<? extends Activity> clazz) {
        String activityName = clazz.getSimpleName();
        if (!advertisementFilter.contains(activityName))
            advertisementFilter.add(activityName);
    }

    /**
     * Remove activity from advertisement filter.
     *
     * @param clazz clazz
     */
    public void removeFromeAdvertisementFilter(Class<? extends Activity> clazz) {
        String name = clazz.getSimpleName();
        if (!advertisementFilter.contains(name))
            advertisementFilter.remove(name);
    }

    private boolean canShowAdvertisement(Class<? extends Activity> clazz){
        return !advertisementFilter.contains(clazz.getSimpleName());
    }
}
