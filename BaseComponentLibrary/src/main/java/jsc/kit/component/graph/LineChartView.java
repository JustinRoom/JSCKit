package jsc.kit.component.graph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import jsc.kit.component.IViewAttrDelegate;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class LineChartView extends View implements IViewAttrDelegate {

    private static final String TAG = "LineChartView";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private LabelItem[] xLabels = null;
    private LabelItem[] yLabels = null;
    private float defaultTextSize;
    private ColumnarItem[] data = null;
    private int xSpace;
    private int ySpace;
    private Rect clipRect = new Rect();
    private int visibleWidth;
    private int from, to;
    private Path path = new Path();
    private boolean canMove = false;
    private float lastTouchX = 0.0f;

    private boolean showDashGrid = true;
    private DashPathEffect dashPathEffect = new DashPathEffect(new float[]{8, 4, 8, 4}, 1.0f);

    private boolean showScale = true;
    private float scaleLength = 5.0f;

    private int axisColor = Color.BLUE;
    private int backgroundColor = Color.WHITE;

    public LineChartView(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, context.getResources().getDisplayMetrics());
        ySpace = xSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());
        if (isInEditMode())
            showTestData();
    }

    private void showTestData(){
        int size = 48;
        xLabels = new LabelItem[size];
        for (int i = 0; i < xLabels.length; i++) {
            LabelItem item = new LabelItem();
            item.setLabel(i + "月");
            xLabels[i] = item;
        }

        String[] labels = new String[]{"", "20%", "40%", "60%", "80%", "100%"};
        yLabels = new LabelItem[labels.length];
        for (int i = 0; i < labels.length; i++) {
            LabelItem item = new LabelItem();
            item.setLabel(labels[i]);
            yLabels[i] = item;
        }

        data = new ColumnarItem[size];
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            ColumnarItem item = new ColumnarItem();
            item.setRatio(random.nextFloat());
            data[i] = item;
        }
    }

    public void applyTestData(){
        showTestData();
        requestLayout();
    }

    public void setXLabels(LabelItem[] xLabels) {
        setXLabels(xLabels, xSpace);
    }

    /**
     *
     * @param xLabels the x labels
     * @param xSpace the space between two x labels
     */
    public void setXLabels(LabelItem[] xLabels, @IntRange(from = 0) int xSpace) {
        this.xLabels = xLabels;
        this.xSpace = xSpace;
        requestLayout();
        setScrollX(0);
    }

    public void setYLabels(LabelItem[] yLabels) {
        setYLabels(yLabels, ySpace);
    }

    /**
     *
     * @param yLabels the y labels
     * @param ySpace the space between two y labels
     */
    public void setYLabels(LabelItem[] yLabels, @IntRange(from = 0) int ySpace) {
        this.yLabels = yLabels;
        this.ySpace = ySpace;
        requestLayout();
        setScrollX(0);
    }

    /**
     * Show data.
     *
     * @param data data
     */
    public void setData(ColumnarItem[] data) {
        this.data = data;
        invalidate();
        setScrollX(0);
    }

    /**
     * Show dash grid or not.
     * @param showDashGrid true, show dash grid, else false.
     */
    public void setShowDashGrid(boolean showDashGrid) {
        if (this.showDashGrid == showDashGrid)
            return;
        this.showDashGrid = showDashGrid;
        invalidate();
    }

    /**
     * Show scales of xy coordinate axis or not.
     *
     * @param showScale true, show scales of xy coordinate axis , else false.
     */
    public void setShowScale(boolean showScale) {
        this.showScale = showScale;
        invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    public void toggleDashGrid() {
        showDashGrid = !showDashGrid;
        invalidate();
    }

    public boolean isShowDashGrid() {
        return showDashGrid;
    }

    public boolean isShowScale() {
        return showScale;
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            runReboundAnimation();
        }
    };

    private void runReboundAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v = (int) animation.getAnimatedValue();
                setScrollX(v);
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float touchX = event.getX();
                float touchY = event.getY();
                if (touchX < clipRect.left
                        || touchX > clipRect.right
                        || touchY < clipRect.top
                        || touchY > clipRect.bottom){
                    canMove = false;
                } else {
                    removeCallbacks(r);
                    lastTouchX = touchX;
                    canMove = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (canMove){
                    float tempTouchX = event.getX();
                    float dx = lastTouchX - tempTouchX;
                    scrollBy((int) (dx + .5f), 0);
                    lastTouchX = tempTouchX;
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX < 0){
                    from = scrollX;
                    to = 0;
                    if (canMove)
                        post(r);
                } else if (scrollX > 0 && (scrollX + visibleWidth > getMeasuredWidth())){
                    from = scrollX;
                    to = getMeasuredWidth() - visibleWidth;
                    if (canMove)
                        post(r);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        visibleWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: [width = " + visibleWidth + ", height = " + height + "]");
        int realWidth = 0;
        int realHeight = 0;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (xLabels != null && xLabels.length > 0)
            realWidth = (int) (getPaddingLeft() + getPaddingRight() + xSpace * (xLabels.length - 1) + .5f);
        if (yLabels != null && yLabels.length > 0)
            realHeight = (int) (getPaddingTop() + getPaddingBottom() + ySpace * (yLabels.length - 1) + 0.5f);

        //compute suitable width
        if (params.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(realWidth, MeasureSpec.EXACTLY);
        } else {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(visibleWidth, realWidth), MeasureSpec.EXACTLY);
        }

        //compute suitable height
        if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(realHeight, MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(height, realHeight), MeasureSpec.EXACTLY);
            int yCount = yLabels == null ? 0 : yLabels.length;
            yCount = Math.max(1, yCount);
            ySpace = height / yCount;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        clipRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());

        paint.setColor(axisColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        //draw x coordinate axis
        canvas.drawLine(clipRect.left, clipRect.bottom, clipRect.right, clipRect.bottom, paint);
        int xLabelCount = xLabels == null ? 0 : xLabels.length;
        int width = (xLabelCount - 1) * xSpace;
        for (int i = 0; i < xLabelCount; i++) {
            float basicX = clipRect.left + i * xSpace;
            //draw x labels
            drawXLabel(canvas, xLabels[i], basicX, clipRect.bottom, 10, textPaint);
            //draw x scale]
            if (isShowScale()) {
                canvas.drawLine(basicX, clipRect.bottom, basicX, clipRect.bottom + scaleLength, paint);
            }
        }

        //draw y coordinate axis
        int scrollX = 0;//getScrollX();
        canvas.drawLine(clipRect.left + scrollX, clipRect.top, clipRect.left + scrollX, clipRect.bottom, paint);
        int yLabelCount = yLabels == null ? 0 : yLabels.length;
        int height = (yLabelCount - 1) * ySpace;
        for (int i = 0; i < yLabelCount; i++) {
            float basicY = clipRect.bottom - i * ySpace;
            //draw y labels
            drawYLabel(canvas, yLabels[i], clipRect.left + scrollX, basicY, -10, textPaint);
            //draw y scale
            if (isShowScale()) {
                canvas.drawLine(clipRect.left, basicY, clipRect.left - scaleLength, basicY, paint);
            }
        }

        //draw dash grid
        drawDashGrid(canvas, height, xLabelCount, yLabelCount);

        //draw data
        drawData(canvas, height, 1, Color.RED, 4.0f, Color.GREEN);

        //draw y coordinate axis for floating
        drawFloatingYAxis(canvas, getScrollX(), yLabelCount);
    }

    /**
     * Draw dash grid.
     *
     * @param canvas canvas
     * @param height the height of valid y coordinate axis
     * @param xLabelCount the size of {@link #xLabels}
     * @param yLabelCount the size of {@link #yLabels}
     */
    private void drawDashGrid(Canvas canvas, int height, int xLabelCount, int yLabelCount){
        if (!isShowDashGrid())
            return;

        paint.setPathEffect(dashPathEffect);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int x, y, x1, y1;
        //draw x dash line
        for (int i = 1; i < xLabelCount; i++) {
            path.reset();
            x = clipRect.left + i * xSpace;
            y = clipRect.bottom;
            x1 = x;
            y1 = y - height;
            path.moveTo(x, y);
            path.lineTo(x1, y1);
            canvas.drawPath(path, paint);
//            canvas.drawLine(x, y, x1, y1, paint);
        }

        //draw y dash line
        for (int i = 1; i < yLabelCount; i++) {
            path.reset();
            x = clipRect.left;
            y = clipRect.bottom - i * ySpace;
            x1 = clipRect.right;
            y1 = y;
            path.moveTo(x, y);
            path.lineTo(x1, y1);
            canvas.drawPath(path, paint);
//            canvas.drawLine(x, y, x1, y1, paint);
        }
        paint.setPathEffect(null);
    }

    /**
     * Draw data.
     *
     * @param canvas canvas
     * @param height the height of valid y coordinate axis
     * @param lineWidth the width of line
     * @param lineColor the color of line
     * @param dotRadius the radius of dot
     * @param dotColor the color of dot
     */
    private void drawData(Canvas canvas, int height, float lineWidth, int lineColor, float dotRadius, int dotColor){
        int dataCount = data == null ? 0 : data.length;
        if (dataCount <= 0)
            return;

        PointF[] points = new PointF[dataCount];
        path.reset();
        float x, y;
        for (int i = 0; i < dataCount; i++) {
            ColumnarItem item = data[i];
            x = clipRect.left + i * xSpace;
            y = clipRect.bottom;
            if (item != null)
                y = y - height * item.getRatio();
            if (i == 0)
                path.moveTo(x, y);
            else
                path.lineTo(x, y);
            points[i] = new PointF(x, y);
        }
        if (dataCount > 1){
            paint.setColor(lineColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            canvas.drawPath(path, paint);
        }

        paint.setColor(dotColor);
        paint.setStyle(Paint.Style.FILL);
        for (PointF p: points) {
            canvas.drawCircle(p.x, p.y, dotRadius, paint);
        }
    }

    /**
     * Draw floating coordinate axis of y when scrolled on horizontal direction.
     *
     * @param canvas canvas
     * @param scrollX scroll distance on horizontal direction
     * @param yLabelCount the size of {@link #yLabels}
     */
    private void drawFloatingYAxis(Canvas canvas, int scrollX, int yLabelCount){
        if (scrollX <=0)
            return;

        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, clipRect.left + scrollX, clipRect.bottom, paint);

        paint.setColor(axisColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawLine(clipRect.left + scrollX, clipRect.top, clipRect.left + scrollX, clipRect.bottom, paint);
        for (int i = 0; i < yLabelCount; i++) {
            float basicY = clipRect.bottom - i * ySpace;
            //draw y labels
            drawYLabel(canvas, yLabels[i], clipRect.left + scrollX, basicY, -10, textPaint);
            //draw y scale]
            if (isShowScale()) {
                canvas.drawLine(clipRect.left + scrollX, basicY, clipRect.left + scrollX - scaleLength, basicY, paint);
            }
        }
    }


    private void drawXLabel(Canvas canvas, LabelItem item, float basicX, float basicY, float offsetY, TextPaint paint) {
        if (item == null)
            return;

        String label = item.getLabel();
        if (label == null || label.length() == 0)
            return;

        paint.setColor(item.getColor());
        paint.setTextSize(item.getSize() <= 0 ? defaultTextSize : item.getSize());
        Rect rect = new Rect();
        paint.getTextBounds(label, 0, label.length(), rect);
        float x = basicX - rect.width() / 2.0f;
        float y = basicY + rect.height() + offsetY;
        canvas.drawText(label, 0, label.length(), x, y, paint);
    }

    private void drawYLabel(Canvas canvas, LabelItem item, float basicX, float basicY, float offsetX, TextPaint paint) {
        if (item == null)
            return;

        String label = item.getLabel();
        if (label == null || label.length() == 0)
            return;

        paint.setColor(item.getColor());
        paint.setTextSize(item.getSize() <= 0 ? defaultTextSize : item.getSize());
        Rect rect = new Rect();
        paint.getTextBounds(label, 0, label.length(), rect);
        float x = basicX - rect.width() + offsetX;
        float y = basicY + rect.height() / 2.0f;
        canvas.drawText(label, 0, label.length(), x, y, paint);
    }
}
