package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.ABaseActivity;
import jsc.kit.component.refreshlayout.RefreshLayout;

public class RefreshLayoutActivity extends ABaseActivity {

    RefreshLayout refreshLayout;
    int lastPullState = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        inflateRefreshLayout();

    }


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        FrameLayout customView = new FrameLayout(this);
        customView.setPadding(padding, 0, 0, 0);
        ActionBar.LayoutParams barParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActionBarSize());
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customView, barParams);
        //
        TextView tvTitle = new TextView(this);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvTitle.setText(getClass().getSimpleName().replace("Activity", ""));
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        customView.addView(tvTitle, titleParams);

        ActionMenuView actionMenuView = new ActionMenuView(this);
        FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        customView.addView(actionMenuView, menuParams);
        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 1:
                        break;
                    case Menu.FIRST + 2:
                        break;
                    case Menu.FIRST + 3:
                        break;
                }
                return true;
            }
        });

        actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Xml");
        actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "ListView");
        actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 3, Menu.NONE, "TextView");
    }

    private void inflateRefreshLayout(){
        setContentView(R.layout.activity_refresh_layout);
        setTitle(getClass().getSimpleName().replace("Activity", ""));
        refreshLayout = findViewById(R.id.refresh_layout);
//        refreshLayout.setPullRatioY(0.55f);
//        refreshLayout.setPullToRefreshRatio(0.45f);
//        refreshLayout.setReleaseToRefreshRatio(0.65f);
        refreshLayout.setReboundAnimationDuration(800);
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
                }, 4000);
            }

            @Override
            public void onEndRefresh(View headerView) {
                TextView tvScrollTips = headerView.findViewById(R.id.tv_scroll_tips);
                tvScrollTips.setText("Refresh Successfully.");
                headerView.findViewById(R.id.loading_view).setVisibility(View.INVISIBLE);
            }
        });
    }
}
