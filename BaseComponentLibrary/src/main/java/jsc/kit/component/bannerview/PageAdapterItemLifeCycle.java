package jsc.kit.component.bannerview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface PageAdapterItemLifeCycle<T> {
    /**
     * create your banner view.
     *
     * @param container parent container
     * @param item      entity
     * @return item view
     */
    @NonNull
    View onInstantiateItem(ViewGroup container, T item);

    /**
     * destroy banner view.
     *
     * @param container parent container
     * @param object    object
     * @return true-destroy item by your way
     * <br>false-destroy item by default way: {@code container.removeView((View) object);}
     */
    boolean onDestroyItem(ViewGroup container, Object object);
}
