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
        int left = 0;
        int top = 0;
        if (childCount > 0){
            switch (orientation){
                case HORIZONTAL:
                    int avWidth = (width - getPaddingLeft() - getPaddingRight()) / childCount;
                    for (int i = 0; i < childCount; i++) {
                        View child = getChildAt(i);
                        left = getPaddingLeft() + i * avWidth + (avWidth - child.getMeasuredWidth()) / 2;
                        top = (height - child.getMeasuredHeight()) / 2;
                        child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                    }
                    break;
                case VERTICAL:
                    int avHeight = (height - getPaddingTop() - getPaddingBottom()) / childCount;
                    for (int i = 0; i < childCount; i++) {
                        View child = getChildAt(i);
                        left = (width - child.getMeasuredWidth()) / 2;
                        top = getPaddingTop() + i * avHeight + (avHeight - child.getMeasuredHeight()) / 2;
                        child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                    }
                    break;
            }
        }
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
