package jsc.kit.reboundlayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 布局上下反弹
 *
 * @author jsc
 */
public class ReboundFrameLayout extends FrameLayout {

    private final String TAG = getClass().getSimpleName();
    private final float RATIO = 0.65f;
    private final int ANIM_TIME = 300;

    private View mChildView;
    private float mLastY;
    private Scroller mScroller;
    private int mTouchSlop;

    public ReboundFrameLayout(Context context) {
        this(context, null);
    }

    public ReboundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChildView = getChildAt(0);
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
                int slop = Math.abs(mark);
                mLastY = mCurY;
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
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mYOffset = (int) ((mCurY - mLastY) * RATIO);
                scroll(mChildView, mYOffset);
                mLastY = mCurY;
                return true;

            case MotionEvent.ACTION_UP:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), ANIM_TIME);
                invalidate();
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


    private void scroll(View mChildView, int mYOffset){
        //为了方便下面的描述，我们用content代表childView的内容，即：content=childView的内容。
        //content下滑的高度
        int childScrolledY = mChildView.getScrollY();
        //content在childView中的可见区域
        Rect rect = new Rect();
        mChildView.getLocalVisibleRect(rect);
        //content的实际高度
        mChildView.measure(0, 0);
        int realHeight = mChildView.getMeasuredHeight();

        //content底部与childView底部对齐时需要向上滑动的距离。
        // 如果childView底部已向上经拉出屏幕外，则认为distanceFromBottom为0
        int distanceFromBottom = rect.bottom < 0 ? 0 : realHeight - rect.bottom;

        int distance = Math.abs(mYOffset);
        int scrollY = getScrollY();
        if (mYOffset > 0){//向下滑动
            //向下滑动childView与ReboundFrameLayout底部对齐
            scrollBy(0, -Math.min(distance, scrollY));
            distance = distance - scrollY;
            if (distance <= 0)
                return;

            //向下滑动content与childView顶部对齐
            mChildView.scrollBy(0, -Math.min(distance, childScrolledY));
            distance = distance - childScrolledY;
            if (distance <= 0)
                return;

            //向下滑动childView
            scrollBy(0, -distance);
        } else if (mYOffset < 0){//向上滑动
            //向上滑动childView与ReboundFrameLayout顶部对齐
            if (scrollY < 0){
                scrollY = Math.abs(scrollY);
                scrollBy(0, Math.min(distance, scrollY));
                distance = distance - scrollY;
            }

            if (distance <= 0)
                return;

            //向上滑动content与childView底部对齐
            mChildView.scrollBy(0, Math.min(distance, distanceFromBottom));
            distance = distance - distanceFromBottom;
            if (distance <= 0)
                return;

            //向上滑动childView
            scrollBy(0, distance);
        }
    }
}