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

public class ClassItemAdapter extends BaseRecyclerViewAdapter<ClassItem, ClassItemAdapter.ClassItemViewHolder> {

    public ClassItemAdapter() {
    }

    public ClassItemAdapter(int layoutId) {
        super(layoutId);
    }

    public ClassItemAdapter(int layoutId, boolean itemClickEnable, boolean itemLongClickEnable) {
        super(layoutId, itemClickEnable, itemLongClickEnable);
    }

    @Override
    public View createView(@NonNull ViewGroup parent, int viewType) {
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

    @NonNull
    @Override
    public ClassItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JSCItemLayout v = (JSCItemLayout) createView(parent, viewType);
        return new ClassItemViewHolder(v);
    }

    @Override
    public void bindViewHolder(@NonNull ClassItemViewHolder holder, int position, ClassItem item, int viewType) {
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