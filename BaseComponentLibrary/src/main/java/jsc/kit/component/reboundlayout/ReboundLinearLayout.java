package jsc.kit.component.reboundlayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jsc.kit.component.IViewAttrDelegate;
import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class ReboundLinearLayout extends LinearLayout implements IViewAttrDelegate {

    public static final String TAG = "ReboundLinearLayout";
    public static final int REBOUND_ORIENTATION_HORIZONTAL = 0;
    public static final int REBOUND_ORIENTATION_VERTICAL = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REBOUND_ORIENTATION_HORIZONTAL, REBOUND_ORIENTATION_VERTICAL})
    public @interface ReboundOrientation {
    }

    public static final Property<View, Integer> SCROLL_X = new IntProperty<View>("scrollX") {
        @Override
        public void setValue(View object, int value) {
            object.setScrollX(value);
        }

        @Override
        public Integer get(View object) {
            return object.getScrollX();
        }
    };

    public static final Property<View, Integer> SCROLL_Y = new IntProperty<View>("scrollY") {
        @Override
        public void setValue(View object, int value) {
            object.setScrollY(value);
        }

        @Override
        public Integer get(View object) {
            return object.getScrollY();
        }
    };

    private int visibleWidth;
    private int visibleHeight;
    private float lastTouchX;
    private float lastTouchY;
    private int reboundOrientation;

    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int scaledTouchSlop;
    private VelocityTracker mVelocityTracker = null;
    private OverScroller mScroller;

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            rebound();
        }
    };

    public ReboundLinearLayout(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public ReboundLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public ReboundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        mScroller = new OverScroller(context);
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
//        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
//        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
//        scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ReboundLinearLayout, defStyleAttr, 0);
        reboundOrientation = a.getInt(R.styleable.ReboundLinearLayout_reboundOrientation, REBOUND_ORIENTATION_HORIZONTAL);
        a.recycle();
    }

    public int getReboundOrientation() {
        return reboundOrientation;
    }

    public void setReboundOrientation(@ReboundOrientation int reboundOrientation) {
        if (this.reboundOrientation == reboundOrientation)
            return;
        this.reboundOrientation = reboundOrientation;
        if (isHorizontalRebound()) {
            setScaleY(0);
        } else {
            setScaleX(0);
        }
    }

    private boolean isHorizontalRebound() {
        return reboundOrientation == REBOUND_ORIENTATION_HORIZONTAL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        visibleWidth = getDefaultSize(0, widthMeasureSpec);
        visibleHeight = getDefaultSize(0, heightMeasureSpec);
        switch (getOrientation()) {
            case HORIZONTAL:
                if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
                    Drawable dividerDrawable = getDividerDrawable();
                    int width = getPaddingLeft() + getPaddingRight();
                    for (int i = 0; i < getChildCount(); i++) {
                        View child = getChildAt(i);
                        ViewGroup.LayoutParams childParams = child.getLayoutParams();
                        measureChildren(LayoutParams.WRAP_CONTENT, heightMeasureSpec);
                        width += child.getMeasuredWidth();
                        if (childParams instanceof MarginLayoutParams) {
                            width += ((MarginLayoutParams) childParams).leftMargin;
                            width += ((MarginLayoutParams) childParams).rightMargin;
                        }
                        //考虑到LinearLayout自带的分割线属性
                        if (dividerDrawable != null) {
                            width += dividerDrawable.getIntrinsicWidth();
                        }
                    }
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                }
                break;
            case VERTICAL:
                if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                    Drawable dividerDrawable = getDividerDrawable();
                    int height = getPaddingTop() + getPaddingBottom();
                    for (int i = 0; i < getChildCount(); i++) {
                        View child = getChildAt(i);
                        ViewGroup.LayoutParams childParams = child.getLayoutParams();
                        measureChildren(widthMeasureSpec, LayoutParams.WRAP_CONTENT);
                        height += child.getMeasuredHeight();
                        if (childParams instanceof MarginLayoutParams) {
                            height += ((MarginLayoutParams) childParams).topMargin;
                            height += ((MarginLayoutParams) childParams).bottomMargin;
                        }
                        //考虑到LinearLayout自带的分割线属性
                        if (dividerDrawable != null) {
                            height += dividerDrawable.getIntrinsicHeight();
                        }
                    }
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                }
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        initVelocityTrackerIfNotExists();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mScroller.forceFinished(true);
                removeCallbacks(r);
                lastTouchX = event.getX();
                lastTouchY = event.getY();

                //add event into velocity tracker.
