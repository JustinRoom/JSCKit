package jsc.kit.component.baseui.fragmentmanager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 *
 * Link {@link FragmentBackHelper}.
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class BackRecord {

    private String TAG = "BackRecord";

    @IdRes
    private int containerViewId;// container view id for add fragment
    private WeakReference<Fragment> fragmentWeakReference;//fragment cache, it will be recycled by system if necessary
    private String clzName;//class name for creating a instance when fragment's cache is recycled by system.
    private boolean returnable;//true, add to back stack
    private Bundle bundle;//a copy of original data

    /**
     * Create a step record for fragment.
     * <br>Store a copy of bundle in memory cache if bundle isn't null.
     *
     * @param containerViewId containerViewId
     * @param fragment        fragment
     * @param bundle          bundle, a copy of bundle will be stored in memory cache.
     * @param returnable      true, a returnable step, otherwise false.
     */
    public BackRecord(@IdRes int containerViewId, @NonNull Fragment fragment, Bundle bundle, boolean returnable) {
        this.containerViewId = containerViewId;
        if (returnable)
            this.fragmentWeakReference = new WeakReference<>(fragment);
        this.returnable = returnable;
        this.clzName = fragment.getClass().getName();
        if (bundle == null)
            return;

        //copy data. The original data will be recycled if fragment was recycled.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            this.bundle = bundle.deepCopy();
        } else {
            this.bundle = new Bundle(bundle);
        }
    }

    @Nullable
    public Fragment getCachedFragment(){
        return fragmentWeakReference == null ? null : fragmentWeakReference.get();
    }

    @Nullable
    public Fragment createInstanceIfNecessary() {
        Fragment fragment = fragmentWeakReference.get();
        if (fragment == null) {
            try {
                Log.i(TAG, "createInstanceIfNecessary: newInstance " + (bundle == null ? "" : bundle.getString("extra_content")));
                Object object = Class.forName(clzName).newInstance();
                if (object instanceof Fragment)
                    fragment = (Fragment) object;
                if (fragment != null)
                    fragment.setArguments(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

    public int getContainerViewId() {
        return containerViewId;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
