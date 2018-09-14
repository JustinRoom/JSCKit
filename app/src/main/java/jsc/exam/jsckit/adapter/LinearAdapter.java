package jsc.exam.jsckit.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.Banner;
import jsc.kit.component.itemlayout.JSCItemLayout;
import jsc.kit.component.swiperecyclerview.BaseRecyclerViewAdapter;
import jsc.kit.component.swiperecyclerview.BaseViewHolder;

public class LinearAdapter extends BaseRecyclerViewAdapter<Banner, LinearAdapter.MViewHolder> {

    public LinearAdapter() {
    }

    public LinearAdapter(int layoutId) {
        super(layoutId);
    }

    public LinearAdapter(int layoutId, boolean itemClickEnable, boolean itemLongClickEnable) {
        super(layoutId, itemClickEnable, itemLongClickEnable);
    }

    @Override
    public View createView(@NonNull ViewGroup parent, int viewType) {
        int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, parent.getContext().getResources().getDisplayMetrics());
        JSCItemLayout itemLayout = new JSCItemLayout(parent.getContext());
        itemLayout.setPadding(dp16, dp16, dp16, dp16);
        itemLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itemLayout.setBackgroundResource(R.drawable.ripple_round_corner_white_r4);
        return itemLayout;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JSCItemLayout v = (JSCItemLayout) createView(parent, viewType);
        return new MViewHolder(v);
    }

    @Override
    public void bindViewHolder(@NonNull MViewHolder holder, int position, Banner item, int viewType) {
        holder.layout.setLabel(item.getLabel() + position);
    }

    static class MViewHolder extends BaseViewHolder {

        JSCItemLayout layout;
        MViewHolder(JSCItemLayout itemView) {
            super(itemView);
            layout = itemView;
        }
    }
}
