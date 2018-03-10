package jsc.kit.bannerview;

import java.util.List;

interface OnDataSetChangedListener<T> {
    void onDataSetChanged(List<T> bannerItems, OnCreateIndicatorViewListener<T> createIndicatorViewListener);
}