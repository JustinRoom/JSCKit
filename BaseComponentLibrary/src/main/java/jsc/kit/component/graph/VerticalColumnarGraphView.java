package jsc.kit.component.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 柱形图（竖直方向）
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class VerticalColumnarGraphView extends View {

    private final String TAG = "ColumnarGraphView";
    //1毫秒(ms)=1000000纳秒(ns)
    private final static long DEFAULT_CLICK_TIME = 1000_000000;
    private final static long DEFAULT_LONG_CLICK_TIME = 1_000;
    protected Paint paint;
    protected TextPaint textPaint;
    protected Rect clipRect = new Rect();
    protected Rect rect = new Rect();
    protected RectF rectF = new RectF();

    private String[] xAxisLabels = {"0", "25%", "50%", "75%", "100%"};
    private String[] yAxisLabels = {"0", "25%", "50%", "75%", "100%"};
    private int axisColor = 0xFFD8D8D8;//坐标颜色
    private int axisLabelTextColor = 0xFF666666;//坐标字体颜色
    private float axisLabelTextSize;//坐标字体大小
    private int space = 0;//柱形间隔
    private int column = 7;//柱形数目
    private int lOffset = 60;
    private int tOffset = 20;
    private int rOffset = 20;
    private int bOffset = 40;

    private List<ColumnarItem> items;//数据
    private String longestText = "";//items中最长的label（或value）
    private int lastSelectedIndex = -1;//上一次点击选中柱形
    private int selectedIndex = -1;//点击选中柱形
    private long pressedTimStamp;
    private boolean isPressed = false;
    private boolean isLongClicked;

    private OnColumnarItemClickListener onColumnarItemClickListener;
    private OnColumnarItemLongClickListener onColumnarItemLongClickListener;
    private Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            isLongClicked = true;
            performColumnarItemLongClick(selectedIndex);
        }
    };

    public VerticalColumnarGraphView(Context context) {
        this(context, null);
    }

    public VerticalColumnarGraphView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalColumnarGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        axisLabelTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

        //test items
        if (isInEditMode())
            setItems(createTestData());
    }

    private List<ColumnarItem> createTestData() {
        List<ColumnarItem> data = new ArrayList<>();
        float[] ratios = {.76f, .36f, .54f, .36f, .6f, .36f, .6f};
        int[] colors = {0xFFFFCF5E, 0xFFB4EE4D, 0xFF27E67B, 0xFF36C771, 0xFF1CA291, 0xFF24DDD0, 0xFf32CEF7};
        String[] labels = {"返情配种", "多次输精", "人工授精", "本交", "同精液配种", "已配种母猪", "其他配种"};
        String[] values = {"76头", "36头", "54头", "36头", "60头", "36头", "60头"};
        for (int i = 0; i < column; i++) {
            ColumnarItem item = new ColumnarItem();
            item.setColor(colors[i]);
            item.setRatio(ratios[i]);
            item.setLabel(labels[i]);
            item.setValue(values[i]);
            data.add(item);
        }
        return data;
    }

    /**
     * @param x                 touch x coordination
     * @param y                 touch y coordination
     * @param hExpandClickPixel expand click area on horizontal direction to increase clicking sensitivity
     * @return the index of selected item
     */
    private int getSelectedIndex(float x, float y, int hExpandClickPixel) {
        if (items == null || items.isEmpty())
            return -1;

        for (int i = 0; i < items.size(); i++) {
            ColumnarItem item = items.get(i);
            item.initRectF(rectF);
            float top = item.getTop();
            //为了增加柱状的点击灵明度，给定柱状的最小点击区域高度为space
            if (rectF.height() > 0 && rectF.height() < space)
                top = item.getBottom() - space;

            if (x >= (item.getLeft() - hExpandClickPixel)
                    && x <= (item.getRight() + hExpandClickPixel)
                    && y >= top
                    && y <= item.getBottom())
                return i;
        }
        return -1;
    }

    /**
     * initialize items
     *
     * @param items items source
     */
    public void setItems(List<ColumnarItem> items) {
        this.items = items;
        longestText = getMaxLengthString();
        invalidate();
    }

    /**
     * 个性化定制UI。
     *
     * @param builder builder
     */
    public void initCustomUI(Builder builder) {
        if (builder == null)
            return;
        xAxisLabels = builder.xAxisLabels;
        yAxisLabels = builder.yAxisLabels;
        axisColor = builder.axisColor;
        axisLabelTextColor = builder.axisLabelTextColor;
        axisLabelTextSize = builder.axisLabelTextSize;
        column = builder.column;
        lOffset = builder.leftOffset;
        tOffset = builder.topOffset;
        rOffset = builder.rightOffset;
        bOffset = builder.bottomOffset;
        if (axisLabelTextSize <= 0)
            axisLabelTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        invalidate();
    }

    public void setOnColumnarItemClickListener(OnColumnarItemClickListener onColumnarItemClickListener) {
        this.onColumnarItemClickListener = onColumnarItemClickListener;
    }

    public void setOnColumnarItemLongClickListener(OnColumnarItemLongClickListener onColumnarItemLongClickListener) {
        this.onColumnarItemLongClickListener = onColumnarItemLongClickListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isPressed) {
                    isPressed = true;
                    selectedIndex = getSelectedIndex(event.getX(), event.getY(), space / 3);

                    pressedTimStamp = System.nanoTime();
                    if (selectedIndex >= 0){
                        //Send a long click event message after DEFAULT_LONG_CLICK_TIME million seconds.
                        isLongClicked = false;
                        getHandler().postDelayed(longClickRunnable, DEFAULT_LONG_CLICK_TIME);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveIndex = getSelectedIndex(event.getX(), event.getY(), space / 3);
                if (moveIndex < 0) {
                    //Remove long click event.
                    getHandler().removeCallbacks(longClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                if (!isLongClicked){
                    //Remove long click event.
                    getHandler().removeCallbacks(longClickRunnable);

                    //deal click event
                    if (System.nanoTime() - pressedTimStamp <= DEFAULT_CLICK_TIME) {
                        pressedTimStamp = 0;
                        performColumnarItemClick(selectedIndex);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clipRect.set(getPaddingLeft() + lOffset, getPaddingTop() + tOffset, getWidth() - getPaddingRight() - rOffset, getHeight() - getPaddingBottom() - bOffset);
        drawAxis(canvas);
        float[] xAxisScales = calculateXScales(clipRect.left);
        float[] yAxisScales = calculateYScales(clipRect.bottom);
        drawXAxisScales(canvas, xAxisScales, 10);
        drawYAxisScales(canvas, yAxisScales, 10);

        clipRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        textPaint.setColor(axisLabelTextColor);
        textPaint.setTextSize(axisLabelTextSize);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        textPaint.setAlpha(0xFF);
        drawXAxisLabels(canvas, xAxisScales);
        drawYAxisLabels(canvas, yAxisScales);

        clipRect.set(getPaddingLeft() + lOffset, getPaddingTop() + tOffset, getWidth() - getPaddingRight() - rOffset, getHeight() - getPaddingBottom() - bOffset);
        drawItems(canvas);

        if (isPressed) {

        } else {
            drawSelectedItemDetailInfo(canvas);
        }

    }

    /**
     * 画坐标系
     *
     * @param canvas canvas
     */
    private void drawAxis(Canvas canvas) {
        paint.setColor(axisColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        //x axis
        canvas.drawLine(clipRect.left, clipRect.bottom, clipRect.right, clipRect.bottom, paint);
        //y axis
        canvas.drawLine(clipRect.left, clipRect.bottom, clipRect.left, clipRect.top, paint);
    }

    /**
     * 画X轴上的刻度线
     *
     * @param canvas      canvas
     * @param xAxisScales X轴上刻度线的X坐标集合
     * @param scaleHeight 刻度线的高度
     */
    private void drawXAxisScales(Canvas canvas, float[] xAxisScales, int scaleHeight) {
        if (xAxisLabels == null || xAxisLabels.length <= 1)
            return;
        for (int i = 1; i < xAxisLabels.length; i++) {
            canvas.drawLine(xAxisScales[i], clipRect.bottom, xAxisScales[i], clipRect.bottom + scaleHeight, paint);
        }
    }

    /**
     * 画Y轴上的刻度线
     *
     * @param canvas      canvas
     * @param yAxisScales Y轴上刻度线的Y坐标集合
     * @param scaleWidth  刻度线的宽度
     */
    private void drawYAxisScales(Canvas canvas, float[] yAxisScales, int scaleWidth) {
        if (yAxisLabels == null || yAxisLabels.length <= 1)
            return;
        for (int i = 1; i < yAxisLabels.length; i++) {
            canvas.drawLine(clipRect.left, yAxisScales[i], clipRect.left + scaleWidth, yAxisScales[i], paint);
        }
    }

    /**
     * 计算X轴上刻度线的X坐标集合
     *
     * @param originX 坐标系原点X坐标
     * @return X轴上刻度线的X坐标集合
     */
    private float[] calculateXScales(int originX) {
        int xAxisLabelCount = xAxisLabels == null ? 0 : xAxisLabels.length;
        float[] xAxisScales = new float[xAxisLabelCount];
        if (xAxisLabelCount > 1) {
            xAxisScales[0] = originX;
            float xDegreeScaleWidth = clipRect.width() * 1.0f / (xAxisLabelCount - 1);
            for (int i = 1; i < xAxisLabelCount; i++) {
                xAxisScales[i] = originX + xDegreeScaleWidth * i;
            }
        }
        return xAxisScales;
    }

    /**
     * 计算Y轴上刻度线的Y坐标集合
     *
     * @param originY 坐标系原点Y坐标
     * @return Y轴上刻度线的Y坐标集合
     */
    private float[] calculateYScales(int originY) {
        int yAxisLabelCount = yAxisLabels == null ? 0 : yAxisLabels.length;
        float[] yAxisScales = new float[yAxisLabelCount];
        if (yAxisLabelCount > 1) {
            yAxisScales[0] = originY;
            float yDegreeScaleHeight = clipRect.height() * 1.0f / (yAxisLabelCount - 1);
            for (int i = 1; i < yAxisLabelCount; i++) {
                yAxisScales[i] = originY - yDegreeScaleHeight * i;
            }
        }
        return yAxisScales;
    }

    /**
     * 画X轴上的刻度
     *
     * @param canvas      canvas
     * @param xAxisScales X轴上刻度线的X坐标集合
     */
    private void drawXAxisLabels(Canvas canvas, float[] xAxisScales) {
        if (xAxisLabels == null)
            return;

        for (int i = 0; i < xAxisLabels.length; i++) {
            String axisLabel = xAxisLabels[i];
            if (axisLabel == null || axisLabel.trim().length() == 0)
                continue;

            textPaint.getTextBounds(axisLabel, 0, axisLabel.length(), rect);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float basicY = clipRect.bottom - rect.height() / 2.0f;
            float x = xAxisScales[i] - rect.width() / 2.0f;
            float baseLine = basicY - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(axisLabel, 0, axisLabel.length(), x, baseLine, textPaint);
        }
    }

    /**
     * 画Y轴上的刻度
     *
     * @param canvas      canvas
     * @param yAxisScales Y轴上刻度线的Y坐标集合
     */
    private void drawYAxisLabels(Canvas canvas, float[] yAxisScales) {
        if (yAxisLabels == null)
            return;

        for (int i = 0; i < yAxisLabels.length; i++) {
            String axisLabel = yAxisLabels[i];
            if (axisLabel == null || axisLabel.trim().length() == 0)
                continue;

            textPaint.getTextBounds(axisLabel, 0, axisLabel.length(), rect);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float basicY = yAxisScales[i];
            float x = clipRect.left;
            float baseLine = basicY - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(axisLabel, 0, axisLabel.length(), x, baseLine, textPaint);
        }
    }

    /**
     * 画柱形
     *
     * @param canvas canvas
     */
    private void drawItems(Canvas canvas) {
        if (items == null || items.isEmpty())
            return;

        paint.setStyle(Paint.Style.FILL);
        int chartWidth = calculateSuitableChartWidth(clipRect.width());
        space = chartWidth;
        int chartHeight = clipRect.height();
        for (int i = 0; i < items.size(); i++) {
            ColumnarItem item = items.get(i);
            if (item == null)
                continue;

            paint.setColor(item.getColor());
            float ratioHeight = chartHeight * item.getRatio();
            item.setLeft(clipRect.left + space * (i + 1) + chartWidth * i);
            item.setTop(clipRect.top + chartHeight - ratioHeight);
            item.setRight(item.getLeft() + chartWidth);
            item.setBottom(clipRect.bottom);
            item.initRectF(rectF);
            if (rectF.height() > 0) {
                if (i == selectedIndex) {
                    paint.setAlpha(0xFF);
                    drawSelectedItem(canvas, item, rectF, paint);
                } else {
                    paint.setAlpha(selectedIndex < 0 ? 0xFF : 0x66);
                    canvas.drawRoundRect(rectF, 4, 4, paint);
                }
            }
        }
        paint.setAlpha(0xFF);
    }

    /**
     * Calculate suitable chart width.
     *
     * @param width columnar area width
     * @return chart width
     */
    private int calculateSuitableChartWidth(int width) {
        return width / (column * 2 + 1);
    }

    /**
     * 画选中的柱形
     *
     * @param canvas canvas
     * @param item   columnar item
     * @param rectF  the rectF of selected columnar
     * @param paint  paint
     */
    protected void drawSelectedItem(Canvas canvas, ColumnarItem item, RectF rectF, Paint paint) {
        canvas.drawRoundRect(rectF, 4, 4, paint);
    }

    /**
     * 画选中柱形后显示的相关详细信息。
     * 如果需展示自己的效果，请重写此方法。
     *
     * @param canvas canvas
     */
    protected void drawSelectedItemDetailInfo(Canvas canvas) {
        if (items == null || items.isEmpty())
            return;

        if (selectedIndex < 0)
            return;

        int padding = 20;
        int dotRadius = 10;
        int margin = 16;
        ColumnarItem selectedItem = items.get(selectedIndex);
        selectedItem.initRectF(rectF);

        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textPaint.getTextBounds(longestText, 0, longestText.length(), rect);
        int ch = rect.height();
        int bgw = dotRadius * 2 + padding * 2 + margin + rect.width();
        int bgh = padding * 2 + ch * 2 + margin;

        float left = rectF.right - (bgw + rectF.width()) / 2.0f;
        float bottom = rectF.top - 10;
        if (rectF.height() > 0 && rectF.height() < space) {
            bottom = rectF.bottom - space - 10;
        }
        RectF clipRectF = new RectF(left, bottom - bgh, left + bgw, bottom);
        if (selectedIndex == 0) {
            clipRectF.left = rectF.left;
            clipRectF.right = rectF.left + bgw;
        } else if (selectedIndex == items.size() - 1) {
            clipRectF.right = rectF.right;
            clipRectF.left = rectF.right - bgw;
        }

        if (clipRectF.top < 0) {
            clipRectF.top = 0;
            clipRectF.bottom = bgh;
        }

        //画背景
        paint.setColor(0xFF485465);
        canvas.drawRoundRect(clipRectF, 6, 6, paint);
        //画圆点
        paint.setColor(selectedItem.getColor());
        canvas.drawCircle(clipRectF.left + padding + dotRadius, clipRectF.top + padding + dotRadius, dotRadius, paint);

        textPaint.setColor(Color.WHITE);
        //画标签
        String label = selectedItem.getLabel();
        if (label != null && label.trim().length() > 0) {
            textPaint.setAlpha(0x99);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float x = clipRectF.left + padding + dotRadius * 2 + margin;
            float basicY = clipRectF.top + padding + dotRadius;
            float baseLine = basicY - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(label, 0, label.length(), x, baseLine, textPaint);
        }
        //画值
        String value = selectedItem.getValue();
        if (value != null && value.trim().length() > 0) {
            textPaint.setAlpha(0xFF);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float x = clipRectF.left + padding + dotRadius * 2 + margin;
            float basicY = clipRectF.top + padding + rect.height() + margin + rect.height() / 2.0f;
            float baseLine = basicY - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(value, 0, value.length(), x, baseLine, textPaint);
        }
    }

    /**
     * Get the longest string of {@link #items}.
     *
     * @return the longest string of {@link #items}
     */
    private String getMaxLengthString() {
        if (items == null || items.isEmpty())
            return "";
        String result = "";
        for (int i = 0; i < items.size(); i++) {
            String itemStr = getItemMaxLengthString(items.get(i));
            int resultLen = getHalfStringLength(result);
            int itemLen = getHalfStringLength(itemStr);
            result = resultLen > itemLen ? result : itemStr;
        }
        return result;
    }

    /**
     * Get the longest string of columnar item.
     *
     * @param item columnar item
     * @return the longest string. One of {@link ColumnarItem#label}, {@link ColumnarItem#value}.
     */
    private String getItemMaxLengthString(ColumnarItem item) {
        int labelLen = getHalfStringLength(item.getLabel());
        int valueLen = getHalfStringLength(item.getValue());
        return labelLen > valueLen ? item.getLabel() : item.getValue();
    }

    /**
     * 半角长度获取
     *
     * @param ss 源字符串
     * @return 字符串半角长度
     */
    public int getHalfStringLength(String ss) {
        if (ss == null || ss.length() == 0)
            return 0;

        try {
            return ss.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void performColumnarItemClick(int selectedIndex) {
        if (lastSelectedIndex != selectedIndex) {
            lastSelectedIndex = selectedIndex;
            invalidate();
            ColumnarItem item = selectedIndex < 0 ? null : items.get(selectedIndex);
            if (onColumnarItemClickListener != null && item != null) {
                onColumnarItemClickListener.onColumnarItemClick(this, selectedIndex, item);
            }
        }
    }

    protected void performColumnarItemLongClick(int selectedIndex) {
        if (lastSelectedIndex >= 0)
            return;

        ColumnarItem item = selectedIndex < 0 ? null : items.get(selectedIndex);
        if (onColumnarItemLongClickListener != null && item != null) {
            onColumnarItemLongClickListener.onColumnarItemLongClick(this, selectedIndex, item);
        }
    }

    public interface OnColumnarItemClickListener {

        /**
         * @param view          view
         * @param selectedIndex the selected index。It represents no item was selected when return -1.
         * @param selectedItem  the selected item
         */
        void onColumnarItemClick(VerticalColumnarGraphView view, int selectedIndex, @Nullable ColumnarItem selectedItem);
    }

    public interface OnColumnarItemLongClickListener {

        /**
         * @param view          view
         * @param selectedIndex the selected index。It represents no item was selected when return -1.
         * @param selectedItem  the selected item
         */
        void onColumnarItemLongClick(VerticalColumnarGraphView view, int selectedIndex, @Nullable ColumnarItem selectedItem);
    }

    public static class Builder {
        String[] yAxisLabels;
        String[] xAxisLabels;
        int axisColor;//坐标系以及刻度线颜色
        int axisLabelTextColor;//刻度字体颜色
        float axisLabelTextSize;//刻度字体大小
        int column;//柱形数目
        int leftOffset;
        int topOffset;
        int rightOffset;
        int bottomOffset;

        public Builder() {
            axisColor = 0xFFD8D8D8;
            axisLabelTextColor = 0xFF666666;
            column = 7;
            leftOffset = 60;
            topOffset = 20;
            rightOffset = 20;
            bottomOffset = 20;
        }

        public Builder setYAxisLabels(String[] yAxisLabels) {
            this.yAxisLabels = yAxisLabels;
            return this;
        }

        public Builder setXAxisLabels(String[] xAxisLabels) {
            this.xAxisLabels = xAxisLabels;
            return this;
        }

        public Builder setAxisColor(@ColorInt int axisColor) {
            this.axisColor = axisColor;
            return this;
        }

        public Builder setAxisLabelTextColor(@ColorInt int axisLabelTextColor) {
            this.axisLabelTextColor = axisLabelTextColor;
            return this;
        }

        public Builder setAxisLabelTextSize(float axisLabelTextSize) {
            this.axisLabelTextSize = axisLabelTextSize;
            return this;
        }

        /**
         * 柱形数目决定了柱形的宽度。
         * <p>{@code chartWidth = width / (column * 2 + 1)}
         *
         * @param column column count
         * @return builder
         */
        public Builder setColumn(@IntRange(from = 1) int column) {
            this.column = column;
            return this;
        }

        public Builder setLeftOffset(@IntRange(from = 0) int leftOffset) {
            this.leftOffset = leftOffset;
            return this;
        }

        public Builder setTopOffset(@IntRange(from = 0) int topOffset) {
            this.topOffset = topOffset;
            return this;
        }

        public Builder setRightOffset(@IntRange(from = 0) int rightOffset) {
            this.rightOffset = rightOffset;
            return this;
        }

        public Builder setBottomOffset(@IntRange(from = 0) int bottomOffset) {
            this.bottomOffset = bottomOffset;
            return this;
        }


        /**
         * 设置XY轴刻度
         *
         * @param xAxisLabels x axis labels
         * @param yAxisLabels y axis labels
         * @return builder
         */
        public Builder setAxisLabels(String[] xAxisLabels, String[] yAxisLabels) {
            this.xAxisLabels = xAxisLabels;
            this.yAxisLabels = yAxisLabels;
            return this;
        }

        /**
         * 设置XY轴刻度字体大小
         *
         * @param context context
         * @param unit    unit
         * @param value   value
         * @return builder
         * @see TypedValue#applyDimension(int, float, DisplayMetrics)
         */
        public Builder setAxisLabelTextSize(@NonNull Context context, int unit, float value) {
            this.axisLabelTextSize = TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
            return this;
        }

        /**
         * 设置四个方向的偏移量。
         * <br>偏移量用来留出空间画刻度。
         *
         * @param leftOffset   left offset
         * @param topOffset    top offset
         * @param rightOffset  right offset
         * @param bottomOffset bottom offset
         * @return builder
         */
        public Builder setOffset(@IntRange(from = 0) int leftOffset, @IntRange(from = 0) int topOffset, @IntRange(from = 0) int rightOffset, @IntRange(from = 0) int bottomOffset) {
            this.leftOffset = leftOffset;
            this.topOffset = topOffset;
            this.rightOffset = rightOffset;
            this.bottomOffset = bottomOffset;
            return this;
        }
    }
}
