package jsc.exam.jsckit.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jsc.exam.jsckit.adapter.ComponentItemAdapter;
import jsc.exam.jsckit.adapter.ComponentItemDecoration;
import jsc.exam.jsckit.entity.ComponentItem;
import jsc.exam.jsckit.ui.component.AdvertisementViewActivity;
import jsc.exam.jsckit.ui.component.ArcHeaderViewActivity;
import jsc.exam.jsckit.ui.component.AutoSizeTextViewActivity;
import jsc.exam.jsckit.ui.component.AverageLayoutActivity;
import jsc.exam.jsckit.ui.component.CameraMaskActivity;
import jsc.exam.jsckit.ui.component.JSCBannerViewActivity;
import jsc.exam.jsckit.ui.component.JSCItemLayoutActivity;
import jsc.exam.jsckit.ui.component.JSCRoundCornerProgressBarActivity;
import jsc.exam.jsckit.ui.component.LayoutManagerActivity;
import jsc.exam.jsckit.ui.component.MonthViewActivity;
import jsc.exam.jsckit.ui.component.ReboundRecyclerViewActivity;
import jsc.exam.jsckit.ui.component.RadarViewActivity;
import jsc.exam.jsckit.ui.component.ReboundFrameLayoutActivity;
import jsc.exam.jsckit.ui.component.RefreshLayoutActivity;
import jsc.exam.jsckit.ui.component.RippleViewActivity;
import jsc.exam.jsckit.ui.component.ScannerCameraMaskActivity;
import jsc.exam.jsckit.ui.component.SwipeRecyclerViewActivity;
import jsc.exam.jsckit.ui.component.TurntableViewActivity;
import jsc.exam.jsckit.ui.component.VScrollScreenLayoutActivity;
import jsc.exam.jsckit.ui.component.VerticalColumnarGraphViewActivity;
import jsc.exam.jsckit.ui.component.VerticalStepLinearLayoutActivity;
import jsc.exam.jsckit.ui.component.VerticalStepViewActivity;
import jsc.exam.jsckit.ui.fragment.DefaultFragment;
import jsc.exam.jsckit.ui.fragment.LineChartViewFragment;
import jsc.kit.component.baseui.transition.TransitionEnum;
import jsc.kit.component.baseui.transition.TransitionProvider;
import jsc.kit.component.reboundlayout.ReboundRecyclerView;
import jsc.kit.component.swiperecyclerview.BaseRecyclerViewAdapter;
import jsc.kit.component.swiperecyclerview.OnItemClickListener;
import jsc.kit.component.utils.dynamicdrawable.DynamicDrawableFactory;

public class ComponentsActivity extends BaseActivity {

    @Override
    public Transition createExitTransition() {
        return TransitionProvider.createFade(300L);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Transition createReturnTransition() {
        Slide slide = TransitionProvider.createSlide(300L);
        slide.setSlideEdge(Gravity.START);
        return slide;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Transition createReenterTransition() {
        Slide slide = TransitionProvider.createSlide(300L);
        slide.setSlideEdge(Gravity.BOTTOM);
        return slide;
    }

    @Override
    public void initComponent() {
        super.initComponent();
        getWindow().setBackgroundDrawable(DynamicDrawableFactory.cornerRectangleDrawable(0xFF303F9F, 0));
    }

    ComponentItemAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReboundRecyclerView reboundRecyclerView = new ReboundRecyclerView(this);
//        reboundRecyclerView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        RecyclerView recyclerView = reboundRecyclerView.getRecyclerView();
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new ComponentItemDecoration(this));
        setContentView(reboundRecyclerView);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        adapter = new ComponentItemAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ComponentItem>() {
            @Override
            public void onItemClick(View itemView, int position, ComponentItem item, int viewType) {
                toNewActivity(item);
            }
        });
        List<ComponentItem> items = getComponentItems();
//        Collections.sort(items, new Comparator<ComponentItem>() {
//            @Override
//            public int compare(ComponentItem o1, ComponentItem o2) {
//                if (o1.isUpdated() && !o2.isUpdated())
//                    return -1;
//                else if (!o1.isUpdated() && o2.isUpdated()){
//                    return 1;
//                }
//                return 0;
//            }
//        });
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
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, 0);
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
                // 将数据集中的数据移除
