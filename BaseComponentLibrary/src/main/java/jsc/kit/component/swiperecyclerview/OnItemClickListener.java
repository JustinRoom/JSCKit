package jsc.kit.component.swiperecyclerview;

import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, T item);

    void onItemClick(View view, T item, int adapterPosition, int layoutPosition);
}
