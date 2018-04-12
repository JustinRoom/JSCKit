package jsc.kit.swiperecyclerview;

import android.view.View;

public interface OnItemLongClickListener<T> {
    boolean onItemLongClick(View view, T item);

    boolean onItemLongClick(View view, T item, int adapterPosition, int layoutPosition);
}
