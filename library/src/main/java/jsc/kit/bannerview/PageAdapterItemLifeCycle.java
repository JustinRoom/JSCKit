package jsc.kit.bannerview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2018/2/3.
 *
 * @author jsc
 */

public interface PageAdapterItemLifeCycle<T> {
    /**
     * create your banner view.
     *
     * @param container
     * @param item
     * @return
     */
    @NonNull
    View onInstantiateItem(ViewGroup container, T item);

    /**
     * destroy banner view.The below codes would be added usually.<br>
     * <code>
     * container.removeView((View) object);
     * </code>
     *
     * @param container
     * @param object
     */
    void onDestroyItem(ViewGroup container, Object object);
}
