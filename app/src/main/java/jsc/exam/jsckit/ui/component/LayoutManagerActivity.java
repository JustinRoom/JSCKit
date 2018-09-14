package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.adapter.LayoutManagerAdapter;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.swiperecyclerview.manager.OverLayCardLayoutManager;
import jsc.kit.component.widget.AspectRatioFrameLayout;

public class LayoutManagerActivity extends BaseActivity {

    LayoutManagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int margin = getResources().getDimensionPixelOffset(R.dimen.space_16);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.leftMargin = margin;
        params.rightMargin = margin;
        params.topMargin = margin;
        AspectRatioFrameLayout frameLayout = new AspectRatioFrameLayout(this);
        frameLayout.setxAspect(4);
        frameLayout.setyAspect(3);
        setContentView(frameLayout, params);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        //
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new OverLayCardLayoutManager(5,margin, margin));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LayoutManagerAdapter();
        frameLayout.addView(recyclerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setAdapter(adapter);
        List<ClassItem> items = new ArrayList<>();
        int count = 5;
        for (int i = 0; i < count; i++) {
            items.add(new ClassItem((count-i) + "", null));
        }
        adapter.setItems(items);

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 拖拽的标记，这里允许上下左右四个方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                        ItemTouchHelper.RIGHT;
                // 滑动的标记，这里允许左右滑动
                int swipeFlags = ItemTouchHelper.END | ItemTouchHelper.DOWN | ItemTouchHelper.UP;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 移动时更改列表中对应的位置并返回true
                Collections.swap(adapter.getItems(), viewHolder.getAdapterPosition(), target
                        .getAdapterPosition());
                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                // 移动完成后刷新列表
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target
                        .getAdapterPosition());
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.onSwiped(viewHolder.getLayoutPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }
}
