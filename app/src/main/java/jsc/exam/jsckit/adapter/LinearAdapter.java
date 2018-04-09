package jsc.exam.jsckit.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.Banner;
import jsc.kit.itemlayout.JSCItemLayout;

public class LinearAdapter extends BaseRecyclerViewAdapter<Banner, LinearAdapter.MViewHolder> {

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, parent.getContext().getResources().getDisplayMetrics());
        JSCItemLayout itemLayout = new JSCItemLayout(parent.getContext());
        itemLayout.setPadding(dp16, dp16, dp16, dp16);
        itemLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itemLayout.setBackgroundResource(R.drawable.ripple_round_corner_white_r4);
        return new MViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        JSCItemLayout layout = (JSCItemLayout) holder.itemView;
        layout.setLabel(getItemAtPosition(position).getLabel() + position);
    }

    class MViewHolder extends RecyclerView.ViewHolder {

        public MViewHolder(JSCItemLayout itemView) {
            super(itemView);
        }
    }
}
