package jsc.kit.component.refreshlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import jsc.kit.component.R;

/**
 * <p>
 * Pull to refresh layout.You can't scroll up from bottom if {@link #isRefreshing()} is true.
 * The minimal compatible sdk version is {@link Build.VERSION_CODES#ICE_CREAM_SANDWICH}.
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class RefreshLayout extends ViewGroup {

    private final String TAG = getClass().getSimpleName();
    /**
     *
     */
    public final static int STATE_PULL_INIT = -1;
    public final static int STATE_PULL_TO_REFRESH = 0;
    public final static int STATE_RELEASE_TO_REFRESH = 1;

    private final float DEFAULT_PULL_RATIO_Y = 0.55f;
    private final float DEFAULT_PULL_TO_REFRESH_RATIO = 0.45f;
    private final float DEFAULT_RELEASE_TO_REFRESH_RATIO = 0.65f;
    private final int DEFAULT_REBOUND_ANIMATION_DURATION = 300;
    private float mLastY;
    private Scroller mScroller;

    private View headerView;
    private View contentView;
    private boolean isRefreshing = false;
    /**
     * the ration of pulling on vertical direction
     */
    private float pullRatioY = DEFAULT_PULL_RATIO_Y;
    /**
     * the ratio of pulling to refresh
     */
    private float pullToRefreshRatio = DEFAULT_PULL_TO_REFRESH_RATIO;
    /**
     * the ratio of releasing to refresh
     */
    private float releaseToRefreshRatio = DEFAULT_RELEASE_TO_REFRESH_RATIO;
    /**
     * the duration of rebound animation
     */
    private int reboundAnimationDuration = DEFAULT_REBOUND_ANIMATION_DURATION;

    private OnScrollListener onScrollListener;
    private OnRefreshListener onRefreshListener;

    public interface OnScrollListener {
        /**
         * Scroll callback.
         *
         * @param headerView            the header view
         * @param headerHeight          the height of header view
         * @param pullToRefreshRatio    the ratio of pulling to refresh
         * @param releaseToRefreshRatio the ratio of releasing to refresh
         * @param scrollY               the scroll on vertical direction. Negative to scroll down from top, positive to scroll up from bottom. {@link #getScrollY()}
         * @param isRefreshing          true, doing refreshing action
         * @see #setPullToRefreshRatio(float)
         * @see #setReleaseToRefreshRatio(float)
         */
        void onScroll(View headerView, int headerHeight, float pullToRefreshRatio, float releaseToRefreshRatio, int scrollY, boolean isRefreshing);
    }

    public interface OnRefreshListener {
        /**
         * Do something here before refreshing action.For example, start header view animation.
         *
         * @param headerView The header view added by yourself.
         */
        void onStartRefresh(View headerView);

        /**
         * Release to refresh call back.
         */
        void onRefresh();

        /**
         * do something here after refreshing action.For example, stop header view animation.
         *
         * @param headerView The header view added by yourself.
         */
        void onEndRefresh(View headerView);
    }

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout, defStyleAttr, 0);
        init(context, a);
        a.recycle();
    }

    private void init(Context context, TypedArray a) {
        mScroller = new Scroller(context);
        if (a.hasValue(R.styleable.RefreshLayout_rf_headerLayout)) {
            int headerLayoutId = a.getResourceId(R.styleable.RefreshLayout_rf_headerLayout, 0);
            addHeader(headerLayoutId);
        } else {
            addHeader(createDefaultHeader());
        }

        if (a.hasValue(R.styleable.RefreshLayout_rf_contentLayout)) {
            int headerLayoutId = a.getResourceId(R.styleable.RefreshLayout_rf_contentLayout, 0);
            addContent(headerLayoutId);
        } else {
            addContent(createDefaultContent());
        }
    }

    /**
     * @param layoutId layout resource id
     * @see #addHeader(View)
     */
    public void addHeader(@LayoutRes int layoutId) {
        View header = LayoutInflater.from(getContext()).inflate(layoutId, null);
        addHeader(header);
    }

    /**
     * 添加下拉刷新头部view
     *
     * @param header header view
     */
    public void addHeader(View header) {
        if (headerView != null)
            removeView(headerView);
        headerView = header;
        addView(headerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        requestLayout();
    }

    /**
     * @param layoutId layout resource id
     * @see #addContent(View)
     */
    public void addContent(@LayoutRes int layoutId) {
        View content = LayoutInflater.from(getContext()).inflate(layoutId, null);
        addContent(content);
    }

    /**
     * Ddd a custom content view.
     *
     * @param content content view
     */
    public void addContent(View content) {
        if (contentView != null)
            removeView(contentView);
        contentView = content;
        addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        requestLayout();
    }

    /**
     * Add scroll listener.
     *
     * @param onScrollListener scroll listener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * Add refresh listener.
     *
     * @param onRefreshListener refresh listener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * @return pull ratio on vertical direction
     * @see #setPullRatioY(float)
     */
    public float getPullRatioY() {
        return pullRatioY;
    }

    /**
     * The Y coordinate pull ratio. The default value is {@link #DEFAULT_PULL_RATIO_Y}.
     *
     * @param pullRatioY float,value is in [0, 1].
     */
    public void setPullRatioY(@FloatRange(from = 0, to = 1.0f) float pullRatioY) {
        this.pullRatioY = pullRatioY;
    }

    /**
     * @return pull to refresh ratio
     * @see #setPullToRefreshRatio(float)
     */
    public float getPullToRefreshRatio() {
        return pullToRefreshRatio;
    }

    /**
     * The default value is {@link #DEFAULT_PULL_TO_REFRESH_RATIO}.
     *
     * @param pullToRefreshRatio float,value is in [0, 1].
     */
    public void setPullToRefreshRatio(@FloatRange(from = 0, to = 1.0f) float pullToRefreshRatio) {
        this.pullToRefreshRatio = pullToRefreshRatio;
    }

    /**
     * @return release to refresh ratio
     * @see #setReleaseToRefreshRatio(float)
     */
    public float getReleaseToRefreshRatio() {
        return releaseToRefreshRatio;
    }

    /**
     * The default value is {@link #DEFAULT_RELEASE_TO_REFRESH_RATIO}.
     *
     * @param releaseToRefreshRatio float,value is in [0, 1].
     */
    public void setReleaseToRefreshRatio(@FloatRange(from = 0, to = 1.0f) float releaseToRefreshRatio) {
        this.releaseToRefreshRatio = releaseToRefreshRatio;
    }

    /**
     * Check refresh state.
     *
     * @return {@code true} refreshing, else not.
     */
    public boolean isRefreshing() {
        return isRefreshing;
    }

    public int getReboundAnimationDuration() {
        return reboundAnimationDuration;
    }

    /**
     * The default value is {@link #DEFAULT_REBOUND_ANIMATION_DURATION}.
     *
     * @param reboundAnimationDuration rebound animation duration
     */
    public void setReboundAnimationDuration(@IntRange(from = 0) int reboundAnimationDuration) {
        this.reboundAnimationDuration = reboundAnimationDuration;
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() > 2)
            throw new IllegalArgumentException("There are two children at most.");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2)
            throw new IllegalArgumentException("There are two children at most.");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (headerView != null)
            headerView.layout(0, 0 - headerView.getMeasuredHeight(), getWidth(), 0);
        if (contentView != null)
            contentView.layout(0, 0, getWidth(), getHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isRefreshing && !mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mark = (int) (mCurY - mLastY);
                mLastY = mCurY;
                //contentView内容在Y轴上可滑动，把事件传递给contentView内部
                if (contentView.canScrollVertically(-mark))
                    return false;

                //如果滑动的距离小于10px，我们认为这次滑动是无效的，把这次事件传递给contentView去消费。例如contentView的child的点击事件。
                //如果大于等于10px，我们把这次事件给拦截掉，让这次事件在onTouchEvent(MotionEvent ev)方法中消费掉。
                if (Math.abs(mark) >= 10)
                    return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mYOffset = (int) ((mCurY - mLastY) * pullRatioY);
                //正在刷新时不能上滑
                if ((isRefreshing && mYOffset < 0)) {

                } else {
                    scroll(mYOffset);
                    mLastY = mCurY;
                }
                break;

            case MotionEvent.ACTION_UP:
                rebound();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void scroll(int mYOffset) {
        scrollBy(0, -mYOffset);
        if (onScrollListener != null)
            onScrollListener.onScroll(headerView, headerView.getMeasuredHeight(), pullToRefreshRatio, releaseToRefreshRatio, getScrollY(), isRefreshing);
    }

    private void rebound() {
        int scrollY = getScrollY();
        int headerHeight = -headerView.getMeasuredHeight();
        //正在刷新
        if (isRefreshing) {
            mScroller.startScroll(0, scrollY, 0, -(scrollY - headerHeight), reboundAnimationDuration);
            invalidate();
            return;
        }

        if (scrollY <= headerHeight * releaseToRefreshRatio) {//向下滑动
            mScroller.startScroll(0, scrollY, 0, -(scrollY - headerHeight), reboundAnimationDuration);
            invalidate();
            isRefreshing = true;
            if (onRefreshListener != null) {
                onRefreshListener.onStartRefresh(headerView);
                onRefreshListener.onRefresh();
            }
            return;
        }

        mScroller.startScroll(0, scrollY, 0, -scrollY, reboundAnimationDuration);
        invalidate();
    }

    /**
     * You can forwardly call this method to start refresh action after a minute.
     * It would do nothing if it is refreshing.
     *
     * @param delay delay time in milliseconds
     * @see #isRefreshing()
     */
    public void startRefresh(long delay) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing)
                    return;

                mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - (0 - headerView.getMeasuredHeight())), reboundAnimationDuration);
                invalidate();
                isRefreshing = true;
                if (onRefreshListener != null) {
                    onRefreshListener.onStartRefresh(headerView);
                    onRefreshListener.onRefresh();
                }
            }
        }, delay);
    }

    /**
     * Complete the refreshing action.
     * Reset refresh state.
     */
    public void refreshComplete() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefreshing = false;
                if (onRefreshListener != null)
                    onRefreshListener.onEndRefresh(headerView);
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), reboundAnimationDuration);
                invalidate();
            }
        }, 100);
    }

    private View createDefaultContent() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setBackgroundColor(Color.WHITE);
        textView.setText("Content");
        textView.setTextColor(0xff333333);
        return textView;
    }

    private View createDefaultHeader() {
        TextView textView = new TextView(getContext());
        textView.setPadding(0, 32, 0, 32);
        textView.setGravity(Gravity.CENTER);
        textView.setMinHeight(300);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setBackgroundColor(0xfff2f2f2);
        textView.setText("Header");
        textView.setTextColor(0xff333333);
        return textView;
    }
}