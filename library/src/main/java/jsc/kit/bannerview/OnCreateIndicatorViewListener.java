package jsc.kit.bannerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Email:1006368252@qq.com
 * QQ:1006368252
 *
 * @author jiangshicheng
 */
public interface OnCreateIndicatorViewListener<T> {
    /**
     * Here you can create your own indicator item view at different index.<br>
     * You are recommended to use {@link android.widget.LinearLayout.LayoutParams} if necessary.<br>
     * For example:
     * <p>
     * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);<br>
     * params.leftMargin = 10;<br>
     * params.rightMargin = 10;<br>
     * View view = new View(context);<br>
     * view.setLayoutParams(params);<br>
     * return view;
     *
     *
     * @param context
     * @param index
     * @param item
     * @return
     */
    @Nullable
    View onCreateIndicatorView(Context context, int index, T item);
}
