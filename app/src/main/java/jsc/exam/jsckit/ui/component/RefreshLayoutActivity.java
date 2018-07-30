package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.adapter.ClassItemAdapter;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.refreshlayout.RefreshLayout;
import jsc.kit.component.swiperecyclerview.HorizontalSpaceItemDecoration;
import jsc.kit.component.utils.WindowUtils;

public class RefreshLayoutActivity extends BaseActivity {

    RefreshLayout refreshLayout;
    int lastPullState = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
        inflateRefreshLayout();
        initXmlContent();
    }

    private void initMenu() {
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 1:
                        initXmlContent();
                        break;
                    case Menu.FIRST + 2:
                        initRecyclerViewContent();
                        break;
                }
                return true;
            }
        });

        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "CustomView");
        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "RecyclerView");
    }

    private void inflateRefreshLayout(){
        refreshLayout = new RefreshLayout(this);
        refreshLayout.addHeader(R.layout.refresh_layout_header);
        setContentView(refreshLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitle(getClass().getSimpleName().replace("Activity", ""));
//        refreshLayout.setPullRatioY(0.55f);
//        refreshLayout.setPullToRefreshRatio(0.45f);
//        refreshLayout.setReleaseToRefreshRatio(0.65f);
        refreshLayout.setReboundAnimationDuration(2000);
        refreshLayout.setOnScrollListener(new RefreshLayout.OnScrollListener() {
            @Override
            public void onScroll(View headerView, int headerHeight, float pullToRefreshRatio, float releaseToRefreshRatio, int scrollY, boolean isRefreshing) {
                //从底部向上滑动，不做任何处理
                if (scrollY > 0 || isRefreshing)
                    return;
                int scrollDistance = Math.abs(scrollY);
                int tempPullState = 0;
                if (scrollDistance >= headerHeight * releaseToRefreshRatio)
                    tempPullState = 2;
                else if (scrollDistance > headerHeight * pullToRefreshRatio)
                    tempPullState = 1;
                else
                    tempPullState = 0;

                if (tempPullState == lastPullState)
                    return;

                lastPullState = tempPullState;
                TextView tvScrollTips = headerView.findViewById(R.id.tv_scroll_tips);
                switch (lastPullState) {
                    case 0:
                        tvScrollTips.setText("Surprise");
                        break;
                    case 1:
                        tvScrollTips.setText("Pull To Refresh");
                        break;
                    case 2:
                        tvScrollTips.setText("Release To Refresh");
                        break;
                }
            }
        });
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onStartRefresh(View headerView) {
                TextView tvScrollTips = headerView.findViewById(R.id.tv_scroll_tips);
                tvScrollTips.setText("Refreshing...");
                headerView.findViewById(R.id.loading_view).setVisibility(View.VISIBLE);
            }

            @Override
            public void onRefresh() {
                // TODO:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onEndRefresh(View headerView) {
                TextView tvScrollTips = headerView.findViewById(R.id.tv_scroll_tips);
                tvScrollTips.setText("Refresh Successfully.");
                headerView.findViewById(R.id.loading_view).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initXmlContent(){
        refreshLayout.addContent(R.layout.refresh_layout_content);
    }

    private void initRecyclerViewContent(){
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.space_16),
                getResources().getDimensionPixelOffset(R.dimen.space_4),
                getResources().getDimensionPixelOffset(R.dimen.space_16),
                0
        ));
        ClassItemAdapter adapter = new ClassItemAdapter();
        recyclerView.setAdapter(adapter);
        refreshLayout.addContent(recyclerView);

        List<ClassItem> items = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            items.add(new ClassItem("Item " + (i < 10 ? "0" + i : "" + i), null));
        }
        adapter.setItems(items);
    }
}
