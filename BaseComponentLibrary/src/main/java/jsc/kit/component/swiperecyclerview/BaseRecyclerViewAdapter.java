package jsc.kit.component.swiperecyclerview;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder, V extends View> extends RecyclerView.Adapter<VH> {

    private List<T> items = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener = null;
    private OnItemLongClickListener<T> onItemLongClickListener = null;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, index, getItemAtPosition(index));
            }
        }
    };
    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int index = (int) v.getTag();
            if (onItemLongClickListener != null)
                return onItemLongClickListener.onItemLongClick(v, index, getItemAtPosition(index));
            return false;
        }
    };

    public BaseRecyclerViewAdapter() {

    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

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
        int startPosition = getItemCount();
        addItems(startPosition, items);
    }

    public void addItems(int startPosition, List<T> items) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(startPosition, items);
            notifyItemRangeInserted(startPosition, items.size());
        }
    }

    public void addItem(T item) {
        addItem(getItemCount(), item);
    }

    public void addItem(int position, T item) {
        if (items != null) {
            this.items.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    protected void onBindListener(V itemView) {
        if (itemView != null){
            itemView.setOnClickListener(clickListener);
            itemView.setOnLongClickListener(longClickListener);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        V itemView = onCreateItemView(parent, viewType);
        onBindListener(itemView);
        return onCreateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.itemView.setTag(position);
        onBindItem(holder, position, getItemAtPosition(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItemAtPosition(int position) {
        return items.get(position);
    }

    public abstract V onCreateItemView(@NonNull ViewGroup parent, int viewType);

    public abstract VH onCreateViewHolder(@NonNull V itemView);

    public abstract void onBindItem(VH holder, int position, T item);
}
