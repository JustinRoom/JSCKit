package jsc.exam.jsckit;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import jsc.exam.jsckit.ui.PhotoActivity;
import jsc.exam.jsckit.ui.component.AdvertisementViewActivity;
import jsc.kit.component.utils.SharePreferencesUtils;

public class MyApplication extends Application {

    public static final long ADVERTISEMENT_INTERNAL_TIME = 2 * 60 * 60 * 1000;//2小时
    private final String TAG = getClass().getSimpleName();
    private int mFinalCount;
    private ArrayMap<String, String> advertisementFilter = new ArrayMap<>();
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mFinalCount++;
            Log.e(TAG, "onActivityStarted: " + activity.getClass().getSimpleName() + ">>>" + mFinalCount);
            if (mFinalCount == 1 && canShowAdvertisement(activity.getClass())) {
                //如果是PhotoActivity时，在开启相机或者打开相册返回时不显示广告
                //onActivityResult(int requestCode, int resultCode, Intent data)在onActivityStarted(Activity activity)方法前调用，
                //所以不能在onActivityResult(int requestCode, int resultCode, Intent data)中设置enableAdvertisement = true
                if (activity instanceof PhotoActivity && !((PhotoActivity) activity).enableAdvertisement){
                    ((PhotoActivity) activity).enableAdvertisement = true;
                    return;
                }

                //说明从后台回到了前台
                long lastShowAdvertisementTimeStamp = SharePreferencesUtils.getInstance().getLong(Configration.SP_ADVERTISEMENT_LAST_SHOW_TIME, 0L);
                long curTime = System.currentTimeMillis();
                if ((curTime - lastShowAdvertisementTimeStamp > ADVERTISEMENT_INTERNAL_TIME)) {
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
//            Log.e(TAG, "onActivityStopped: " + activity.getClass().getSimpleName() + ">>>" + mFinalCount);
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
            addToAdvertisementFilter(AdvertisementViewActivity.class);
        }
    }

    /**
     * Add activity to advertisement filter.
     *
     * @param clazz clazz
     */
    public void addToAdvertisementFilter(Class<? extends Activity> clazz) {
        String key = clazz.getName();
        String activityName = clazz.getSimpleName();
        if (!advertisementFilter.containsKey(key))
            advertisementFilter.put(key, activityName);
    }

    /**
     * Remove activity from advertisement filter.
     *
     * @param clazz clazz
     */
    public void removeFromAdvertisementFilter(Class<? extends Activity> clazz) {
        String key = clazz.getName();
        if (advertisementFilter.containsKey(key))
            advertisementFilter.remove(key);
    }

    private boolean canShowAdvertisement(Class<? extends Activity> clazz){
        String key = clazz.getName();
        return !advertisementFilter.containsKey(key);
    }
}
