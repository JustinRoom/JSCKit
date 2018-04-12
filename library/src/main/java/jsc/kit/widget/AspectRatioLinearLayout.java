package jsc.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import jsc.kit.R;
/**
 * <p>
 *     自定义宽高比例的LinearLayout，例如2:5、4:3等等。
 *     默认为1:1。
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public class AspectRatioLinearLayout extends LinearLayout {
    private int baseWhat;
    private int xAspect;
    private int yAspect;
    public AspectRatioLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public AspectRatioLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioLinearLayout, defStyleAttr, 0);
        baseWhat = a.getInt(R.styleable.AspectRatioFrameLayout_base_what, 0);
        xAspect = a.getInteger(R.styleable.AspectRatioFrameLayout_x_aspect, 1);
        yAspect = a.getInteger(R.styleable.AspectRatioFrameLayout_y_aspect, 1);
        a.recycle();

        if (xAspect <= 0)
            xAspect = 1;
        if (yAspect <= 0)
            yAspect = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int width;
        int height;
        switch (baseWhat){
            case 0://width
                width = getMeasuredWidth();
                height = width * yAspect / xAspect;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                break;
            case 1:
                height = getMeasuredHeight();
                width = height * xAspect / yAspect;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getBaseWhat() {
        return baseWhat;
    }

    public void setBaseWhat(@IntRange(from = 0, to = 1) int baseWhat) {
        this.baseWhat = baseWhat;
        requestLayout();
    }

    public int getxAspect() {
        return xAspect;
    }

    public void setxAspect(@IntRange(from = 1) int xAspect) {
        this.xAspect = xAspect;
        requestLayout();
    }

    public int getyAspect() {
        return yAspect;
    }

    public void setyAspect(@IntRange(from = 1) int yAspect) {
        this.yAspect = yAspect;
        requestLayout();
    }
}
