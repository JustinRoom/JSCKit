package jsc.kit.component.swiperecyclerview;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> items = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener = null;
    private OnItemLongClickListener<T> onItemLongClickListener = null;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items.clear();
        if (items != null)
            this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItem(T item) {
        if (items != null) {
            this.items.add(item);
            notifyDataSetChanged();
        }
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public OnItemLongClickListener<T> getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItemAtPosition(int position) {
        return items.get(position);
    }
}
