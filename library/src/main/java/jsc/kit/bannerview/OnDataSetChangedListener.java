package jsc.kit.bannerview;

import java.util.List;
/**
 * Email:1006368252@qq.com
 * QQ:1006368252
 *
 * @author jiangshicheng
 */
interface OnDataSetChangedListener<T> {
    void onDataSetChanged(List<T> bannerItems, OnCreateIndicatorViewListener<T> createIndicatorViewListener);
}