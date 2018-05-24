package jsc.kit.component.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import jsc.kit.component.R;

/**
 * <p>如果开启循环滑动效果，data长度至少为3.</p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class JSCBannerView extends FrameLayout {
    private final int MSG_NEXT_ITEM = 0x9988;
    private ViewPager viewPager;
    private FrameLayout indicatorContainerParent;
    private LinearLayout indicatorContainer;
    private View backgroundView;

    private int indicatorPaddingLeft;
    private int indicatorPaddingRight;
    private int indicatorPaddingTop;
    private int indicatorPaddingBottom;
    private int indicatorAlign;
    private int viewPagerLeftMargin;
    private int viewPagerRightMargin;

    private int curPosition = 0;//仅代表滑动到的位置，它的值可能大于data的size。
    private long AUTO_SCROLL_TIME = 5 * 1000;
    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_NEXT_ITEM) {
                curPosition++;
                assert viewPager.getAdapter() != null;
                if (curPosition > viewPager.getAdapter().getCount() - 1) {
                    curPosition = 0;
                    viewPager.setCurrentItem(curPosition, false);
                } else {
                    viewPager.setCurrentItem(curPosition, true);
                }
                handler.sendEmptyMessageDelayed(MSG_NEXT_ITEM, AUTO_SCROLL_TIME);
            }
            return msg.what == MSG_NEXT_ITEM;
        }
    });

    public JSCBannerView(@NonNull Context context) {
        this(context, null);
    }

    public JSCBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSCBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JSCBannerView, defStyleAttr, 0);
        indicatorPaddingLeft = a.getDimensionPixelSize(R.styleable.JSCBannerView_bv_indicator_padding_left, 0);
        indicatorPaddingRight = a.getDimensionPixelSize(R.styleable.JSCBannerView_bv_indicator_padding_right, 0);
        indicatorPaddingTop = a.getDimensionPixelSize(R.styleable.JSCBannerView_bv_indicator_padding_top, 20);
        indicatorPaddingBottom = a.getDimensionPixelSize(R.styleable.JSCBannerView_bv_indicator_padding_bottom, 20);
        indicatorAlign = a.getInt(R.styleable.JSCBannerView_bv_indicator_align, 0);
        viewPagerLeftMargin = a.getDimensionPixelSize(R.styleable.JSCBannerView_bv_view_pager_left_margin, 0);
        viewPagerRightMargin = a.getDimensionPixelSize(R.styleable.JSCBannerView_bv_view_pager_right_margin, 0);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        viewPager = new ViewPager(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.leftMargin = viewPagerLeftMargin;
        params.rightMargin = viewPagerRightMargin;
        addView(viewPager, params);

        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.BOTTOM;
        indicatorContainerParent = new FrameLayout(context);
        indicatorContainerParent.setPadding(indicatorPaddingLeft, indicatorPaddingTop, indicatorPaddingRight, indicatorPaddingBottom);
        addView(indicatorContainerParent, params1);
        //
        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switch (indicatorAlign) {
            case -1:
                params2.gravity = Gravity.LEFT;
                break;
            case 0:
                params2.gravity = Gravity.CENTER_HORIZONTAL;
                break;
            case 1:
                params2.gravity = Gravity.RIGHT;
                break;
        }
        indicatorContainer = new LinearLayout(context);
        indicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        indicatorContainerParent.addView(indicatorContainer, params2);

//        viewPager.setClipChildren(true);
//        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //find real index by position
                curPosition = position;
                PagerAdapter adapter = viewPager.getAdapter();
                if (adapter instanceof BannerPagerAdapter)
                    updateSelectedIndicator(((BannerPagerAdapter) adapter).changePositionToIndex(curPosition));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Nullable
    public View getBackgroundView() {
        return backgroundView;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * Add a custom background view for viewPager.
     *
     * @param backgroundView custom background view
     * @param params layout params
     */
    public void setBackgroundView(@NonNull View backgroundView, @NonNull LayoutParams params) {
        this.backgroundView = backgroundView;
        if (backgroundView.getParent() != null)
            throw new IllegalArgumentException("This backgroundView already have a prent.");
        addView(backgroundView, 0, params);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    public <T> void setAdapter(@NonNull BannerPagerAdapter<T> adapter) {
        viewPager.setAdapter(adapter);
        adapter.setDataSetChangedListener(new OnDataSetChangedListener<T>() {
            @Override
            public void onDataSetChanged(List<T> bannerItems, OnCreateIndicatorViewListener<T> createIndicatorViewListener) {
                int number = bannerItems.size();
                indicatorContainer.removeAllViews();
                for (int i = 0; i < number; i++) {
                    View view = null;
                    if (createIndicatorViewListener != null) {
                        view = createIndicatorViewListener.onCreateIndicatorView(getContext(), i, bannerItems.get(i));
                    }
                    if (view == null)
                        view = getDefaultIndicatorView();
                    indicatorContainer.addView(view);
                }
                updateSelectedIndicator(number > 0 ? 0 : -1);
            }
        });
    }

    /**
     * It will not take effect if the data length is less than 3.
     */
    public void start() {
        BannerPagerAdapter adapter = (BannerPagerAdapter) viewPager.getAdapter();
        if (adapter == null || adapter.getCount() < 3)
            return;

        handler.sendEmptyMessageDelayed(MSG_NEXT_ITEM, AUTO_SCROLL_TIME);
    }

    public void stop() {
        handler.removeMessages(MSG_NEXT_ITEM);
    }

    private void updateSelectedIndicator(int index) {
        int count = indicatorContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            indicatorContainer.getChildAt(i).setSelected(false);
        }
        if (index >= 0 && index < count)
            indicatorContainer.getChildAt(index).setSelected(true);
    }

    private View getDefaultIndicatorView() {
        int dp2 = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 2, getResources().getDisplayMetrics()) + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2 * 4, dp2 * 4);
        params.leftMargin = dp2 * 2;
        params.rightMargin = dp2 * 2;
        View view = new View(getContext());
        view.setBackgroundResource(R.drawable.indicator_background_selector);
        view.setLayoutParams(params);
        return view;
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }
}
