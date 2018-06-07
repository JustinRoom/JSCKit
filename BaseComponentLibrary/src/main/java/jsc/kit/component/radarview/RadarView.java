package jsc.kit.component.radarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jsc.kit.component.R;

public class RadarView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private List<List<RadarPoint>> pointLists = new ArrayList<>();
    private List<RadarEntity> radarEntities = new ArrayList<>();
    private int startAngle;
    private int layerCount;
    private int layerStyle;
    private int layerStrokeWidth;
    private int layerColor;
    private int outputColor;
    private boolean vertexShow;
    private int vertexColor;
    private int vertexRadius;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadarView, defStyleAttr, 0);
        startAngle = a.getInteger(R.styleable.RadarView_rv_start_angle, 0);
        int tempLayerCount = a.getInteger(R.styleable.RadarView_rv_layer_count, 4);
        layerCount = Math.abs(tempLayerCount);
        if (layerCount < 1)
            layerCount = 1;
        layerStyle = a.getInt(R.styleable.RadarView_rv_layer_style, 0);
        int tempLineWidth = a.getDimensionPixelSize(R.styleable.RadarView_rv_layer_stroke_width, 1);
        layerStrokeWidth = Math.abs(tempLineWidth);
        if (layerStrokeWidth < 1)
            layerStrokeWidth = 1;
        layerColor = a.getColor(R.styleable.RadarView_rv_layer_color, 0xFFCCCCCC);
        outputColor = a.getColor(R.styleable.RadarView_rv_output_color, 0x66FF4081);
        vertexShow = a.getBoolean(R.styleable.RadarView_rv_vertex_show, false);
        vertexColor = a.getColor(R.styleable.RadarView_rv_vertex_color, Color.CYAN);
        int tempDotRadius = a.getDimensionPixelSize(R.styleable.RadarView_rv_vertex_radius, 8);
        vertexRadius = Math.abs(tempDotRadius);
        if (vertexRadius <= 0)
            vertexRadius = 8;
        a.recycle();
    }

    public void setRadarEntities(List<RadarEntity> radarEntities) {
        this.radarEntities = radarEntities;
        postInvalidate();
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        postInvalidate();
    }

    public int getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(@IntRange(from = 1) int layerCount) {
        this.layerCount = layerCount;
        postInvalidate();
    }

    public int getLayerStyle() {
        return layerStyle;
    }

    /**
     * 0 represents {@link Paint.Style#STROKE}<br>
     * 1 represents {@link Paint.Style#FILL}<br>
     *
     * @param layerStyle the style of paint
     */
    public void setLayerStyle(@IntRange(from = 0, to = 1) int layerStyle) {
        this.layerStyle = layerStyle;
        postInvalidate();
    }

    public int getLayerStrokeWidth() {
        return layerStrokeWidth;
    }

    public void setLayerStrokeWidth(@IntRange(from = 1) int layerStrokeWidth) {
        this.layerStrokeWidth = layerStrokeWidth;
        postInvalidate();
    }

    public int getLayerColor() {
        return layerColor;
    }

    public void setLayerColor(@ColorInt int layerColor) {
        this.layerColor = layerColor;
        postInvalidate();
    }

    public int getOutputColor() {
        return outputColor;
    }

    public void setOutputColor(@ColorInt int outputColor) {
        this.outputColor = outputColor;
        postInvalidate();
    }

    public boolean isVertexShow() {
        return vertexShow;
    }

    public void setVertexShow(boolean vertexShow) {
        this.vertexShow = vertexShow;
        postInvalidate();
    }

    public int getVertexColor() {
        return vertexColor;
    }

    public void setVertexColor(@ColorInt int vertexColor) {
        this.vertexColor = vertexColor;
        postInvalidate();
    }

    public int getVertexRadius() {
        return vertexRadius;
    }

    public void setVertexRadius(@IntRange(from = 1) int vertexRadius) {
        this.vertexRadius = vertexRadius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (radarEntities == null || radarEntities.size() < 3)
            return;

        int maxHPadding = Math.max(getPaddingLeft(), getPaddingRight());
        int maxVPadding = Math.max(getPaddingTop(), getPaddingBottom());
        float maxRadius = getWidth() / 2.0f - layerStrokeWidth - Math.max(maxHPadding, maxVPadding);
        int count = radarEntities.size();

        paint.setStyle(layerStyle == 0 ? Paint.Style.STROKE : Paint.Style.FILL);
        paint.setStrokeWidth(layerStrokeWidth);
        paint.setColor(layerColor);
        pointLists.clear();
        for (int i = 0; i < layerCount; i++) {
            float tempRadius = maxRadius - maxRadius * i / layerCount;
            List<RadarPoint> points = getPointsOnCircle(startAngle, count, getWidth() / 2.0f, getWidth() / 2.0f, tempRadius);
            drawPolygon(canvas, points, paint);
            pointLists.add(points);
        }

        //最外层多边形和最里层多边形相对应顶点vertex间的连线
        if (layerCount >= 2){
            for (int i = 0; i < count; i++) {
                RadarPoint point1 = pointLists.get(0).get(i);
                RadarPoint point2 = pointLists.get(layerCount - 1).get(i);
                drawPolygonLine(canvas, point1, point2, paint);
            }
        }

        paint.setStyle(Paint.Style.FILL);
        //vertex
        if (vertexShow){
            paint.setColor(vertexColor);
            for (int i = 0; i < count; i++) {
                RadarPoint point = pointLists.get(0).get(i);
                canvas.drawCircle(point.getX(), point.getY(), vertexRadius, paint);
            }
        }

        //output
        paint.setColor(outputColor);
        drawPolygon(canvas, getDataPoints(startAngle, maxRadius, radarEntities), paint);

        //labels
        for (int i = 0; i < count; i++) {
            RadarPoint point = pointLists.get(0).get(i);
            drawLabel(canvas, radarEntities.get(i), point, textPaint, vertexRadius + 8);
        }
    }

    /**
     * 画正多边形
     *
     * @param canvas
     * @param points
     * @param paint
     */
    private void drawPolygon(Canvas canvas, List<RadarPoint> points, Paint paint) {
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            RadarPoint point = points.get(i);
            if (i == 0) path.moveTo(point.getX(), point.getY());
            else path.lineTo(point.getX(), point.getY());
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 画正多边形相对应顶点的连线
     *
     * @param canvas
     * @param point1
     * @param point2
     * @param paint
     */
    private void drawPolygonLine(Canvas canvas, RadarPoint point1, RadarPoint point2, Paint paint) {
        canvas.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY(), paint);
    }

    /**
     * 画标签
     *
     * @param canvas
     * @param entity
     * @param point
     * @param textPaint
     */
    private void drawLabel(Canvas canvas, RadarEntity entity, RadarPoint point, TextPaint textPaint, int offset) {
        String label = entity.getLabel();
        if (label == null || label.trim().isEmpty())
            return;

        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, entity.getLabelTextSize(), getResources().getDisplayMetrics());
        textPaint.setColor(entity.getLabelColor());
        textPaint.setTextSize(textSize);
        Rect rect = new Rect();
        textPaint.getTextBounds(label, 0, label.length(), rect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        float xStart;
        float baseLine;
        switch (entity.getLabelAlignType()) {
            case RadarEntity.ALIGN_LEFT:
                xStart = point.getX() - w - offset;
                if (xStart < 0)
                    xStart = 0;
                baseLine = point.getY() - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                break;
            case RadarEntity.ALIGN_RIGHT:
                xStart = point.getX() + offset;
                if ((xStart + w) > getWidth())
                    xStart = getWidth() - w;
                baseLine = point.getY() - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                break;
            case RadarEntity.ALIGN_TOP:
                xStart = point.getX() - w / 2.0f;
                baseLine = (point.getY() - h / 2.0f - offset) - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                break;
            default:
                xStart = point.getX() - w / 2.0f;
                baseLine = (point.getY() + h / 2.0f + offset) - h / 2.0f + (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                break;
        }
        canvas.drawText(label, xStart, baseLine, textPaint);
    }


    /**
     * 计算多边形顶点
     *
     * @param startAngle
     * @param count
     * @param centerX
     * @param centerY
     * @param radius
     * @return
     */
    private List<RadarPoint> getPointsOnCircle(int startAngle, int count, float centerX, float centerY, float radius) {
        List<RadarPoint> points = new ArrayList<>();
        int avgAngle = 360 / count;
        for (int i = 0; i < count; i++) {
            int angle = startAngle + avgAngle * i;
            points.add(getPointAtSpecialAngle(angle, centerX, centerY, radius));
        }
        return points;
    }

    /**
     * 计算value顶点
     *
     * @param startAngle
     * @param radarEntities
     * @param maxRadius
     * @return
     */
    private List<RadarPoint> getDataPoints(int startAngle, float maxRadius, List<RadarEntity> radarEntities) {
        List<RadarPoint> points = new ArrayList<>();
        int count = radarEntities.size();
        int avgAngle = 360 / count;
        for (int i = 0; i < count; i++) {
            RadarEntity entity = radarEntities.get(i);
            int angle = startAngle + avgAngle * i;
            float tempRadius = maxRadius * entity.getValue();
            RadarPoint point = getPointAtSpecialAngle(angle, getWidth() / 2.0f, getWidth() / 2.0f, tempRadius);
            points.add(point);
        }
        return points;
    }

    private RadarPoint getPointAtSpecialAngle(int angle, float centerX, float centerY, float radius) {
        double angleRadians = angle * Math.PI / 180;
        double x = centerX + radius * Math.cos(angleRadians) + 0.5f;
        double y = centerY + radius * Math.sin(angleRadians) + 0.5f;
        return new RadarPoint((float) x, (float) y);
    }
}
