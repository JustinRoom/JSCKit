package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.adapter.LinearAdapter;
import jsc.exam.jsckit.entity.Banner;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.swiperecyclerview.BlankSpaceItemDecoration;
import jsc.kit.component.swiperecyclerview.OnItemClickListener;
import jsc.kit.component.swiperecyclerview.OnItemLongClickListener;
import jsc.kit.component.swiperecyclerview.SwipeRefreshRecyclerView;
import jsc.kit.component.utils.CompatResourceUtils;

public class SwipeRecyclerViewActivity extends BaseActivity {

    SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    LinearAdapter adapter;
    int pageSize = 24;//一页最多可显示的数据数
    int pageIndex = 1;//页码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeRefreshRecyclerView = new SwipeRefreshRecyclerView(this);
        //添加LayoutManager
//        swipeRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
//        swipeRefreshRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        //添加ItemDecoration
//        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        decoration.setDrawable(CompatResourceUtils.getDrawable(this, R.drawable.item_decoration_shape));
        swipeRefreshRecyclerView.getRecyclerView().addItemDecoration(new BlankSpaceItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.space_4),
                getResources().getDimensionPixelOffset(R.dimen.space_4),
                getResources().getDimensionPixelOffset(R.dimen.space_4),
                0
        ));
        setContentView(swipeRefreshRecyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        initMenu();

        //
        swipeRefreshRecyclerView.getSwipeRefreshLayout().setColorSchemeColors(0xFF3F51B5, 0xFF303F9F, 0xFFFF4081, Color.CYAN);
        //设置自定义的emptyView
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = topMargin;
        swipeRefreshRecyclerView.setEmptyView(createEmptyView(), params);
        //设置自定义的loadMoreView
        swipeRefreshRecyclerView.setLoadMoreView(createLoadMoreView());
        //添加刷新或者加载更多监听
        swipeRefreshRecyclerView.setCustomRefreshListener(new SwipeRefreshRecyclerView.OnCustomRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                //模拟请求网络数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshRecyclerView.refreshComplete();
                        List<Banner> banners = getRandomBanners();
                        adapter.setItems(banners);
                        //是否还有下一页数据
                        swipeRefreshRecyclerView.setHaveMore(banners.size() >= pageSize);
                        //是否显示empty view
                        swipeRefreshRecyclerView.showEmptyViewIfNecessary();
                    }
                }, 3000);

            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                //模拟请求网络数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshRecyclerView.loadMoreComplete();
                        List<Banner> banners = getRandomBanners();
                        adapter.addItems(banners);
                        //是否还有下一页数据
                        swipeRefreshRecyclerView.setHaveMore(banners.size() >= pageSize);
                    }
                }, 3000);
            }
        });
        //自定义“加载更多” 的动画(或逻辑)。请参照SwipeRefreshRecyclerView默认的动画(或逻辑)：showScrollUpAnim()、showScrollDownAnim()
        //如果只用SwipeRefreshRecyclerView默认的动画(或逻辑)。不设置OnCustomLoadMoreAnimationListener或者设置为null即可。
/*
        swipeRefreshRecyclerView.setCustomLoadMoreAnimationListener(new SwipeRefreshRecyclerView.OnCustomLoadMoreAnimationListener() {
            @Override
            public void onLoadMoreStartAnim(@NonNull final SwipeRefreshRecyclerView swipeRefreshRecyclerView, final SwipeRefreshRecyclerView.OnCustomRefreshListener refreshListener) {
                final SwipeRefreshLayout layout = swipeRefreshRecyclerView.getSwipeRefreshLayout();
                final View loadMoreView = swipeRefreshRecyclerView.getLoadMoreView();
                // TODO: 4/9/2018
            }

            @Override
            public void onLoadMoreCompleteAnim(@NonNull final SwipeRefreshRecyclerView swipeRefreshRecyclerView, final SwipeRefreshRecyclerView.OnCustomRefreshListener refreshListener) {
                final SwipeRefreshLayout layout = swipeRefreshRecyclerView.getSwipeRefreshLayout();
                final View loadMoreView = swipeRefreshRecyclerView.getLoadMoreView();
                // TODO: 4/9/2018
            }
        });
*/
        adapter = new LinearAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener<Banner>() {
            @Override
            public void onItemClick(View view, Banner item) {
                Toast.makeText(view.getContext(), item.getLabel(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(View view, Banner item, int adapterPosition, int layoutPosition) {

            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener<Banner>() {
            @Override
            public boolean onItemLongClick(View view, Banner item) {
                return false;
            }

            @Override
            public boolean onItemLongClick(View view, Banner item, int adapterPosition, int layoutPosition) {
                return false;
            }
        });
        //设置适配器
        swipeRefreshRecyclerView.setAdapter(adapter);
        //300毫秒后自动刷新
        swipeRefreshRecyclerView.refreshDelay(300);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initMenu() {
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clear();
                return true;
            }
        });

        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "CLEAR").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private List<Banner> getRandomBanners() {
        List<Banner> banners = new ArrayList<>();
        int itemCount = 20 + new Random().nextInt(8);
        for (int i = 0; i < itemCount; i++) {
            banners.add(new Banner("Banner item ", ""));
        }
        return banners;
    }

    private View createEmptyView() {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        //
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.tiger);
        layout.addView(imageView, new LinearLayout.LayoutParams(size, size));

        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText("No data.");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        layout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }

    private View createLoadMoreView() {
        int dp4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(0, dp4 * 2, 0, dp4 * 2);
        layout.setBackgroundColor(Color.BLACK);

        AVLoadingIndicatorView indicatorView = new AVLoadingIndicatorView(this);
        indicatorView.setIndicatorColor(0xFFFF4081);
        indicatorView.setIndicator(new BallSpinFadeLoaderIndicator());
        layout.addView(indicatorView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(this);
        textView.setTextColor(Color.WHITE);
        textView.setText("loading more...");
        layout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }

    public void clear() {
        pageIndex = 1;
        adapter.setItems(new ArrayList<Banner>());
        swipeRefreshRecyclerView.reset();
        swipeRefreshRecyclerView.showEmptyViewIfNecessary();
    }
}
