package jsc.kit.component.bannerview;

import android.view.View;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public interface OnPageAdapterItemClickListener<T> {

    /**
     * item click listener.
     * @param view the target view
     * @param item the bind entity of target view
     */
    void onPageAdapterItemClick(View view, T item);
}
