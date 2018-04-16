package jsc.exam.jsckit.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.kit.itemlayout.JSCItemLayout;
import jsc.kit.swiperecyclerview.BaseRecyclerViewAdapter;

public class ClassItemAdapter extends BaseRecyclerViewAdapter<ClassItem, ClassItemAdapter.ClassItemViewHolder> {

    @NonNull
    @Override
    public ClassItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item_layout, parent, false);
        return new ClassItemViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassItemViewHolder holder, int position) {
        JSCItemLayout layout = (JSCItemLayout) holder.itemView;
        layout.setLabel(getItemAtPosition(position).getLabel());
    }

    class ClassItemViewHolder extends RecyclerView.ViewHolder {

        public ClassItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null)
                        getOnItemClickListener().onItemClick(v, getItemAtPosition(getAdapterPosition()));
                }
            });
        }
    }
}
