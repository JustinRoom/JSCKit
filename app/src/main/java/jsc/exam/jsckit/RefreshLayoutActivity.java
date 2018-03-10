package jsc.exam.jsckit;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import jsc.kit.refreshlayout.RefreshLayout;

public class RefreshLayoutActivity extends AppCompatActivity {

    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_layout);

        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnScrollListener(new RefreshLayout.OnScrollListener() {
            @Override
            public void onScroll(View headerView, int headerHeight, float pullToRefreshRatio, float releaseToRefreshRatio, int scrollY) {
                if (scrollY > 0)
                    return;
                TextView tvScrollTips = headerView.findViewById(R.id.tv_scroll_tips);
                if (Math.abs(scrollY) >= headerHeight * releaseToRefreshRatio)
                    tvScrollTips.setText("Release To Refresh");
                else if (Math.abs(scrollY) >= headerHeight * pullToRefreshRatio)
                    tvScrollTips.setText("Pull To Refresh");
                else
                    tvScrollTips.setText("Surprise");
            }
        });
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
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
        });
    }
}
