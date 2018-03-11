package jsc.kit.bannerview;

import android.view.View;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public interface OnPageAdapterItemClickListener<T> {

    /**
     * item click listener.
     * @param view
     * @param item
     */
    void onPageAdapterItemClick(View view, T item);
}
