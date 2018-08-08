package jsc.exam.jsckit.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.kit.component.itemlayout.JSCItemLayout;
import jsc.kit.component.swiperecyclerview.BaseRecyclerViewAdapter;
import jsc.kit.component.utils.CompatResourceUtils;

public class ClassItemAdapter extends BaseRecyclerViewAdapter<ClassItem, ClassItemAdapter.ClassItemViewHolder, JSCItemLayout> {


    @Override
    public JSCItemLayout onCreateItemView(@NonNull ViewGroup parent, int viewType) {
        JSCItemLayout layout = new JSCItemLayout(parent.getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CompatResourceUtils.getDimensionPixelSize(parent, R.dimen.item_height)));
        layout.setBackgroundResource(R.drawable.ripple_round_corner_white_r4);
        layout.setPadding(
                CompatResourceUtils.getDimensionPixelSize(parent, R.dimen.space_8),
                0,
                CompatResourceUtils.getDimensionPixelSize(parent, R.dimen.space_8),
                0
        );
        layout.getLabelView().setPadding(
                CompatResourceUtils.getDimensionPixelSize(parent, R.dimen.space_12),
                0,
                0,
                0
        );
        return layout;
    }

    @Override
    public ClassItemViewHolder onCreateViewHolder(@NonNull JSCItemLayout itemView) {
        return new ClassItemViewHolder(itemView);
    }

    @Override
    public void onBindItem(ClassItemViewHolder holder, int position, ClassItem item) {
        holder.layout.setLabel(item.getLabel());
        holder.layout.showDotView(item.isUpdated());
    }

    static class ClassItemViewHolder extends RecyclerView.ViewHolder {

        JSCItemLayout layout;

        ClassItemViewHolder(JSCItemLayout layout) {
            super(layout);
            this.layout = layout;
        }
    }
}