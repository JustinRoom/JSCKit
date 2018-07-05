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
import android.util.Log;
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
    public final static int DEFAULT_REBOUND_DURATION = 1000;

    private OverScroller overScroller;
    private VelocityTracker velocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int scaledTouchSlop;

    private int reboundAnimDuration = DEFAULT_REBOUND_DURATION;
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
//        Log.i(TAG, "init: scaledTouchSlop = " + scaledTouchSlop);

        recyclerView = new RecyclerView(context);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //如果recyclerView已经停止滚动，则rebound
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    rebound(300);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
                if (!pressed) {
                    pressed = true;
                }
                if (!overScroller.isFinished()) {
                    overScroller.forceFinished(true);
                }
                recyclerView.stopScroll();
                mLastTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curTouchY = ev.getY();
                float deltaY = mLastTouchY - curTouchY;
                mLastTouchY = curTouchY;
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
                move(deltaY);
                break;
            case MotionEvent.ACTION_UP:
                int initialVelocity = getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    int verticalOffset = recyclerView.computeVerticalScrollOffset();
                    int distanceFromBottom = recyclerView.computeVerticalScrollRange() - verticalOffset - recyclerView.getMeasuredHeight();
                    if ((initialVelocity > 0 && verticalOffset > 0)
                            || (initialVelocity < 0 && distanceFromBottom > 0)) {
                        recyclerView.fling(0, -initialVelocity);
                    } else {
                        rebound(reboundAnimDuration);
                    }
                } else {
                    rebound(reboundAnimDuration);
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

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

    private void rebound(int duration) {
        if (getScrollY() == 0)
            return;
        overScroller.startScroll(0, getScrollY(), 0, -getScrollY(), duration);
        invalidate();
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
     * The default value is {@link #DEFAULT_SCROLL_RATIO}.
     * @param scrollRatio scroll ratio on vertical direction
     */
    public void setScrollRatio(@FloatRange(from = 0) float scrollRatio) {
        this.scrollRatio = scrollRatio;
    }

    public int getReboundAnimDuration() {
        return reboundAnimDuration;
    }

    /**
     * The default value is {@link #DEFAULT_REBOUND_DURATION}.
     * @param reboundAnimDuration  the duration of rebound animation
     */
    public void setReboundAnimDuration(@IntRange(from = 0) int reboundAnimDuration) {
        this.reboundAnimDuration = reboundAnimDuration;
    }
}
