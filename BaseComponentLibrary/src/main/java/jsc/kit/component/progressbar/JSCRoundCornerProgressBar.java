package jsc.kit.component.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class JSCRoundCornerProgressBar extends View {

    private RectF rectF = new RectF();
    private Rect rect = new Rect();
    private Paint paint;
    private int rcBackgroundColor;
    private int rcProgressColor;
    private int rcSecondProgressColor;
    private int rcMax;
    private int rcProgress;
    private int rcSecondProgress;

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String rcText;
    private int rcTextColor;
    private float rcTextSize;

    public JSCRoundCornerProgressBar(Context context) {
        this(context, null);
    }

    public JSCRoundCornerProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSCRoundCornerProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JSCRoundCornerProgressBar);
        rcBackgroundColor = a.getColor(R.styleable.JSCRoundCornerProgressBar_rc_background_color, Color.TRANSPARENT);
        rcSecondProgressColor = a.getColor(R.styleable.JSCRoundCornerProgressBar_rc_second_progress_color, Color.GREEN);
        rcProgressColor = a.getColor(R.styleable.JSCRoundCornerProgressBar_rc_progress_color, Color.YELLOW);

        rcMax = a.getInt(R.styleable.JSCRoundCornerProgressBar_rc_max, 100);
        rcSecondProgress = a.getInt(R.styleable.JSCRoundCornerProgressBar_rc_second_progress, 0);
        rcProgress = a.getInt(R.styleable.JSCRoundCornerProgressBar_rc_progress, 0);

        rcText = a.getString(R.styleable.JSCRoundCornerProgressBar_rc_text);
        rcTextColor = a.getColor(R.styleable.JSCRoundCornerProgressBar_rc_text_color, 0xFF333333);
        rcTextSize = a.getDimension(R.styleable.JSCRoundCornerProgressBar_rc_text_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        a.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setRcText(String rcText) {
        setRcText(rcText, rcTextColor);
    }

    public void setRcText(String rcText, int rcTextColor) {
        setRcText(rcText, rcTextColor, rcTextSize);
    }

    public void setRcText(String rcText, int rcTextColor, float rcTextSize) {
        this.rcText = rcText;
        this.rcTextColor = rcTextColor;
        this.rcTextSize = rcTextSize;
        postInvalidate();
    }

    public void setRcProgresses(int rcProgress) {
        setRcProgresses(rcMax, rcProgress);
    }

    public void setRcProgresses(int rcMax, int rcProgress) {
        setRcProgresses(rcMax, rcSecondProgress, rcProgress);
    }

    public void setRcProgresses(int rcMax, int rcSecondProgress, int rcProgress) {
        this.rcMax = rcMax;
        this.rcSecondProgress = rcSecondProgress;
        this.rcProgress = rcProgress;
        postInvalidate();
    }

    public void setRcColors(int rcBackgroundColor, int rcProgressColor, int rcSecondProgressColor) {
        this.rcBackgroundColor = rcBackgroundColor;
        this.rcProgressColor = rcProgressColor;
        this.rcSecondProgressColor = rcSecondProgressColor;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rcBackgroundRadius = getHeight() / 2.0f;

        //backgroundColor
        rectF.set(0, 0, getWidth(), getHeight());
        paint.setColor(rcBackgroundColor);
        canvas.drawRoundRect(rectF, rcBackgroundRadius, rcBackgroundRadius, paint);

        //secondProgress
        drawProgress(canvas, rcSecondProgress, rcSecondProgressColor);

        //progress
        drawProgress(canvas, rcProgress, rcProgressColor);

        //text
        drawText(canvas, getWidth(), getHeight(), rcText, rcTextColor, rcTextSize, textPaint);
    }

    /**
     * progress
     *
     * @param canvas canvas
     */
    private void drawProgress(Canvas canvas, int progress, int progressColor) {
        if (progress <= 0)
            return;

        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingLeft(), getHeight() - getPaddingBottom());
        int curProgressWidth;
        if (progress >= rcMax)
            curProgressWidth = (int) (rectF.width() + 0.5f);
        else
            curProgressWidth = (int) (rectF.width() * (progress * 1.0f / rcMax) + 0.5f);
        rect.set(getPaddingLeft(), getPaddingTop(), curProgressWidth + getPaddingLeft(), getHeight() - getPaddingBottom());
        boolean clip = curProgressWidth < rectF.height() / 2.0f || curProgressWidth > (rectF.width() - rectF.height() / 2.0f);
        paint.setColor(progressColor);
        drawProgressbar(canvas, clip, rectF, rect, paint);
    }

    private void drawProgressbar(Canvas canvas, boolean clip, RectF rectF, Rect rect, Paint paint) {
        float radius = rectF.height() / 2.0f;
        if (clip) {
            canvas.save();
            canvas.clipRect(rect);
            canvas.drawRoundRect(rectF, radius, radius, paint);
            canvas.restore();
        } else {
            rectF.set(rect);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
    }

    private void drawText(Canvas canvas, int w, int h, String text, int textColor, float textSize, TextPaint textPaint) {
        if (text == null || text.length() == 0)
            return;

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.getTextBounds(text, 0, text.length(), rect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float baseLine = (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, (w - rect.width()) / 2.0f, baseLine, textPaint);
    }
}
