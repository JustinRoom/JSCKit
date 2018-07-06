package jsc.kit.component.reboundlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Rebound recycler view on vertical direction.
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class ReboundRecyclerView extends FrameLayout {

    private final static String TAG = "ReboundRecyclerView";
    /**
     * default scroll ratio
     */
    public final static float DEFAULT_SCROLL_RATIO = 0.55f;
    /**
     * default rebound duration
     */
    public final static int DEFAULT_MAX_REBOUND_DURATION = 1000;

    private OverScroller overScroller;
    private VelocityTracker velocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int scaledTouchSlop;

    private int maxReboundAnimDuration = DEFAULT_MAX_REBOUND_DURATION;
    private float scrollRatio = DEFAULT_SCROLL_RATIO;
    private RecyclerView recyclerView;
    private float mLastTouchY;
    private boolean pressed = false;

    public ReboundRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public ReboundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ReboundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReboundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        overScroller = new OverScroller(context);
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        scaledTouchSlop = viewConfiguration.getScaledTouchSlop();

        recyclerView = new RecyclerView(context);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        addView(recyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void computeScroll() {
//        Log.i(TAG, "computeScroll: ");
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录是否按下
                if (!pressed) {
                    pressed = true;
                }
                //停止自动滑动
                if (!overScroller.isFinished()) {
                    overScroller.forceFinished(true);
                }
                //停止RecyclerView列表的滑动
                recyclerView.stopScroll();
                mLastTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curTouchY = ev.getY();
                float deltaY = mLastTouchY - curTouchY;
                mLastTouchY = curTouchY;
                //如果滑动距离小于scaledTouchSlop，则把事件交给子View消耗；否则此事件交由自己的onTouchEvent(MotionEvent event)方法消耗。
                if (Math.abs(deltaY) >= scaledTouchSlop)
                    return true;
                break;
            case MotionEvent.ACTION_UP:
                pressed = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //跟踪滑动事件，在MotionEvent.ACTION_UP计算滑动速度
        trackerEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curTouchY = event.getY();
                float dy = mLastTouchY - curTouchY;
                int deltaY = (int) (dy);
                mLastTouchY = curTouchY;
                //执行滑动处理
                move(deltaY);
                break;
            case MotionEvent.ACTION_UP:
                //计算滑动速度
                int initialVelocity = getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    int verticalOffset = recyclerView.computeVerticalScrollOffset();
                    int distanceFromBottom = recyclerView.computeVerticalScrollRange() - verticalOffset - recyclerView.getMeasuredHeight();
                    if ((initialVelocity > 0 && verticalOffset > 0)
                            || (initialVelocity < 0 && distanceFromBottom > 0)) {
                        //执行RecyclerView平滑
                        recyclerView.fling(0, -initialVelocity);
                    }
                }
                //执行反弹
                rebound();
                //释放滑动事件跟踪器
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     *
     * @param deltaY negative value represents moving down, positive value represents moving up.
     */
    private void move(int deltaY) {
        int verticalOffset = recyclerView.computeVerticalScrollOffset();
        int distanceFromBottom = recyclerView.computeVerticalScrollRange() - verticalOffset - recyclerView.getMeasuredHeight();
//        Log.i(TAG, "move: scrollY = " + getScrollY());
        //getScrollY()<0 子view的顶部向下👇离开ReboundRecyclerView的顶部；getScrollY()>0 子view的底部向上👆离开ReboundRecyclerView的底部
        int scrollDistance = 0;
        int scrollY = -getScrollY();
        if (deltaY < 0) {//向下滑动
            //recyclerView底部已经向上离开ReboundRecyclerView的底部
            if (scrollY < 0) {
                scrollDistance = Math.max(deltaY, scrollY);
                scrollBy(0, (int) (scrollDistance * scrollRatio));
                deltaY = deltaY - scrollDistance;
                if (deltaY >= 0)
                    return;
            }

            scrollDistance = Math.max(deltaY, -verticalOffset);
            recyclerView.scrollBy(0, scrollDistance);
            deltaY = deltaY - scrollDistance;
            if (deltaY >= 0)
                return;

            scrollBy(0, (int) (deltaY * scrollRatio));
        } else if (deltaY > 0) {//向上滑动
            //recyclerView顶部已经向下离开ReboundRecyclerView的顶部
            if (scrollY > 0) {
                scrollDistance = Math.min(deltaY, scrollY);
                scrollBy(0, (int) (scrollDistance * scrollRatio));
                deltaY = deltaY - scrollDistance;
                if (deltaY <= 0)
                    return;
            }

            scrollDistance = Math.min(deltaY, distanceFromBottom);
            recyclerView.scrollBy(0, scrollDistance);
            deltaY = deltaY - scrollDistance;
            if (deltaY <= 0)
                return;

            scrollBy(0, (int) (deltaY * scrollRatio));
        }
    }

    private void rebound() {
        if (getScrollY() == 0)
            return;
        overScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateDurationByScrollY());
        invalidate();
    }

    /**
     * Calculate suitable rebound duration by scroll distance.
     *
     * @return suitable rebound duration. The minimum duration is 300, and the maximal is {@link #getMaxReboundAnimDuration()}.
     */
    private int calculateDurationByScrollY() {
        int duration = Math.abs(getScrollY()) * 2;
        duration = Math.max(duration, 300);
        duration = Math.min(duration, maxReboundAnimDuration);
        return duration;
    }

    private void trackerEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    private int getYVelocity() {
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(600, mMaximumVelocity);
            return (int) velocityTracker.getYVelocity();
        }
        return 0;
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public float getScrollRatio() {
        return scrollRatio;
    }

    /**
     * Set the ratio of scrolling on vertical direction.
     * <p>For example: {@code distance = scrollY * ratio;}
     * <br>The default value is {@link #DEFAULT_SCROLL_RATIO}.
     *
     * @param scrollRatio scroll ratio on vertical direction
     */
    public void setScrollRatio(@FloatRange(from = 0) float scrollRatio) {
        this.scrollRatio = scrollRatio;
    }

    public int getMaxReboundAnimDuration() {
        return maxReboundAnimDuration;
    }

    /**
     * Set the maximal rebound animation duration.
     * The default value is {@link #DEFAULT_MAX_REBOUND_DURATION}.
     *
     * @param maxReboundAnimDuration the maximal rebound animation duration
     */
    public void setMaxReboundAnimDuration(@IntRange(from = 300) int maxReboundAnimDuration) {
        this.maxReboundAnimDuration = maxReboundAnimDuration;
    }
}
