package jsc.kit.rippleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RippleView extends View {

    private Paint mPaint;
    private float minRadius = 0;
    private float maxRadius = -1;
    private float step = 2f;
    private float curRadius = minRadius;
    private boolean running = false;
    private AnimListener animListener;
    private int repeatCount = 0;
    private int repeatIndex = 0;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void setColor(@ColorInt int color) {
        mPaint.setColor(color);
        if (!running)
            invalidate();
    }

    public void setMinRadius(float minRadius) {
        this.minRadius = minRadius;
        if (!running) {
            curRadius = minRadius;
            invalidate();
        }
    }

    public void setStep(float step) {
        this.step = step;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        repeatIndex = 0;
    }

    public void setAnimListener(AnimListener animListener) {
        this.animListener = animListener;
    }

    public void start() {
        if (running)
            return;
        running = true;
        //第一次动画开始
        if (animListener != null){
            animListener.onAnimationStart();
        }
        invalidate();
    }

    public void stop() {
        reset();
        if (animListener != null)
            animListener.onAnimationStop();
    }

    private void reset(){
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

        curRadius += step;
        if (curRadius > maxRadius) {
            curRadius = minRadius;
            repeatIndex++;
            if (doListener())
                return;
        }
        invalidate();
    }

    private boolean doListener(){
        //无限循环模式
        if (repeatCount < 0){
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