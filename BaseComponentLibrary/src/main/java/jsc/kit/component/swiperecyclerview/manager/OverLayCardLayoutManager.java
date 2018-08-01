package jsc.kit.component.swiperecyclerview.manager;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class OverLayCardLayoutManager extends RecyclerView.LayoutManager {

    private final String TAG = "OverLayCard";
    /** 用于保存item的位置信息 */
    private SparseArray<Rect> rectSparseArray = new SparseArray<>();
    /** 用于保存item是否处于可见状态的信息 */
    private SparseBooleanArray stateBooleanArray = new SparseBooleanArray();

    private final static int DEFAULT_MAX_SHOW_COUNT = 10;
    Rect displayRect = new Rect();
    int maxShowCount = 0;
    int xOffsetAvg = 0;
    int yOffsetAvg = 0;
    float scale = 0.80f;

    public OverLayCardLayoutManager(int xOffsetAvg, int yOffsetAvg) {
        this(DEFAULT_MAX_SHOW_COUNT, xOffsetAvg, yOffsetAvg);
    }

    public OverLayCardLayoutManager(@IntRange(from = 1) int maxShowCount, int xOffsetAvg, int yOffsetAvg) {
        this.maxShowCount = maxShowCount;
        this.xOffsetAvg = xOffsetAvg;
        this.yOffsetAvg = yOffsetAvg;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        calculateAddChildren(recycler);
        recycleChildrenIfNecessary(recycler, state);
    }

    private void calculateAddChildren(RecyclerView.Recycler recycler){
        int itemCount = getItemCount();
        if (itemCount <= 0)
            return;

        displayRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        int childWidth = getWidth() - getPaddingLeft() - getPaddingRight() - (maxShowCount - 1) * xOffsetAvg;
        int childHeight = getHeight() - getPaddingTop() - getPaddingBottom() - (maxShowCount - 1) * yOffsetAvg;
        int startShowIndex = itemCount - maxShowCount;
        if (startShowIndex < 0){
            startShowIndex = 0;
        }
        for (int i = 0; i < itemCount; i++) {
            int xOffset = xOffsetAvg * (i - startShowIndex);
            int yOffset = yOffsetAvg * (i - startShowIndex);
            Rect rect = new Rect(xOffset, yOffset, xOffset + childWidth, yOffset + childHeight);
            rectSparseArray.put(i, rect);
            if (i >= startShowIndex){
//                double viewScale = (float) Math.pow(scale, itemCount -1 - i);
//                int scaleWidth = (int) (childWidth * viewScale);
//                int scaleHeight = (int) (childHeight * viewScale);
//                int newLeft = rect.left + (childWidth - scaleWidth) / 2;
//                int newTop = rect.top + (childHeight - scaleHeight) / 2;
//                int newRight = rect.left + scaleWidth;
//                int newBottom = rect.top + scaleHeight;
//                rect.set(rect.left, rect.top, newRight, newBottom);
//                rectSparseArray.put(i, rect);

                View child = recycler.getViewForPosition(i);
                //一定要测量child，否则child里面的东西是显示不出来的
                measureChildWithMargins(child, 0, 0);
                addView(child);
                layoutDecoratedWithMargins(child, rect.left, rect.top, rect.right, rect.bottom);
                child.getMatrix().postRotate((float) (Math.PI * 30 / 180), getWidth() / 2.0f, getHeight() / 2.0f);
                //更新该View的状态为未依附
                stateBooleanArray.put(i, true);
            }
        }
    }

    private void recycleChildrenIfNecessary(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }

        Rect childRect;
        for (int i = 0; i < getChildCount(); i++) {
            //这个方法获取的是RecyclerView中的View，注意区别Recycler中的View
            //下面几个方法能够获取每个View占用的空间的位置信息，包括ItemDecorator
            childRect = rectSparseArray.get(i);
            Log.i(TAG, "recycleChildrenIfNecessary: index = " + i);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayRect, childRect)) {
                //这获取的是实际的View
                View child = getChildAt(i);
                Log.i(TAG, "recycleChildrenIfNecessary: index = " + i + ", child = " + (child == null ? "null" : child.getId()));
                //移除并回收掉滑出屏幕的View
                removeAndRecycleView(child, recycler);
                //更新该View的状态为未依附
                stateBooleanArray.put(i, false);
            }
        }
    }


    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
