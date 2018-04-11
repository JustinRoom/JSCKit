package jsc.kit.rippleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import jsc.kit.R;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class RippleView extends View {

    private Paint mPaint;
    private float minRadius;
    private float maxRadius = -1;
    private float radiusStep = 1.0f;
    private float curRadius;
    private boolean running = false;
    private AnimListener animListener;
    private int repeatCount = 0;
    private int repeatIndex = 0;
    private boolean autoRunOnAttached;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleView, defStyleAttr, 0);
        int color = a.getColor(R.styleable.RippleView_rv_color, Color.BLUE);
        minRadius = a.getDimensionPixelSize(R.styleable.RippleView_rv_mini_radius, 0);
        if (minRadius < 0)
            minRadius = 0;
        radiusStep = a.getDimensionPixelSize(R.styleable.RippleView_rv_radius_step, 1);
        if (radiusStep == 0)
            radiusStep = 1.0f;
        repeatCount = a.getInteger(R.styleable.RippleView_rv_repeat_count, -1);
        autoRunOnAttached = a.getBoolean(R.styleable.RippleView_rv_auto_run_on_attached, true);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (autoRunOnAttached)
            start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * Set the start color of an animation.
     *
     * @param color
     */
    public void setColor(@ColorInt int color) {
        mPaint.setColor(color);
        if (!running)
            invalidate();
    }

    public float getMinRadius() {
        return minRadius;
    }

    /**
     * Set animation's the minimum radius. The default value is 0.
     *
     * @param minRadius more than or equal 0.
     */
    public void setMinRadius(float minRadius) {
        this.minRadius = minRadius;
        if (this.minRadius < 0)
            this.minRadius = 0;

        if (!running) {
            curRadius = this.minRadius;
            invalidate();
        }
    }

    /**
     * Set animation radius' radius_step. The default value is 1px.
     *
     * @param radiusStep zero is invalid.
     */
    public void setRadiusStep(float radiusStep) {
        this.radiusStep = radiusStep;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    /**
     * Please call {@link #start()} after set repeat count value.
     * <br/>The default value is -1;
     *
     * @param repeatCount An negative value presenters infinite loop.
     */
    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        reset();
    }

    public boolean isAutoRunOnAttached() {
        return autoRunOnAttached;
    }

    /**
     * It runs animation automatically if pass <code>true</code> value.
     * <br/>The default value is true.
     *
     * @param autoRunOnAttached
     * @see #onAttachedToWindow()
     */
    public void setAutoRunOnAttached(boolean autoRunOnAttached) {
        this.autoRunOnAttached = autoRunOnAttached;
    }

    /**
     * Set animation listener.
     *
     * @param animListener
     */
    public void setAnimListener(AnimListener animListener) {
        this.animListener = animListener;
    }

    /**
     * start animation.
     */
    public void start() {
        if (running)
            return;
        running = true;
        //第一次动画开始
        if (animListener != null) {
            animListener.onAnimationStart();
        }
        invalidate();
    }

    /**
     * stop animation.
     */
    public void stop() {
        reset();
        if (animListener != null)
            animListener.onAnimationStop();
    }

    private void reset() {
        curRadius = minRadius;
        running = false;
        repeatIndex = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centerXY = getWidth() / 2.0f;
        if (maxRadius == -1) {
            maxRadius = centerXY;
        }

        int alpha = (int) (255 * 0.5f * (1.0 - curRadius / maxRadius) + 0.5f);
        mPaint.setAlpha(alpha);
        canvas.drawCircle(centerXY, centerXY, curRadius, mPaint);

        if (!running)
            return;

        curRadius += radiusStep;
        if (radiusStep > 0){//波纹从里往外扩展
            if (curRadius > maxRadius) {
                curRadius = minRadius;
                repeatIndex++;
                if (doListener())
                    return;
            }
        } else {//波纹由外往里收缩
            if (curRadius < 0) {
                curRadius = minRadius;
                repeatIndex++;
                if (doListener())
                    return;
            }
        }
        invalidate();
    }

    private boolean doListener() {
        //无限循环模式
        if (repeatCount < 0) {
            if (animListener != null)
                animListener.onAnimationRepeat(repeatIndex);
            return false;
        }

        //循环repeatCount次
        if (repeatIndex == repeatCount + 1) {
            reset();
            if (animListener != null)
                animListener.onAnimationStop();
            return true;
        }

        if (animListener != null)
            animListener.onAnimationRepeat(repeatIndex);
        return false;
    }

    public interface AnimListener {

        void onAnimationStart();

        void onAnimationRepeat(int repeatIndex);

        void onAnimationStop();
    }
}