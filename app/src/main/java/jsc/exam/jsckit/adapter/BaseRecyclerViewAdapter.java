package jsc.exam.jsckit.adapter;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.entity.Banner;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public List<T> items = new ArrayList<>();

    public void setItems(List<T> items) {
        this.items.clear();
        if (items != null)
            this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items){
        if (items != null && !items.isEmpty()){
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItem(T item){
        if (items != null){
            this.items.add(item);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItemAtPosition(int position){
        return items.get(position);
    }
}
