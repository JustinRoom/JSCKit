package jsc.kit.component.bannerview;

import java.util.List;
/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
interface OnDataSetChangedListener<T> {
    void onDataSetChanged(List<T> bannerItems, OnCreateIndicatorViewListener<T> createIndicatorViewListener);
}