//                adapter.getItems().remove(viewHolder.getAdapterPosition());
                // 刷新列表
//                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void toNewActivity(ComponentItem item){
        if (item.getCls().getSimpleName().equals(EmptyFragmentActivity.class.getSimpleName())){
            Bundle bundle = new Bundle();
//            bundle.putString(DefaultFragment.EXTRA_CONTENT, "empty activity with fragment");
            bundle.putString(EmptyFragmentActivity.EXTRA_TITLE, item.getLabel());
            bundle.putBoolean(EmptyFragmentActivity.EXTRA_FULL_SCREEN, false);
            bundle.putString(EmptyFragmentActivity.EXTRA_FRAGMENT_CLASS_NAME, item.getFragmentClassName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bundle.putString("transition", TransitionEnum.SLIDE.getLabel());
                EmptyFragmentActivity.launchTransition(this, bundle, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                EmptyFragmentActivity.launch(this, bundle);
            }
        } else {
            Intent mIntent = new Intent();
            mIntent.setClass(this, item.getCls());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIntent.putExtra("transition", TransitionEnum.SLIDE.getLabel());
                startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(mIntent);
            }
        }
    }

    private List<ComponentItem> getComponentItems() {
        List<ComponentItem> classItems = new ArrayList<>();
        classItems.add(new ComponentItem("AverageLayout", AverageLayoutActivity.class, true));
        classItems.add(new ComponentItem("LineChartView", EmptyFragmentActivity.class, true, LineChartViewFragment.class.getName()));
        classItems.add(new ComponentItem("AutoSizeTextView", AutoSizeTextViewActivity.class));
        classItems.add(new ComponentItem("VerticalStep\nLinearLayout", VerticalStepLinearLayoutActivity.class));
        classItems.add(new ComponentItem("LayoutManager", LayoutManagerActivity.class));
        classItems.add(new ComponentItem("VerticalColumnar\nGraphView", VerticalColumnarGraphViewActivity.class));
        classItems.add(new ComponentItem("CameraMask", CameraMaskActivity.class));
        classItems.add(new ComponentItem("ScannerCamera\nMask", ScannerCameraMaskActivity.class));
        classItems.add(new ComponentItem("Rebound\nFrameLayout", ReboundFrameLayoutActivity.class));
        classItems.add(new ComponentItem("Rebound\nRecyclerView", ReboundRecyclerViewActivity.class));
        classItems.add(new ComponentItem("RefreshLayout", RefreshLayoutActivity.class));
        classItems.add(new ComponentItem("SwipeRecycler\nView", SwipeRecyclerViewActivity.class));
        classItems.add(new ComponentItem("TurntableView", TurntableViewActivity.class));
        classItems.add(new ComponentItem("RippleView", RippleViewActivity.class));
        classItems.add(new ComponentItem("RadarView", RadarViewActivity.class));
        classItems.add(new ComponentItem("JSCBannerView", JSCBannerViewActivity.class));
        classItems.add(new ComponentItem("ArcHeaderView", ArcHeaderViewActivity.class));
        classItems.add(new ComponentItem("MonthView", MonthViewActivity.class));
        classItems.add(new ComponentItem("VerticalStepView", VerticalStepViewActivity.class));
        classItems.add(new ComponentItem("JSCRoundCorner\nProgressBar", JSCRoundCornerProgressBarActivity.class));
        classItems.add(new ComponentItem("JSCItemLayout", JSCItemLayoutActivity.class));
        classItems.add(new ComponentItem("VScrollScreen\nLayout", VScrollScreenLayoutActivity.class));
        classItems.add(new ComponentItem("Advertisement\nView", AdvertisementViewActivity.class));
        return classItems;
    }
}
