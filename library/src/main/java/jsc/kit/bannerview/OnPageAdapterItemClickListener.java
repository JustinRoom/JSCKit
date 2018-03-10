package jsc.kit.bannerview;

import android.view.View;

/**
 * Created on 2018/2/3.
 *
 * @author jsc
 */

public interface OnPageAdapterItemClickListener<T> {

    /**
     * item click listener.
     * @param view
     * @param item
     */
    void onPageAdapterItemClick(View view, T item);
}
