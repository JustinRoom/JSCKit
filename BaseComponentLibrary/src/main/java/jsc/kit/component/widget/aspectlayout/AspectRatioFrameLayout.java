package jsc.kit.component.widget.aspectlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import jsc.kit.component.IViewAttrDelegate;
import jsc.kit.component.R;
/**
 * <p>
 *     自定义宽高比例的FrameLayout，例如2:5、4:3等等。
 *     默认为1:1。
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class AspectRatioFrameLayout extends FrameLayout implements IViewAttrDelegate, IAspect {
    private AspectRatioHelper helper = null;

    public AspectRatioFrameLayout(@NonNull Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public AspectRatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public AspectRatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout, defStyleAttr, 0);
        int baseWhat = a.getInt(R.styleable.AspectRatioFrameLayout_base_what, 0);
        int aspectX = a.getInteger(R.styleable.AspectRatioFrameLayout_aspect_y, 1);
        int aspectY = a.getInteger(R.styleable.AspectRatioFrameLayout_aspect_y, 1);
        a.recycle();
        helper = new AspectRatioHelper(baseWhat, aspectX, aspectY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        switch (helper.getBaseWhat()){
            case AspectRatioHelper.BASE_HORIZONTAL:
                heightMeasureSpec = helper.calHeightMeasureSpec(getMeasuredWidth());
                break;
            case AspectRatioHelper.BASE_VERTICAL:
                widthMeasureSpec = helper.calWidthMeasureSpec(getMeasuredHeight());
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getBaseWhat() {
        return helper.getBaseWhat();
    }

    @Override
    public void setBaseWhat(@AspectRatioHelper.BaseWhat int baseWhat) {
        helper.setBaseWhat(baseWhat);
        requestLayout();
    }

    @Override
    public int getAspectX() {
        return helper.getAspectX();
    }

    @Override
    public void setAspectX(@IntRange(from = 1) int aspectX) {
        helper.setAspectX(aspectX);
        requestLayout();
    }

    @Override
    public int getAspectY() {
        return helper.getAspectY();
    }

    @Override
    public void setAspectY(@IntRange(from = 1) int aspectY) {
        helper.setAspectY(aspectY);
        requestLayout();
    }
}
