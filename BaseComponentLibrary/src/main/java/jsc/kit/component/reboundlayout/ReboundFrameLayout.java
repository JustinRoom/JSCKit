package jsc.kit.component.reboundlayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Rebound after dragging like IOS.
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class ReboundFrameLayout extends FrameLayout {
    private final String TAG = getClass().getSimpleName();
    private final float DEFAULT_SLIDING_SCALE_RATIO = 0.65f;
    /**
     * default rebound duration
     */
    public final static int DEFAULT_MAX_REBOUND_DURATION = 1000;

    private float mLastTouchY;
    private OverScroller mOverScroller;
    private VelocityTracker velocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int scaledTouchSlop;
    private boolean pressed;
    /**
     * the ratio of sliding on vertical direction.
     */
    private float scrollRatio = DEFAULT_SLIDING_SCALE_RATIO;
    /**
     * the duration of rebound animation
     */
    private int maxReboundAnimDuration = DEFAULT_MAX_REBOUND_DURATION;

    public ReboundFrameLayout(Context context) {
        this(context, null);
    }

    public ReboundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mOverScroller = new OverScroller(context);
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            View target = getChildAt(0);
            ViewGroup.LayoutParams params = target.getLayoutParams();
            int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.AT_MOST);
            target.measure(widthMeasureSpec, newHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: ");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getChildCount() == 0)
            return super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录是否按下
                if (!pressed) {
                    pressed = true;
                }
                if (!mOverScroller.isFinished())
                    mOverScroller.forceFinished(true);
                mLastTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curTouchY = ev.getY();
                float deltaY = mLastTouchY - curTouchY;
                mLastTouchY = curTouchY;
                //如果滑动的距离小于scaledTouchSlop，我们认为这次滑动是无效的，把这次事件传递给contentView去消费。例如contentView的child的点击事件。
                //如果大于等于scaledTouchSlop，我们把这次事件给拦截掉，让这次事件在onTouchEvent(MotionEvent ev)方法中消费掉。
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
        if (getChildCount() == 0)
            return super.onTouchEvent(event);

//        trackerEvent(event);
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
                executeScroll(getChildAt(0), deltaY);
                break;

            case MotionEvent.ACTION_UP:
                //计算滑动速度
//                int initialVelocity = getYVelocity();
                //释放滑动事件跟踪器
//                recycleVelocityTracker();
//                if (Math.abs(initialVelocity) > mMinimumVelocity) {
//                    mOverScroller.fling(0, getScrollY(), 0, - initialVelocity, 0, 0, -getHeight() / 2, getHeight() / 2);
//                    invalidate();
//                } else {
//                    rebound();
//                }

                rebound();
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());
            invalidate();

//            int deltaY = -(mOverScroller.getCurrY() - getScrollY());
//            if (deltaY != 0)
//                executeScroll(getChildAt(0), deltaY);
//            invalidate();
        } else if (!pressed) {
//            rebound();
        }
    }

    /**
     * 滑动处理
     * <p>
     * * @param target target
     *
     * @param deltaY deltaY
     */
    private void executeScroll(View target, int deltaY) {
        //为了方便下面的描述，我们用content代表target的内容，即：content=target的内容。
        //content下滑的高度
        int verticalOffset = target.getScrollY();

        int scrollDistance = 0;
        int scrollY = -getScrollY();
        if (deltaY < 0) {//向下滑动
            //target底部已经向上离开ReboundFrameLayout的底部
            if (scrollY < 0) {
                scrollDistance = Math.max(deltaY, scrollY);
                scrollBy(0, (int) (scrollDistance * scrollRatio));
                deltaY = deltaY - scrollDistance;
                if (deltaY >= 0)
                    return;
            }

            scrollDistance = Math.max(deltaY, -verticalOffset);
            target.scrollBy(0, scrollDistance);
            deltaY = deltaY - scrollDistance;
            if (deltaY >= 0)
                return;

            scrollBy(0, (int) (deltaY * scrollRatio));
        } else if (deltaY > 0) {//向上滑动
            //content在target中的可见区域
            Rect rect = new Rect();
            target.getLocalVisibleRect(rect);
            //content的实际高度
            int realHeight = target.getMeasuredHeight();
            //content底部与target底部对齐时需要向上滑动的距离。
            // 如果target底部已向上经拉出屏幕外，则认为distanceFromBottom为0
            int distanceFromBottom = realHeight - rect.bottom;
            distanceFromBottom = Math.max(distanceFromBottom, 0);

            //target顶部已经向下离开ReboundFrameLayout的顶部
            if (scrollY > 0) {
                scrollDistance = Math.min(deltaY, scrollY);
                scrollBy(0, (int) (scrollDistance * scrollRatio));
                deltaY = deltaY - scrollDistance;
                if (deltaY <= 0)
                    return;
            }

            scrollDistance = Math.min(deltaY, distanceFromBottom);
            target.scrollBy(0, scrollDistance);
            deltaY = deltaY - scrollDistance;
            if (deltaY <= 0)
                return;

            scrollBy(0, (int) (deltaY * scrollRatio));
        }
    }

    /**
     * 执行反弹
     */
    private void rebound() {
        if (getScrollY() == 0)
            return;

        mOverScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateDurationByScrollY());
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

    public float getScrollRatio() {
        return scrollRatio;
    }

    /**
     * The default value is {@link #DEFAULT_SLIDING_SCALE_RATIO}.
     *
     * @param scrollRatio the sliding ratio on vertical direction
     */
    public void setScrollRatio(@FloatRange(from = 0, to = 1.0f) float scrollRatio) {
        this.scrollRatio = scrollRatio;
    }

    public int getMaxReboundAnimDuration() {
        return maxReboundAnimDuration;
    }

    /**
     * The default value is {@link #DEFAULT_MAX_REBOUND_DURATION}.
     *
     * @param maxReboundAnimDuration the rebound-animation duration
     */
    public void setMaxReboundAnimDuration(@IntRange(from = 0) int maxReboundAnimDuration) {
        this.maxReboundAnimDuration = maxReboundAnimDuration;
    }
}