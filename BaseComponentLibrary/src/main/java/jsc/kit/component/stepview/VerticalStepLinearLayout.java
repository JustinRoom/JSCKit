package jsc.kit.component.stepview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    public final static int SORT_BASE_TOP = 0;
    public final static int SORT_BASE_FIRST = 1;

    @IntDef({LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Location {

    }

    @IntDef({SORT_BASE_TOP, SORT_BASE_FIRST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortBase {

    }

    SparseArray<Float> yShowAxis = new SparseArray<>();
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
    int scrollY = 0;
    int sortBase;
    private int minToTop;

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
        sortBase = a.getInt(R.styleable.VerticalStepLinearLayout_vsll_sortBase, SORT_BASE_TOP);
        a.recycle();
        setOrientation(VERTICAL);
        setWillNotDraw(false);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        updateDelay(10);
    }

    public void setIndexColor(int indexColor) {
        this.indexColor = indexColor;
        updateDelay(10);
    }

    public void setIndexRadius(float indexRadius) {
        this.indexRadius = indexRadius;
        updateDelay(10);
    }

    public void setIndexTextColor(int indexTextColor) {
        this.indexTextColor = indexTextColor;
        updateDelay(10);
    }

    public void setIndexTextSize(float indexTextSize) {
        this.indexTextSize = indexTextSize;
        updateDelay(10);
    }

    private void updateDelay(long delay){
        removeCallbacks(updateRunnable);
        postDelayed(updateRunnable, delay);
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    /**
     *
     * @param location location. One of {@link #LEFT}、{@link #RIGHT}.
     */
    public void setLocation(@Location int location) {
        if (this.location == location)
            return;

        this.location = location;
        updateDelay(10);
    }

    /**
     *
     * @param sortBase sort base. One of {@link #SORT_BASE_TOP}、{@link #SORT_BASE_FIRST}.
     */
    public void setSortBase(@SortBase int sortBase) {
        if (this.sortBase == sortBase)
            return;

        this.sortBase = sortBase;
        sortIndex(true);
    }

    /**
     *
     * @param scrollY scroll distance on y axis.
     */
    public void updateScroll(int scrollY) {
        this.scrollY = scrollY;
        if (yShowAxis.size() < 2)
            return;
        sortIndex(true);
    }

    private void sortIndex(boolean invalidate) {
        switch (sortBase) {
            case SORT_BASE_TOP:
                sortIndexBaseOnTop();
                break;
            case SORT_BASE_FIRST:
                sortIndexBaseOnFirst();
                break;
        }
        if (invalidate)
            invalidate();
    }

    private void sortIndexBaseOnTop() {
        yShowAxis.clear();
        float minDistance = indexRadius * 2 + 10;
        for (int i = 0; i < yAxis.size(); i++) {
            if (i == 0) {
                yShowAxis.put(i, Math.max(minToTop + indexRadius + scrollY, yAxis.get(i)));
            } else {
                float pre = yShowAxis.get(yShowAxis.size() - 1);
                if (yAxis.get(i) - pre < minDistance)
                    yShowAxis.put(i, pre + minDistance);
                else
                    yShowAxis.put(i, yAxis.get(i));
            }
        }
    }

    private void sortIndexBaseOnFirst() {
        yShowAxis.clear();
        float minDistance = indexRadius * 2 + 10;
        yShowAxis.put(0, yAxis.get(0) + scrollY);
        for (int i = 1; i < yAxis.size(); i++) {
            float pre = yShowAxis.get(yShowAxis.size() - 1);
            if (yAxis.get(i) - pre < minDistance)
                yShowAxis.put(i, pre + minDistance);
            else
                yShowAxis.put(i, yAxis.get(i));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        //LinearLayout自带的分割线属性
        Drawable drawable = null;
        if (getShowDividers() == LinearLayout.SHOW_DIVIDER_MIDDLE) {
            drawable = getDividerDrawable();
        }
        yAxis.clear();
        float y = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //child的上下margin
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            y += params.topMargin + child.getMeasuredHeight() / 2.0f;
            if (i == 0){
                minToTop = getPaddingTop() + params.topMargin;
            }
            yAxis.put(i, y);
            y += child.getMeasuredHeight() / 2.0f;
            y += params.bottomMargin;
            //考虑到LinearLayout自带的分割线属性
            if (drawable != null) {
                y += drawable.getIntrinsicHeight();
            }
        }
        sortIndex(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (yShowAxis.size() <= 1)
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
        for (int i = 0; i < yShowAxis.size(); i++) {
            drawIndex(canvas, i, centerX, fontMetrics);
        }
    }

    @Override
    public void drawLine(@NonNull Canvas canvas, float centerX) {
        canvas.drawLine(centerX, yShowAxis.get(0), centerX, yShowAxis.get(yShowAxis.size() - 1), paint);
    }

    @Override
    public void drawIndex(@NonNull Canvas canvas, int index, float centerX, @NonNull Paint.FontMetrics fontMetrics) {
        canvas.drawCircle(centerX, yShowAxis.get(index), indexRadius, paint);

        String s = String.valueOf(index + 1);
        textPaint.getTextBounds(s, 0, s.length(), rect);
        float start = centerX - rect.width() / 2.0f;
        float baseLine = yShowAxis.get(index) - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(s, start, baseLine, textPaint);
    }
}
