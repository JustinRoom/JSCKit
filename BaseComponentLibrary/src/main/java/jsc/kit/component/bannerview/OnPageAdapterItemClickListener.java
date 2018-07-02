package jsc.kit.component.bannerview;

import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface OnPageAdapterItemClickListener<T> {

    /**
     * item click listener.
     *
     * @param view the target view
     * @param item the bind entity of target view
     */
    void onPageAdapterItemClick(View view, T item);
}
