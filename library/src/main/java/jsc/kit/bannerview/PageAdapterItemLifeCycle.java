package jsc.kit.bannerview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
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
