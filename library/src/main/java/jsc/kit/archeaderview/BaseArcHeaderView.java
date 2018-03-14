package jsc.kit.archeaderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jsc.kit.R;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public abstract class BaseArcHeaderView extends View {
    public static final int DIRECTION_DOWN_OUT_SIDE = 0;
    public static final int DIRECTION_DOWN_IN_SIDE = 1;

    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Path mPath = new Path();
    protected int arcHeight;// 圆弧高度
    protected int direction;
    private Shader shader;

    @IntDef({DIRECTION_DOWN_OUT_SIDE, DIRECTION_DOWN_IN_SIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    @NonNull
    protected abstract Shader getShader();

    protected abstract void init(Context context, TypedArray a);

    protected final void resetShader() {
        shader = null;
    }

    public BaseArcHeaderView(Context context) {
        this(context, null);
    }

    public BaseArcHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseArcHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseArcHeaderView, defStyleAttr, 0);
        arcHeight = a.getDimensionPixelSize(R.styleable.BaseArcHeaderView_ahv_height, 0);
        direction = a.getInt(R.styleable.BaseArcHeaderView_ahv_direction, DIRECTION_DOWN_OUT_SIDE);
        init(context, a);
        a.recycle();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(@Direction int direction) {
        this.direction = direction;
        postInvalidate();
    }

    public int getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetShader();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calPath();
        if (shader == null)
            shader = getShader();
        mPaint.setShader(shader);
        canvas.drawPath(mPath, mPaint);
    }

    private void calPath() {
        int w = getWidth();
        int h = getHeight();
        mPath.reset();
        mPath.moveTo(0, 0);
        switch (direction) {
            case DIRECTION_DOWN_OUT_SIDE:
                mPath.lineTo(0, h - arcHeight);
                mPath.quadTo(w / 2.0f, h + arcHeight, w, h - arcHeight);
                mPath.lineTo(w, 0);
                break;
            case DIRECTION_DOWN_IN_SIDE:
                mPath.lineTo(0, h);
                mPath.quadTo(w / 2.0f, h - arcHeight * 2, w, h);
                mPath.lineTo(w, 0);
                break;
        }
        mPath.close();
    }
}