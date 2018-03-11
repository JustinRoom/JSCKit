package jsc.kit.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jsc.kit.R;

/**
 * Email:1006368252@qq.com
 * QQ:1006368252
 *
 * @author jiangshicheng
 */
public class VerticalStepView extends View {

    private List<RouteViewPoint> points = new ArrayList<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Rect textBoundRect = new Rect();//测量文字的Rect

    private int lineWidth;
    private int lineColor;
    private boolean drawCircle;
    private boolean drawIndex;
    private boolean drawTransit;
    private boolean drawLabel;
    private boolean drawCursor;

    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalStepView, defStyleAttr, 0);
        lineWidth = a.getDimensionPixelSize(R.styleable.VerticalStepView_vs_line_width, 1);
        lineColor = a.getColor(R.styleable.VerticalStepView_vs_line_color, 0xFFCCCCCC);
        drawCircle = a.getBoolean(R.styleable.VerticalStepView_vs_draw_circle, true);
        drawIndex = a.getBoolean(R.styleable.VerticalStepView_vs_draw_index, true);
        drawTransit = a.getBoolean(R.styleable.VerticalStepView_vs_draw_transit, true);
        drawLabel = a.getBoolean(R.styleable.VerticalStepView_vs_draw_label, true);
        drawCursor = a.getBoolean(R.styleable.VerticalStepView_vs_draw_cursor, true);
        a.recycle();
        init();
    }

    private void init() {
        float textSize8 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics());
        float textSize10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        float textSize12 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

        String[] transits = {"", "16路", "", ""};
        String[] labels = {"起点", "东圃(站)", "珠村(站)", "终点"};
        String[] cursors = {"", "上","下", ""};
        int[] distances = {0, 80, 80, 80};
        int len = labels.length;
        for (int i = 0; i < len; i++) {
            RouteViewPoint point = new RouteViewPoint();
            //圆
            point.setRadius(15);
            point.setBorderColor(0xffcccccc);
            point.setBackgroundColor(0xffcccccc);
            point.setDistance(distances[i]);

            //index
            point.setIndex(String.valueOf(i + 1));
            point.setIndexSize(textSize8);
//            point.setIndexColor(Color.RED);

            //transit
            point.setTransit(transits[i]);
            point.setTransitSize(textSize10);
            point.setTransitColor(Color.RED);

            //label
            point.setLabel(labels[i]);
            point.setLabelSize(textSize12);

            //cursor
            point.setCursor(cursors[i]);
            point.setCursorSize(textSize8);
            point.setCursorColor(0xFFFF00FF);

            points.add(point);
        }
    }

    public void setPoints(List<RouteViewPoint> points) {
        this.points.clear();
        if (points != null)
            this.points.addAll(points);
        requestLayout();
        postInvalidate();
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        postInvalidate();
    }

    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
        postInvalidate();
    }

    public boolean isDrawCircle() {
        return drawCircle;
    }

    public void setDrawCircle(boolean drawCircle) {
        this.drawCircle = drawCircle;
        postInvalidate();
    }

    public boolean isDrawIndex() {
        return drawIndex;
    }

    public void setDrawIndex(boolean drawIndex) {
        this.drawIndex = drawIndex;
        postInvalidate();
    }

    public boolean isDrawTransit() {
        return drawTransit;
    }

    public void setDrawTransit(boolean drawTransit) {
        this.drawTransit = drawTransit;
        requestLayout();
        postInvalidate();
    }

    public boolean isDrawLabel() {
        return drawLabel;
    }

    public void setDrawLabel(boolean drawLabel) {
        this.drawLabel = drawLabel;
        postInvalidate();
    }

    public boolean isDrawCursor() {
        return drawCursor;
    }

    public void setDrawCursor(boolean drawCursor) {
        this.drawCursor = drawCursor;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int offsetY = getPaddingTop();
        int startX = (int) (getPaddingLeft() + 10 + getMaxRadius() + 0.5f);
        if (isDrawTransit())
            startX = startX + getMaxTransitWidth();
        if (points.size() > 1) {
            for (int i = 0; i < points.size(); i++) {
                RouteViewPoint p = points.get(i);
                if (i == 0)
                    offsetY += p.getRadius();

                if (i > 0)
                    offsetY += p.getDistance();

                p.setBasicX(startX);
                p.setBasicY(offsetY);

                if (i == points.size() - 1)
                    offsetY += p.getRadius();
            }
        } else if (points.size() == 1) {
            RouteViewPoint p = points.get(0);
            offsetY += p.getRadius();
            p.setBasicY(offsetY);
            offsetY += p.getRadius();
        }

        offsetY += getPaddingBottom();

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(offsetY, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (points.size() > 1) {
            paint.setStrokeWidth(lineWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(lineColor);
            canvas.drawLine(
                    points.get(0).getBasicX(),
                    points.get(0).getBasicY(),
                    points.get(points.size() - 1).getBasicX(),
                    points.get(points.size() - 1).getBasicY(),
                    paint
            );
        }

        for (RouteViewPoint p : points) {
            if (isDrawCircle())
                drawCircle(canvas, p, paint);
            if (isDrawIndex())
                drawIndex(canvas, p, textPaint);
            if (isDrawLabel())
                drawLabel(canvas, p, 10, textPaint);
            if (isDrawTransit())
                drawTransit(canvas, p, 10, textPaint);
            if (isDrawCursor())
                drawCursor(canvas, p, 10, textPaint);
        }
    }

    /**
     * 画icon
     *
     * @param canvas
     * @param drawable
     * @param pointXPos
     * @param pointYPos
     * @param pointRadius
     */
    private void drawIcon(Canvas canvas, Drawable drawable, int pointXPos, int pointYPos, float pointRadius) {
        if (drawable == null)
            return;
        int marginRight = 10;//icon与point右边距离

        int left = (int) (pointXPos - pointRadius - marginRight - drawable.getIntrinsicWidth() + 0.5f);
        int top = (int) (pointYPos - drawable.getIntrinsicHeight() / 2.0f + 0.5f);
        int right = left + drawable.getIntrinsicWidth();
        int bottom = top + drawable.getIntrinsicHeight();
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    /**
     * 画圆
     *
     * @param canvas
     * @param p
     * @param paint
     */
    private void drawCircle(Canvas canvas, RouteViewPoint p, Paint paint) {
        if (p == null)
            return;

        paint.setColor(p.getBackgroundColor());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(p.getBasicX(), p.getBasicY(), p.getRadius(), paint);

        paint.setColor(p.getBorderColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(p.getBorderWidth());
        canvas.drawCircle(p.getBasicX(), p.getBasicY(), p.getRadius(), paint);
    }

    /**
     * 画序号
     *
     * @param canvas
     * @param p
     * @param textPaint
     */
    private void drawIndex(Canvas canvas, RouteViewPoint p, TextPaint textPaint) {
        if (p == null)
            return;

        String index = p.getIndex();
        if (index == null || index.length() == 0)
            return;

        textPaint.setColor(p.getIndexColor());
        textPaint.setTextSize(p.getIndexSize());
        textPaint.setTypeface(Typeface.defaultFromStyle(p.isIndexBold() ? Typeface.BOLD : Typeface.NORMAL));
        textPaint.getTextBounds(index, 0, index.length(), textBoundRect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int w = textBoundRect.right - textBoundRect.left;
        int h = textBoundRect.bottom - textBoundRect.top;
        float xStart = p.getBasicX() - w / 2.0f;
        float baseLine = p.getBasicY() - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(index, xStart, baseLine, textPaint);
    }

    /**
     * 画标签
     *
     * @param canvas
     * @param p
     * @param marginLeft
     *         文字与圆左边的距离
     * @param textPaint
     */
    private void drawLabel(Canvas canvas, RouteViewPoint p, int marginLeft, TextPaint textPaint) {
        if (p == null)
            return;
        String label = p.getLabel();
        if (label == null || label.length() == 0)
            return;

        textPaint.setColor(p.getLabelColor());
        textPaint.setTextSize(p.getLabelSize());
        textPaint.setTypeface(Typeface.defaultFromStyle(p.isLabelBold() ? Typeface.BOLD : Typeface.NORMAL));
        textPaint.getTextBounds(label, 0, label.length(), textBoundRect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int w = textBoundRect.right - textBoundRect.left;
        int h = textBoundRect.bottom - textBoundRect.top;
        float xStart = p.getBasicX() + getMaxRadius() + marginLeft;
        float baseLine = p.getBasicY() - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(label, xStart, baseLine, textPaint);
    }

    /**
     * 画公交
     *
     * @param canvas
     * @param p
     * @param marginRight
     *         文字与圆右边的距离
     * @param textPaint
     */
    private void drawTransit(Canvas canvas, RouteViewPoint p, int marginRight, TextPaint textPaint) {
        if (p == null)
            return;

        String transit = p.getTransit();
        if (transit == null || transit.length() == 0)
            return;

        textPaint.setColor(p.getTransitColor());
        textPaint.setTextSize(p.getTransitSize());
        textPaint.setTypeface(Typeface.defaultFromStyle(p.isTransitBold() ? Typeface.BOLD : Typeface.NORMAL));
        textPaint.getTextBounds(transit, 0, transit.length(), textBoundRect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int w = textBoundRect.right - textBoundRect.left;
        int h = textBoundRect.bottom - textBoundRect.top;
        float xStart = p.getBasicX() - w - p.getRadius() - marginRight;
        float baseLine = p.getBasicY() - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(transit, xStart, baseLine, textPaint);
    }

    /**
     * 画游标
     *
     * @param canvas
     * @param p
     * @param marginLeft
     *         文字与圆右边的距离
     * @param textPaint
     */
    private void drawCursor(Canvas canvas, RouteViewPoint p, int marginLeft, TextPaint textPaint) {
        if (p == null)
            return;

        String cursor = p.getCursor();
        if (cursor == null || cursor.length() == 0)
            return;

//        String label = p.getLabel();
//        Rect rect = new Rect();
//        textPaint.setColor(p.getLabelColor());
//        textPaint.setTextSize(p.getLabelSize());
//        textPaint.setTypeface(Typeface.defaultFromStyle(p.isLabelBold() ? Typeface.BOLD : Typeface.NORMAL));
//        textPaint.getTextBounds(label, 0, label.length(), rect);

        textPaint.setColor(p.getCursorColor());
        textPaint.setTextSize(p.getCursorSize());
        textPaint.setTypeface(Typeface.defaultFromStyle(p.isCursorBold() ? Typeface.BOLD : Typeface.NORMAL));
        textPaint.getTextBounds(cursor, 0, cursor.length(), textBoundRect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int w = textBoundRect.right - textBoundRect.left;
        int h = textBoundRect.bottom - textBoundRect.top;
        float xStart = p.getBasicX() + getMaxRadius() + marginLeft;
        float newBasicY = p.getBasicY() + p.getRadius() + h / 2.0f;
        float baseLine = newBasicY - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//        float xStart = p.getBasicX() + getMaxRadius() + marginLeft + 10 + rect.right - rect.left;
//        float baseLine = p.getBasicY() - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(cursor, xStart, baseLine, textPaint);
    }

    private float getMaxRadius() {
        float maxRadius = 0;
        for (RouteViewPoint p : points) {
            maxRadius = Math.max(maxRadius, p.getRadius());
        }
        return maxRadius;
    }

    private int getMaxTransitWidth() {
        int maxTransitWidth = 0;
        for (RouteViewPoint p : points) {
            String transit = p.getTransit();
            if (transit == null || transit.length() == 0)
                continue;
            textPaint.setTextSize(p.getTransitSize());
            textPaint.getTextBounds(transit, 0, transit.length(), textBoundRect);
            maxTransitWidth = Math.max(maxTransitWidth, textBoundRect.right - textBoundRect.left);
        }
        return maxTransitWidth;
    }
}
