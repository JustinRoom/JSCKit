package jsc.exam.jsckit.adapter;


import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.ComponentItem;
import jsc.kit.component.swiperecyclerview.BaseRecyclerViewAdapter;
import jsc.kit.component.utils.WindowUtils;
import jsc.kit.component.utils.dynamicdrawable.DynamicDrawableFactory;
import jsc.kit.component.widget.AspectRatioFrameLayout;

public class ComponentItemAdapter extends BaseRecyclerViewAdapter<ComponentItem, ComponentItemAdapter.ComponentItemViewHolder> {

    @NonNull
    @Override
    public ComponentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int radius = parent.getResources().getDimensionPixelSize(R.dimen.space_4);
        AspectRatioFrameLayout aspectRatioLinearLayout = new AspectRatioFrameLayout(parent.getContext());
        aspectRatioLinearLayout.setBaseWhat(0);
        aspectRatioLinearLayout.setxAspect(1);
        aspectRatioLinearLayout.setyAspect(1);
        aspectRatioLinearLayout.setPadding(12, 12, 12, 12);
        View backgroundView = new View(parent.getContext());
        backgroundView.setBackground(WindowUtils.getSelectableItemBackgroundBorderless(parent.getContext()));
        aspectRatioLinearLayout.addView(backgroundView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        aspectRatioLinearLayout.setBackground(DynamicDrawableFactory.cornerRectangleDrawable(0xFF3F51B5, radius));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            aspectRatioLinearLayout.setElevation(radius);
        }
        aspectRatioLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

        return new ComponentItemViewHolder(aspectRatioLinearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentItemViewHolder holder, int position) {
        String label = getItemAtPosition(position).getLabel();
        holder.tvShortName.setText(label.substring(0, 1).toUpperCase());
        holder.tvComponentName.setText(label);
    }

    class ComponentItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvShortName;
        TextView tvComponentName;

        public ComponentItemViewHolder(AspectRatioFrameLayout itemView) {
            super(itemView);
            int size = itemView.getResources().getDimensionPixelSize(R.dimen.space_56);
            FrameLayout.LayoutParams p1 = new FrameLayout.LayoutParams(size, size);
            p1.gravity = Gravity.CENTER_HORIZONTAL;
            p1.topMargin = 16;
            tvShortName = new TextView(itemView.getContext());
            tvShortName.setGravity(Gravity.CENTER);
            tvShortName.setTextColor(0xFF333333);
            tvShortName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvShortName.getPaint().setFakeBoldText(true);
            initViewShapeProvider(tvShortName);
            tvShortName.setBackgroundColor(0xFFF2F2F2);
            itemView.addView(tvShortName, p1);
            //
            FrameLayout.LayoutParams p2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p2.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            p2.bottomMargin = 4;
            tvComponentName = new TextView(itemView.getContext());
            tvComponentName.setTextColor(Color.WHITE);
            tvComponentName.setGravity(Gravity.CENTER_HORIZONTAL);
            tvComponentName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvComponentName.setLines(2);
            itemView.addView(tvComponentName, p2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null)
                        getOnItemClickListener().onItemClick(v, getItemAtPosition(getAdapterPosition()));
                }
            });
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void initViewShapeProvider(View targetView) {
            targetView.setClipToOutline(true);
            targetView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        }
    }
}
