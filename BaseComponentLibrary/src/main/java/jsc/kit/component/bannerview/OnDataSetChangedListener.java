package jsc.kit.component.bannerview;

import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
interface OnDataSetChangedListener<T> {
    void onDataSetChanged(List<T> bannerItems, OnCreateIndicatorViewListener<T> createIndicatorViewListener);
}