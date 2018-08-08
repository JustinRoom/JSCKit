package jsc.exam.jsckit.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.kit.component.swiperecyclerview.BaseRecyclerViewAdapter;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class LayoutManagerAdapter extends BaseRecyclerViewAdapter<ClassItem, LayoutManagerAdapter.LayoutManagerViewHolder, FrameLayout> {

    public void onSwiped(int pos) {
        ClassItem item = getItems().get(pos);
        removeItem(pos);
        addItem(0, item);
    }

    @Override
    public FrameLayout onCreateItemView(@NonNull ViewGroup parent, int viewType) {
        FrameLayout layout = new FrameLayout(parent.getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setId(R.id.page_empty);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        layout.addView(imageView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        TextView textView = new TextView(parent.getContext());
        textView.setId(R.id.page_error);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextColor(Color.WHITE);
        layout.addView(textView, params);
        return layout;
    }

    @Override
    public LayoutManagerViewHolder onCreateViewHolder(@NonNull FrameLayout itemView) {
        return new LayoutManagerViewHolder(itemView);
    }

    @Override
    public void onBindItem(LayoutManagerViewHolder holder, int position, ClassItem item) {
        holder.imageView.setImageResource(R.drawable.header);
        holder.textView.setText(item.getLabel());
    }

    static class LayoutManagerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LayoutManagerViewHolder(FrameLayout layout) {
            super(layout);
            imageView = layout.findViewById(R.id.page_empty);
            textView = layout.findViewById(R.id.page_error);
        }
    }
}
