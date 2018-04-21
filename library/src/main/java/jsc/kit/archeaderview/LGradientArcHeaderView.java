package jsc.kit.archeaderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import jsc.kit.R;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public class LGradientArcHeaderView extends BaseArcHeaderView {
    private int startColor;
    private int endColor;

    public LGradientArcHeaderView(Context context) {
        super(context);
    }

    public LGradientArcHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LGradientArcHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, TypedArray a) {
        startColor = a.getColor(R.styleable.BaseArcHeaderView_ahv_start_color, 0xFFFF3A80);
        endColor = a.getColor(R.styleable.BaseArcHeaderView_ahv_end_color, 0xFFFF3745);
    }

    @NonNull
    @Override
    protected Shader getShader() {
        return new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(), startColor, endColor, Shader.TileMode.MIRROR);
    }

    public int getStartColor() {
        return startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    /**
     * @param startColor
     * @param endColor
     */
    public void setColors(@ColorInt int startColor, @ColorInt int endColor) {
        resetShader();
        this.startColor = startColor;
        this.endColor = endColor;
        postInvalidate();
    }
}