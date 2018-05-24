package jsc.exam.jsckit;

import android.app.Application;

import jsc.kit.component.utils.BitmapCacheManager;

public class InstanceManager {
    private volatile static InstanceManager instance = null;
    private boolean isInitialized = false;
    private BitmapCacheManager bitmapCacheManager;

    private InstanceManager(){}

    public static InstanceManager getInstance() {
        if (instance == null){
            synchronized (InstanceManager.class){
                if (instance == null)
                    instance = new InstanceManager();
            }
        }
        return instance;
    }

    public void init(Application application){
        isInitialized =  true;
        bitmapCacheManager = new BitmapCacheManager();
    }

    public BitmapCacheManager getBitmapCacheManager() {
        checkIsInitialized();
        return bitmapCacheManager;
    }

    private void checkIsInitialized(){
        if (!isInitialized)
            throw new RuntimeException("Please init first.");
    }
}
