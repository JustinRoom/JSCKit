package jsc.kit.refreshlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import jsc.kit.R;

/**
 * <p>
 *     Pull to refresh layout.You can't scroll up from bottom if {@link #isRefreshing()} is true.
 *     The minimal compatible sdk version is {@link Build.VERSION_CODES#ICE_CREAM_SANDWICH}.
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class RefreshLayout extends ViewGroup {

    private final String TAG = getClass().getSimpleName();
    private final float DEFAULT_PULL_RATIO_Y = 0.55f;
    private final float DEFAULT_PULL_TO_REFRESH_RATIO = 0.45f;
    private final float DEFAULT_RELEASE_TO_REFRESH_RATIO = 0.65f;
    private final int ANIM_TIME = 300;
    private float mLastY;
    private Scroller mScroller;

    private View headerView;
    private View contentView;
    private int contentViewHeight = 0;
    private boolean isRefreshing = false;
    private float pullRatioY = DEFAULT_PULL_RATIO_Y;
    private float pullToRefreshRatio = DEFAULT_PULL_TO_REFRESH_RATIO;
    private float releaseToRefreshRatio = DEFAULT_RELEASE_TO_REFRESH_RATIO;

    private OnScrollListener onScrollListener;
    private OnRefreshListener onRefreshListener;

    public interface OnScrollListener {
        /**
         * Scroll callback.
         * @param headerView The header view added by yourself.
         * @param headerHeight The header's height.
         * @param pullToRefreshRatio Pull to refresh ratio, <code>pullToRefreshHeight = headerHeight * pullToRefreshRatio</code>.
         * @param releaseToRefreshRatio Release to refresh ratio, <code>releaseToRefreshHeight = headerHeight * releaseToRefreshRatio</code>.
         * @param scrollY {@link #getScrollY()}. Negative to scroll down from top, positive to scroll up from bottom.
         * @param isRefreshing {@link #isRefreshing()}
         * @see #setPullToRefreshRatio(float)
         * @see #setReleaseToRefreshRatio(float)
         */
        void onScroll(View headerView, int headerHeight, float pullToRefreshRatio, float releaseToRefreshRatio, int scrollY, boolean isRefreshing);
    }

    public interface OnRefreshListener {
        /**
         * Do something here before refreshing action.For example, start header view animation.
         * @param headerView The header view added by yourself.
         */
        void onStartRefresh(View headerView);

        /**
         * Release to refresh call back.
         */
        void onRefresh();

        /**
         * do something here after refreshing action.For example, stop header view animation.
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
        if (a.hasValue(R.styleable.RefreshLayout_rf_headerLayout)){
            int headerLayoutId = a.getResourceId(R.styleable.RefreshLayout_rf_headerLayout, 0);
            addHeader(headerLayoutId);
        } else {
            addHeader(createDefaultHeader());
        }

        if (a.hasValue(R.styleable.RefreshLayout_rf_contentLayout)){
            int headerLayoutId = a.getResourceId(R.styleable.RefreshLayout_rf_contentLayout, 0);
            addContent(headerLayoutId);
        } else {
            addContent(createDefaultContent());
        }
    }

    /**
     * Find header child by id.
     * @param id
     * @param <T>
     * @return
     */
    public final <T extends View> T findHeaderChildView(@IdRes int id){
        return headerView.findViewById(id);
    }

    /**
     * Find content child by id.
     * @param id
     * @param <T>
     * @return
     */
    public final <T extends View> T findContentChildView(@IdRes int id){
        return contentView.findViewById(id);
    }

    /**
     * @see #addHeader(View)
     * @param layoutId
     * @return
     */
    public View addHeader(@LayoutRes int layoutId){
        View header = LayoutInflater.from(getContext()).inflate(layoutId, null);
        addHeader(header);
        return headerView;
    }

    /**
     * 添加下拉刷新头部view
     * @param header
     */
    public void addHeader(View header) {
        if (headerView != null)
            removeView(headerView);
        headerView = header;
        addView(headerView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        requestLayout();
    }

    /**
     * @see #addContent(View)
     * @param layoutId
     * @return
     */
    public View addContent(@LayoutRes int layoutId){
        View content = LayoutInflater.from(getContext()).inflate(layoutId, null);
        addContent(content);
        return contentView;
    }

    /**
     * Ddd a custom content view.
     * @param content
     */
    public void addContent(View content) {
        if (contentView != null)
            removeView(contentView);
        contentView = content;
        addView(contentView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        requestLayout();
    }

    /**
     * Add scroll listener.
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * Add refresh listener.
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * @see #setPullRatioY(float)
     * @return
     */
    public float getPullRatioY() {
        return pullRatioY;
    }

    /**
     * The Y coordinate pull ratio. The default value is {@link #DEFAULT_PULL_RATIO_Y}.
     * @param pullRatioY float,value is in [0, 1].
     */
    public void setPullRatioY(@FloatRange(from = 0, to = 1.0f) float pullRatioY) {
        this.pullRatioY = pullRatioY;
    }

    /**
     * @see #setPullToRefreshRatio(float)
     * @return
     */
    public float getPullToRefreshRatio() {
        return pullToRefreshRatio;
    }

    /**
     * The default value is {@link #DEFAULT_PULL_TO_REFRESH_RATIO}.
     * @param pullToRefreshRatio float,value is in [0, 1].
     */
    public void setPullToRefreshRatio(@FloatRange(from = 0, to = 1.0f) float pullToRefreshRatio) {
        this.pullToRefreshRatio = pullToRefreshRatio;
    }

    /**
     * @see #setReleaseToRefreshRatio(float)
     * @return
     */
    public float getReleaseToRefreshRatio() {
        return releaseToRefreshRatio;
    }

    /**
     * The default value is {@link #DEFAULT_RELEASE_TO_REFRESH_RATIO}.
     * @param releaseToRefreshRatio float,value is in [0, 1].
     */
    public void setReleaseToRefreshRatio(@FloatRange(from = 0, to = 1.0f)float releaseToRefreshRatio) {
        this.releaseToRefreshRatio = releaseToRefreshRatio;
    }

    @Override
    @CallSuper
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2)
            throw new IllegalArgumentException("Couldn't add more children.");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
        contentViewHeight = height;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        height = contentViewHeight + headerView.getMeasuredHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View header = getChildAt(0);
        View content = getChildAt(1);
        header.layout(0, -header.getMeasuredHeight(), getWidth(), 0);
        content.layout(0, 0, getWidth(), contentViewHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
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
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
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
            mScroller.startScroll(0, scrollY, 0, -(scrollY - headerHeight), ANIM_TIME);
            invalidate();
            return;
        }

        if (scrollY <= headerHeight * releaseToRefreshRatio) {//向下滑动
            mScroller.startScroll(0, scrollY, 0, -(scrollY - headerHeight), ANIM_TIME);
            invalidate();
            isRefreshing = true;
            if (onRefreshListener != null){
                onRefreshListener.onStartRefresh(headerView);
                onRefreshListener.onRefresh();
            }
            return;
        }

        mScroller.startScroll(0, scrollY, 0, -scrollY, ANIM_TIME);
        invalidate();
    }

    /**
     * You can forwardly call this method to start refresh action after a minute.
     * It would do nothing if it is refreshing.
     * @param delay delay time in milliseconds
     * @see #isRefreshing()
     */
    public void startRefresh(long delay){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing)
                    return;

                mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - (0 - headerView.getMeasuredHeight())), ANIM_TIME);
                invalidate();
                isRefreshing = true;
                if (onRefreshListener != null){
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
        if (onRefreshListener != null)
            onRefreshListener.onEndRefresh(headerView);
        isRefreshing = false;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), ANIM_TIME);
                invalidate();
            }
        }, 100);
    }

    /**
     * Check refresh state.
     * @return
     */
    public boolean isRefreshing() {
        return isRefreshing;
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