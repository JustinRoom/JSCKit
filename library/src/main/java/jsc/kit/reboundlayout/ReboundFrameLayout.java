package jsc.kit.reboundlayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * <p>
 * Rebound after dragging like IOS.
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class ReboundFrameLayout extends FrameLayout {
    private final String TAG = getClass().getSimpleName();
    private final float DEFAULT_SLIDING_SCALE_RATIO = 0.65f;
    private final int DEFAULT_REBOUND_ANIMATION_DURATION = 300;

    private float mStart;
    private Scroller mScroller;
    /**
     * the ratio of sliding on vertical direction.
     */
    private float slidingScaleRatio = DEFAULT_SLIDING_SCALE_RATIO;
    /**
     * the duration of rebound animation
     */
    private int reboundAnimationDuration = DEFAULT_REBOUND_ANIMATION_DURATION;

    public ReboundFrameLayout(Context context) {
        this(context, null);
    }

    public ReboundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        Log.i(TAG, "addView: ");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: ");
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
                if (!mScroller.isFinished())
                    mScroller.forceFinished(true);
                mStart = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mark = (int) (mStart - mCurY);
                int slop = Math.abs(mark);
                mStart = mCurY;
                //如果滑动的距离小于10px，我们认为这次滑动是无效的，把这次事件传递给contentView去消费。例如contentView的child的点击事件。
                //如果大于等于10px，我们把这次事件给拦截掉，让这次事件在onTouchEvent(MotionEvent ev)方法中消费掉。
                if (slop >= 10) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getChildCount() == 0)
            return super.onTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStart = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mYOffset = (int) ((mStart - mCurY) * slidingScaleRatio);
                executeScroll(getChildAt(0), mYOffset);
                mStart = mCurY;
                return true;

            case MotionEvent.ACTION_UP:
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), reboundAnimationDuration);
                postInvalidate();
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

    private void executeScroll(View target, int mYOffset) {
        //为了方便下面的描述，我们用content代表target的内容，即：content=target的内容。
        //content下滑的高度
        int childScrollY = target.getScrollY();
        //content在target中的可见区域
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        //content的实际高度
        int realHeight = target.getMeasuredHeight();

        //content底部与target底部对齐时需要向上滑动的距离。
        // 如果target底部已向上经拉出屏幕外，则认为distanceFromBottom为0
        int distanceFromBottom = rect.bottom < 0 ? 0 : realHeight - rect.bottom;

        int scrollY = getScrollY();
        Log.i(TAG, "executeScroll: scrollY = " + scrollY);
        int childScrolledY = Math.abs(childScrollY);
        int scrolledY = Math.abs(scrollY);
        int distance = Math.abs(mYOffset);
        if (mYOffset < 0){//向下滑动
            //向下滑动target与ReboundFrameLayout底部对齐
            scrollBy(0, -Math.min(distance, scrolledY));
            distance = distance - scrolledY;
            if (distance <= 0){
                return;
            }

            //向下滑动content与target顶部对齐
            target.scrollBy(0, -Math.min(distance, childScrolledY));
            distance = distance - childScrolledY;
            if (distance <= 0){
                return;
            }

            //向下滑动target
            scrollBy(0, -distance);
        } else if (mYOffset > 0) {//向上滑动
            //向上滑动target与ReboundFrameLayout顶部对齐
            scrollBy(0, Math.min(distance, scrolledY));
            distance = distance - scrolledY;
            if (distance <= 0){
                return;
            }

            //向上滑动content与target底部对齐
            target.scrollBy(0, Math.min(distance, distanceFromBottom));
            distance = distance - distanceFromBottom;
            if (distance <= 0){
                return;
            }

            //向上滑动target
            scrollBy(0, distance);
        }
    }

    public float getSlidingScaleRatio() {
        return slidingScaleRatio;
    }

    /**
     * The default value is {@link #DEFAULT_SLIDING_SCALE_RATIO}.
     *
     * @param slidingScaleRatio
     */
    public void setSlidingScaleRatio(@FloatRange(from = 0, to = 1.0f) float slidingScaleRatio) {
        this.slidingScaleRatio = slidingScaleRatio;
    }

    public int getReboundAnimationDuration() {
        return reboundAnimationDuration;
    }

    /**
     * The default value is {@link #DEFAULT_REBOUND_ANIMATION_DURATION}.
     *
     * @param reboundAnimationDuration
     */
    public void setReboundAnimationDuration(@IntRange(from = 0) int reboundAnimationDuration) {
        this.reboundAnimationDuration = reboundAnimationDuration;
    }
}