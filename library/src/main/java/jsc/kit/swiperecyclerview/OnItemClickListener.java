package jsc.kit.swiperecyclerview;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(View view, T item);

    void onItemClick(View view, T item, int adapterPosition, int layoutPosition);
}