//                mVelocityTracker.clear();
//                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //add event into velocity tracker and compute velocity.
//                mVelocityTracker.addMovement(event);

                float tempTouchX = event.getX();
                float tempTouchY = event.getY();
                //X轴方向上滑动
                float dx = lastTouchX - tempTouchX;
                int deltaX = (int) (dx < 0 ? dx - .5f : dx + .5f);
//                scrollBy(deltaX, 0);

                //Y轴方向上滑动
                float dy = lastTouchY - tempTouchY;
                int deltaY = (int) (dy < 0 ? dy - .5f : dy + .5f);
//                Log.i(TAG, "onTouchEvent: deltaX = " + deltaX + ", deltaY = " + deltaY);
//                scrollBy(0, deltaY);
                if (isHorizontalRebound()) {
                    scrollBy(deltaX, 0);
                } else {
                    scrollBy(0, deltaY);
                }
                lastTouchX = tempTouchX;
                lastTouchY = tempTouchY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //recycle velocity tracker.
//                final VelocityTracker velocityTracker = mVelocityTracker;
//                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
//                float velocity = isHorizontalRebound() ? velocityTracker.getXVelocity() : velocityTracker.getYVelocity();
//                recycleVelocityTracker();
//                Log.i(TAG, "onTouchEvent: [max:" + mMaximumVelocity + ", min:" + mMinimumVelocity + ", cur:" + velocity + "]");
//                if ((Math.abs(velocity) > mMinimumVelocity)) {
//                    fling((int) -velocity);
//                } else {
//                    postDelayed(r, 10);
//                }

                postDelayed(r, 10);
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        if (mScroller.computeScrollOffset()) {
//            int oldX = getScrollX();
//            int oldY = getScrollY();
//            int x = mScroller.getCurrX();
//            int y = mScroller.getCurrY();
//
//            if (oldX != x || oldY != y) {
//                if (isHorizontalRebound())
//                    scrollBy(x - oldX, 0);
//                else
//                    scrollBy(0, y - oldY);
//                postInvalidateOnAnimation();
//            }
//        } else {
//            post(r);
//        }
    }

    public void fling(int velocity) {
        if (isHorizontalRebound())
            mScroller.fling(getScrollX(), getScrollY(), velocity, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        else
            mScroller.fling(getScrollX(), getScrollY(), 0, velocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        postInvalidateOnAnimation();
    }

    private void rebound() {
        int from = 0;
        int to = 0;
        switch (getReboundOrientation()) {
            case REBOUND_ORIENTATION_HORIZONTAL:
                if (getScrollX() < 0) {
                    from = getScrollX();
                    to = 0;
                } else if (getScrollX() > getWidth() - visibleWidth) {
                    from = getScrollX();
                    to = getWidth() - visibleWidth;
                }
                break;
            case REBOUND_ORIENTATION_VERTICAL:
                if (getScrollY() < 0) {
                    from = getScrollY();
                    to = 0;
                } else if (getScrollY() > getHeight() - visibleHeight) {
                    from = getScrollY();
                    to = getHeight() - visibleHeight;
                }
                break;
        }
        if (from == 0 && to == 0)
            return;

        Property<View, Integer> property = isHorizontalRebound() ? SCROLL_X : SCROLL_Y;
        ObjectAnimator animator = ObjectAnimator.ofInt(this, property, from, to);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
