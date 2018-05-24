package jsc.kit.component.vscrollscreen;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Scroller;

import jsc.kit.component.R;

/**
 * <p>
 *     Flip layout.
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jsc
 */
public class VScrollScreenLayout extends ViewGroup {
    private final float DEFAULT_Y_SLIDE_RATIO = 0.65f;
    private final float DEFAULT_FLIP_RATIO = 0.05f;
    private final int DEFAULT_REBOUND_ANIM_TIME = 300;
    private final int DEFAULT_FLIP_ANIM_TIME = 500;

    private float mLastY;
    private int curPageIndex = 0;
    private int pageHeight = 0;

    private float ySlideRatio;
    private float flipRatio;
    private int reboundAnimTime;
    private int flipAnimTime;

    private Scroller mScroller;
    private OnScrollPageChangedListener onScrollPageChangedListener;

    public interface OnScrollPageChangedListener {
        /**
         * @param pageIndex
         *         The index of first page is 0.
         */
        void onScroll(Context context, int pageIndex);
    }

    public VScrollScreenLayout(Context context) {
        this(context, null);
    }

    public VScrollScreenLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VScrollScreenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VScrollScreenLayout, defStyleAttr, 0);
        float tempYSlideRate = a.getFloat(R.styleable.VScrollScreenLayout_vssl_y_slide_ratio, DEFAULT_Y_SLIDE_RATIO);
        float tempFlipRatio = a.getFloat(R.styleable.VScrollScreenLayout_vssl_flip_ratio, DEFAULT_FLIP_RATIO);
        int tempReboundAnimTime = a.getInteger(R.styleable.VScrollScreenLayout_vssl_rebound_anim_time, DEFAULT_REBOUND_ANIM_TIME);
        int tempFlipAnimTime = a.getInteger(R.styleable.VScrollScreenLayout_vssl_flip_anim_time, DEFAULT_FLIP_ANIM_TIME);
        a.recycle();

        ySlideRatio = tempYSlideRate < 0f ? DEFAULT_Y_SLIDE_RATIO : tempYSlideRate;
        flipRatio = (tempFlipRatio <= 0f || tempFlipRatio >= 1.0f) ? DEFAULT_FLIP_RATIO : tempFlipRatio;
        reboundAnimTime = tempReboundAnimTime <= 0 ? DEFAULT_REBOUND_ANIM_TIME : tempReboundAnimTime;
        flipAnimTime = tempFlipAnimTime <= 0 ? DEFAULT_FLIP_ANIM_TIME : tempFlipAnimTime;
    }

    /**
     *
     * @return
     * @see #setySlideRatio(float)
     */
    public float getySlideRatio() {
        return ySlideRatio;
    }

    /**
     * <br>Set the ratio of sliding in Y axis.
     * <br>Default value is {@link #DEFAULT_Y_SLIDE_RATIO}.
     * @param ySlideRatio
     */
    public void setySlideRatio(float ySlideRatio) {
        this.ySlideRatio = ySlideRatio < 0f ? DEFAULT_Y_SLIDE_RATIO : ySlideRatio;
    }

    /**
     * @return
     * @see #setFlipRatio(float)
     */
    public float getFlipRatio() {
        return flipRatio;
    }

    /**
     * <br>The ratio of turning to the next page.
     * <br>For example:
     * <code>
     * <br>//distance----the minimum distance of starting to turn to the next page
     * <br>//pageHeight----the height of page
     * <br>distance = pageHeight * flipRatio
     * </code>
     *
     * @param flipRatio
     *         The value is in (0, 1).
     */
    public void setFlipRatio(@FloatRange(from = 0.0f, to = 1.0f) float flipRatio) {
        this.flipRatio = (flipRatio == 0f || flipRatio == 1.0f) ? DEFAULT_FLIP_RATIO : flipRatio;
    }

    /**
     * @return
     * @see #setReboundAnimTime(int)
     */
    public int getReboundAnimTime() {
        return reboundAnimTime;
    }

    /**
     * <br>Set the animation time of rebounding.
     * <br>Default value is {@link #DEFAULT_REBOUND_ANIM_TIME}.
     *
     * @param reboundAnimTime
     */
    public void setReboundAnimTime(int reboundAnimTime) {
        this.reboundAnimTime = reboundAnimTime <= 0 ? DEFAULT_REBOUND_ANIM_TIME : reboundAnimTime;
    }

    /**
     * @return
     * @see #setFlipAnimTime(int)
     */
    public int getFlipAnimTime() {
        return flipAnimTime;
    }

    /**
     * <br>Set the animation time of turning to the next page.
     * <br>Default value is {@link #DEFAULT_FLIP_ANIM_TIME}.
     *
     * @param flipAnimTime
     */
    public void setFlipAnimTime(int flipAnimTime) {
        this.flipAnimTime = flipAnimTime <= 0 ? DEFAULT_FLIP_ANIM_TIME : flipAnimTime;
    }

    public OnScrollPageChangedListener getOnScrollPageChangedListener() {
        return onScrollPageChangedListener;
    }

    /**
     * @param onScrollPageChangedListener
     */
    public void setOnScrollPageChangedListener(OnScrollPageChangedListener onScrollPageChangedListener) {
        this.onScrollPageChangedListener = onScrollPageChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {
            int width = getDefaultSize(0, widthMeasureSpec);
            pageHeight = getDefaultSize(0, heightMeasureSpec);
            setMeasuredDimension(width, pageHeight);
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            int totalHeight = pageHeight * getChildCount();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0, pageHeight * i, getWidth(), pageHeight * (i + 1));
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getChildCount() == 0)
            return super.onInterceptTouchEvent(ev);

        //如果正在滚动，则立即停止滚动
        if (!mScroller.isFinished())
            mScroller.abortAnimation();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                curPageIndex = getScrollY() / pageHeight;
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mark = (int) (mCurY - mLastY);
                mLastY = mCurY;
                //contentView内容在Y轴上可滑动，把事件传递给contentView内部
                if (getChildAt(curPageIndex).canScrollVertically(-mark))
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
        if (getChildCount() == 0)
            return super.onTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                curPageIndex = getScrollY() / pageHeight;
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mYOffset = (int) ((mCurY - mLastY) * ySlideRatio);
                scroll(mYOffset);
                mLastY = mCurY;
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
    }

    private void rebound() {
        int scrollY = getScrollY();
        int oldScrollY = curPageIndex * pageHeight;
        int diffY = scrollY - oldScrollY;
        int reboundHeight;
        int tempFlipAnimTime = reboundAnimTime;
        //向上翻页
        if (scrollY < oldScrollY) {
            //第一页（或者上翻距离不够最少翻页距离），松手回弹
            if (curPageIndex == 0 || Math.abs(diffY) < pageHeight * flipRatio)
                reboundHeight = diffY;
            else {//翻到上一页
                curPageIndex--;
                reboundHeight = scrollY - curPageIndex * pageHeight;
                tempFlipAnimTime = flipAnimTime;
                if (onScrollPageChangedListener != null)
                    onScrollPageChangedListener.onScroll(getContext(), curPageIndex);
            }
        }
        //向下翻页
        else {
            //最后一页（或者上翻距离不够最少翻页距离），松手回弹
            if (curPageIndex == getChildCount() - 1 || Math.abs(diffY) < pageHeight * flipRatio) {
                reboundHeight = diffY;
            } else {//翻到下一页
                curPageIndex++;
                reboundHeight = scrollY - curPageIndex * pageHeight;
                tempFlipAnimTime = flipAnimTime;
                if (onScrollPageChangedListener != null)
                    onScrollPageChangedListener.onScroll(getContext(), curPageIndex);
            }
        }
        mScroller.startScroll(0, scrollY, 0, -reboundHeight, tempFlipAnimTime);
        invalidate();
    }
}