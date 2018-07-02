package jsc.kit.component.bannerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
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
     * @param context context
     * @param index   index
     * @param item    entity
     * @return indicator view at index
     */
    @Nullable
    View onCreateIndicatorView(Context context, int index, T item);
}
