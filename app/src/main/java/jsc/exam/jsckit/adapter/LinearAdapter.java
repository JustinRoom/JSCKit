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

public class LinearAdapter extends BaseRecyclerViewAdapter<Banner, LinearAdapter.MViewHolder, JSCItemLayout> {

    @Override
    public JSCItemLayout onCreateItemView(@NonNull ViewGroup parent, int viewType) {
        int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, parent.getContext().getResources().getDisplayMetrics());
        JSCItemLayout itemLayout = new JSCItemLayout(parent.getContext());
        itemLayout.setPadding(dp16, dp16, dp16, dp16);
        itemLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itemLayout.setBackgroundResource(R.drawable.ripple_round_corner_white_r4);
        return itemLayout;
    }

    @Override
    public MViewHolder onCreateViewHolder(@NonNull JSCItemLayout itemView) {
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindItem(MViewHolder holder, int position, Banner item) {
        holder.layout.setLabel(item.getLabel() + position);
    }

    static class MViewHolder extends RecyclerView.ViewHolder {

        JSCItemLayout layout;
        MViewHolder(JSCItemLayout itemView) {
            super(itemView);
            layout = itemView;
        }
    }
}
