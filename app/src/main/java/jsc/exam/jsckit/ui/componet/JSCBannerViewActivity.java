package jsc.exam.jsckit.ui.componet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.Banner;
import jsc.exam.jsckit.ui.ABaseActivity;
import jsc.kit.archeaderview.LGradientArcHeaderView;
import jsc.kit.bannerview.BannerPagerAdapter;
import jsc.kit.bannerview.JSCBannerView;
import jsc.kit.bannerview.OnCreateIndicatorViewListener;
import jsc.kit.bannerview.OnPageAdapterItemClickListener;
import jsc.kit.bannerview.PageAdapterItemLifeCycle;
import jsc.kit.bannerview.pageTransformer.ScaleTransformer;

public class JSCBannerViewActivity extends ABaseActivity {

    JSCBannerView jscBannerView;
    private boolean changeValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsc_banner_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        jscBannerView = findViewById(R.id.banner_view);
        LGradientArcHeaderView backgroundView = new LGradientArcHeaderView(this);
        backgroundView.setArcHeight(100);
        backgroundView.setColors(0xFF00BA86, 0x2200BA86);
        jscBannerView.setBackgroundView(backgroundView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        example1();
    }

    private void example1() {
        jscBannerView.getChildAt(0).setVisibility(View.VISIBLE);

        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner("img/1.jpg"));
        banners.add(new Banner("img/2.jpg"));
        banners.add(new Banner("img/3.jpg"));
        banners.add(new Banner("img/4.jpg"));
        banners.add(new Banner("img/5.jpg"));
        banners.add(new Banner("img/6.jpg"));

        BannerPagerAdapter<Banner> adapter = new BannerPagerAdapter<>(true);
        adapter.setOnPageAdapterItemClickListener(new OnPageAdapterItemClickListener<Banner>() {
            @Override
            public void onPageAdapterItemClick(View view, Banner item) {
                Toast.makeText(view.getContext(), item.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setPageAdapterItemLifeCycle(new PageAdapterItemLifeCycle<Banner>() {
            @NonNull
            @Override
            public View onInstantiateItem(ViewGroup container, Banner item) {
                FrameLayout layout = new FrameLayout(container.getContext());
                TextView textView = new TextView(container.getContext());
                textView.setText(item.getUrl());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                layout.addView(textView, params);
                return layout;
            }

            @Override
            public boolean onDestroyItem(ViewGroup container, Object object) {
                return false;
            }
        });
        adapter.setOnCreateIndicatorViewListener(new OnCreateIndicatorViewListener<Banner>() {
            @Override
            public View onCreateIndicatorView(Context context, int index, Banner item) {
                return null;
            }
        });
        jscBannerView.setAdapter(adapter);
        adapter.setBannerItems(banners);
    }

    private void example2() {
        jscBannerView.getChildAt(0).setVisibility(View.INVISIBLE);

        jscBannerView.setClipChildren(false);
        jscBannerView.getViewPager().setClipChildren(false);
//        jscBannerView.getViewPager().setPageMargin(20);//设置ViewPager页间距
        jscBannerView.getViewPager().setOffscreenPageLimit(3);//设置预加载的页数
        jscBannerView.setPageTransformer(true, new ScaleTransformer());

        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner("img/1.jpg"));
        banners.add(new Banner("img/2.jpg"));
        banners.add(new Banner("img/3.jpg"));
        banners.add(new Banner("img/4.jpg"));
        banners.add(new Banner("img/5.jpg"));
        banners.add(new Banner("img/6.jpg"));

        BannerPagerAdapter<Banner> adapter = new BannerPagerAdapter<>(true);
        adapter.setOnPageAdapterItemClickListener(new OnPageAdapterItemClickListener<Banner>() {
            @Override
            public void onPageAdapterItemClick(View view, Banner item) {
                Toast.makeText(view.getContext(), item.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setPageAdapterItemLifeCycle(new PageAdapterItemLifeCycle<Banner>() {
            @NonNull
            @Override
            public View onInstantiateItem(ViewGroup container, Banner item) {
                FrameLayout layout = new FrameLayout(container.getContext());
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                layout.addView(imageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                Glide.with(container.getContext())
                        .load("file:///android_asset/" + item.getUrl())
                        .into(imageView);
                return layout;
            }

            @Override
            public boolean onDestroyItem(ViewGroup container, Object object) {
                return false;
            }
        });
        adapter.setOnCreateIndicatorViewListener(new OnCreateIndicatorViewListener<Banner>() {
            @Override
            public View onCreateIndicatorView(Context context, int index, Banner item) {
                return null;
            }
        });
        jscBannerView.setAdapter(adapter);
        adapter.setBannerItems(banners);
    }

    public void widgetClick(View view) {
        changeValue = !changeValue;
        if (changeValue)
            example2();
        else
            example1();
    }
}
