package jsc.kit.component.stepview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jsc.kit.component.IViewAttrDelegate;
import jsc.kit.component.R;
import jsc.kit.component.refreshlayout.RefreshLayout;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class VerticalStepLinearLayout extends LinearLayout implements IViewAttrDelegate, DrawDelegate {

    public final static int LEFT = 0;
    public final static int RIGHT = 1;

    @IntDef({LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Location {

    }

    SparseArray<Float> yAxis = new SparseArray<>();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    Rect rect = new Rect();
    int lineColor;
    int indexColor;
    float indexRadius;
    int indexTextColor;
    float indexTextSize;
    int location;

    public VerticalStepLinearLayout(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public VerticalStepLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public VerticalStepLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalStepLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalStepLinearLayout, defStyleAttr, 0);
        lineColor = a.getColor(R.styleable.VerticalStepView_vs_line_color, 0xFFCCCCCC);
        indexColor = a.getColor(R.styleable.VerticalStepLinearLayout_vsll_indexColor, Color.BLUE);
        if (a.hasValue(R.styleable.VerticalStepLinearLayout_vsll_indexRadius))
            indexRadius = a.getDimensionPixelSize(R.styleable.VerticalStepLinearLayout_vsll_indexRadius, 0);
        if (indexRadius <= 0)
            indexRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
        indexTextColor = a.getColor(R.styleable.VerticalStepLinearLayout_vsll_indexTextColor, Color.WHITE);
        indexTextSize = a.getDimensionPixelSize(R.styleable.VerticalStepLinearLayout_vsll_indexTextSize, 0);
        if (indexTextSize <= 0)
            indexTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        location = a.getInt(R.styleable.VerticalStepLinearLayout_vsll_location, LEFT);
        a.recycle();
        setOrientation(VERTICAL);
        setWillNotDraw(false);
    }

    public void updateIndexAndLine(int lineColor, int indexColor, float indexRadius) {
        this.lineColor = lineColor;
        this.indexColor = indexColor;
        this.indexRadius = indexRadius;
        if (this.indexRadius <= 0)
            this.indexRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
        invalidate();
    }

    public void updateIndexText(int indexTextColor, float indexTextSize) {
        this.indexTextColor = indexTextColor;
        this.indexTextSize = indexTextSize;
        if (indexTextSize <= 0)
            this.indexTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        invalidate();
    }

    public void setLocation(@Location int location) {
        this.location = location;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        yAxis.clear();
        float y = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            y += params.topMargin + child.getMeasuredHeight() / 2.0f;
            yAxis.put(i, y);
            y += child.getMeasuredHeight() / 2.0f;
            y += params.bottomMargin;
            if (getShowDividers() == LinearLayout.SHOW_DIVIDER_MIDDLE && getDividerDrawable() != null) {
                y += getDividerDrawable().getIntrinsicHeight();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (yAxis.size() <= 1)
            return;

        float centerX = 0;
        switch (location) {
            case LEFT:
                centerX = Math.max(getPaddingLeft() / 2.0f, indexRadius);
                break;
            case RIGHT:
                centerX = getWidth() - Math.max(getPaddingRight() / 2.0f, indexRadius);
                break;
        }

        //TODO draw the vertical line
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(lineColor);
        drawLine(canvas, centerX);

        //TODO draw circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(indexColor);
        textPaint.setColor(indexTextColor);
        textPaint.setTextSize(indexTextSize);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        for (int i = 0; i < yAxis.size(); i++) {
            drawIndex(canvas, i, centerX, fontMetrics);
        }
    }

    @Override
    public void drawLine(@NonNull Canvas canvas, float centerX) {
        canvas.drawLine(centerX, yAxis.get(0), centerX, yAxis.get(yAxis.size() - 1), paint);
    }

    @Override
    public void drawIndex(@NonNull Canvas canvas, int index, float centerX, @NonNull Paint.FontMetrics fontMetrics) {
        canvas.drawCircle(centerX, yAxis.get(index), indexRadius, paint);

        String s = String.valueOf(index + 1);
        textPaint.getTextBounds(s, 0, s.length(), rect);
        float start = centerX - rect.width() / 2.0f;
        float baseLine = yAxis.get(index) - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(s, start, baseLine, textPaint);
    }
}
