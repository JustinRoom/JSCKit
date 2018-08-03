package jsc.kit.component.guide;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import jsc.kit.component.IViewAttrDelegate;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class GuideRippleView extends View implements IViewAttrDelegate {
    Paint paint;

    int circleCount;
    int[] colors;
    float circleSpace;
    float speed;

    float radius = 0;
    int startAlpha = 0xFF;
    boolean isRunning = false;
    int clipWidth, clipHeight;

    public GuideRippleView(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public GuideRippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public GuideRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GuideRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        initCirculars(3, new int[]{Color.BLUE, Color.BLUE, Color.BLUE}, 20, .6f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isRunning)
            return;

        if (clipWidth > 0 && clipHeight > 0){
            int clipLeft = (getWidth() - clipWidth) / 2;
            int clipTop = (getHeight() - clipHeight) / 2;
            canvas.clipRect(clipLeft, clipTop, clipLeft + clipWidth, clipTop + clipHeight);
        }

        float maxRadius = getWidth() / 2.0f;
        int alpha = (int) (startAlpha * (1 - radius / maxRadius) + .5f);
        for (int i = 0; i < circleCount; i++) {
            paint.setColor(colors[i]);
            paint.setAlpha(alpha);
            float tempRadius = radius - circleSpace * i;
            if (tempRadius <= 0)
                break;
            canvas.drawCircle(maxRadius, maxRadius, tempRadius, paint);
        }
        radius += speed;
        if (radius > maxRadius)
            radius = 0;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRipple();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopRipple();
        super.onDetachedFromWindow();
    }

    public void startRipple() {
        if (isRunning)
            return;

        isRunning = true;
        invalidate();
    }

    public void stopRipple() {
        isRunning = false;
    }

    /**
     * Set the ripple animation display area.
     *
     * @param clipWidth clip width
     * @param clipHeight clip height
     */
    public void setClip(int clipWidth, int clipHeight){
        this.clipWidth = clipWidth;
        this.clipHeight = clipHeight;
    }

    /**
     *
     * @param circleCount ripple circle count
     * @param colors colors
     * @param circleSpace the radius offset of two ripple circle
     * @param speed the speed of ripple animation
     */
    public void initCirculars(int circleCount, @NonNull int[] colors, float circleSpace, float speed) {
        this.circleCount = circleCount;
        this.colors = colors;
        this.circleSpace = circleSpace;
        this.speed = speed;
        if (this.colors.length < circleCount)
            throw new IllegalArgumentException("colors' length must be equal or more than circleCount.");
    }
}
