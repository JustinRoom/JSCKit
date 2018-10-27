package jsc.kit.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jsc.kit.component.IViewAttrDelegate;
import jsc.kit.component.R;

/**
 * 均分布局。
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class AverageLayout extends ViewGroup implements IViewAttrDelegate{

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation{}
    private int orientation = HORIZONTAL;

    public AverageLayout(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public AverageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public AverageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AverageLayout, defStyleAttr, 0);
        orientation = a.getInt(R.styleable.AverageLayout_orientation, HORIZONTAL);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount > 0){
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            LayoutParams params = getLayoutParams();
            int width = 0;
            int height = 0;
            switch (orientation){
                case HORIZONTAL:
                    width = getPaddingLeft() + getPaddingRight();
                    for (int i = 0; i < childCount; i++) {
                        View child = getChildAt(i);
                        width += child.getMeasuredWidth();
                        height = Math.max(height, child.getMeasuredHeight());
                    }
                    height = height + getPaddingTop() + getPaddingBottom();
                    if (params.width == LayoutParams.WRAP_CONTENT){
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                    }
                    if (params.height == LayoutParams.WRAP_CONTENT){
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                    }
                    break;
                case VERTICAL:
                    height = getPaddingTop() + getPaddingBottom();
                    for (int i = 0; i < childCount; i++) {
                        View child = getChildAt(i);
                        height += child.getMeasuredHeight();
                        width = Math.max(width, child.getMeasuredWidth());
                    }
                    width = width + getPaddingLeft() + getPaddingRight();
                    if (params.height == LayoutParams.WRAP_CONTENT){
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                    }
                    if (params.width == LayoutParams.WRAP_CONTENT){
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                    }
                    break;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int left, top, right, bottom;
        if (childCount > 0){
            switch (orientation){
                case HORIZONTAL:
                    int[] widths = calculateAverageValue(width - getPaddingLeft() - getPaddingRight(), childCount);
                    int startX = getPaddingLeft();
                    for (int i = 0; i < childCount; i++) {
                        View child = getChildAt(i);
                        LayoutParams childParams = child.getLayoutParams();
                        if (childParams.width == LayoutParams.MATCH_PARENT) {
                            left = startX;
                            right = left + widths[i];
                        } else {
                            left = startX + (widths[i] - child.getMeasuredWidth()) / 2;
                            right = left + child.getMeasuredWidth();
                        }

                        if (childParams.height == LayoutParams.MATCH_PARENT) {
                            top = getPaddingTop();
                            bottom = height - getPaddingBottom();
                        } else {
                            top = (height - child.getMeasuredHeight()) / 2;
                            bottom = top + child.getMeasuredHeight();
                        }
                        child.layout(left, top, right, bottom);
                        startX += widths[i];
                    }
                    break;
                case VERTICAL:
                    int[] heights = calculateAverageValue(height - getPaddingTop() - getPaddingBottom(), childCount);
                    int startY = getPaddingTop();
                    for (int i = 0; i < childCount; i++) {
                        View child = getChildAt(i);
                        LayoutParams childParams = child.getLayoutParams();
                        if (childParams.width == LayoutParams.MATCH_PARENT) {
                            left = getPaddingLeft();
                            right = width - getPaddingRight();
                        } else {
                            left = (width - child.getMeasuredWidth()) / 2;
                            right = left + child.getMeasuredWidth();
                        }

                        if (childParams.height == LayoutParams.MATCH_PARENT) {
                            top = startY;
                            bottom = top + heights[i];
                        } else {
                            top = startY + (heights[i] - child.getMeasuredHeight()) / 2;
                            bottom = top + child.getMeasuredHeight();
                        }
                        child.layout(left, top, right, bottom);
                        startY += heights[i];
                    }
                    break;
            }
        }
    }

    private int[] calculateAverageValue(int value, int childCount) {
        int tempAverageValue = value / childCount;
        int rest = value - tempAverageValue * childCount;
        int[] averageValues = new int[childCount];
        for (int i = 0; i < childCount; i++) {
            if (rest > 0) {
                averageValues[i] = tempAverageValue + 1;
                rest --;
            } else {
                averageValues[i] = tempAverageValue;
            }
        }
        return averageValues;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        requestLayout();
    }

    public void setOrientation(@Orientation int orientation) {
        if (this.orientation == orientation)
            return;
        this.orientation = orientation;
        requestLayout();
    }
}
