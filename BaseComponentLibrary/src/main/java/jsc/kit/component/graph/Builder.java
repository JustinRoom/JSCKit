package jsc.kit.component.graph;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author jiangshicheng
 */
public class Builder {
    private String[] yAxisLabels;
    private String[] xAxisLabels;
    private int axisColor = 0xFFD8D8D8;//坐标系以及刻度线颜色
    private int axisLabelTextColor = 0xFF666666;//刻度字体颜色
    private float axisLabelTextSize;//刻度字体大小
    private int space = 60;//柱形间隔
    private int column = 7;//柱形数目
    private int leftOffset = 50;
    private int topOffset = 20;
    private int rightOffset = 20;
    private int bottomOffset = 40;

    public String[] getYAxisLabels() {
        return yAxisLabels;
    }

    public Builder setYAxisLabels(String[] yAxisLabels) {
        this.yAxisLabels = yAxisLabels;
        return this;
    }

    public String[] getXAxisLabels() {
        return xAxisLabels;
    }

    public Builder setXAxisLabels(String[] xAxisLabels) {
        this.xAxisLabels = xAxisLabels;
        return this;
    }

    public int getAxisColor() {
        return axisColor;
    }

    public Builder setAxisColor(@ColorInt int axisColor) {
        this.axisColor = axisColor;
        return this;
    }

    public int getAxisLabelTextColor() {
        return axisLabelTextColor;
    }

    public Builder setAxisLabelTextColor(@ColorInt int axisLabelTextColor) {
        this.axisLabelTextColor = axisLabelTextColor;
        return this;
    }

    public float getAxisLabelTextSize() {
        return axisLabelTextSize;
    }

    public Builder setAxisLabelTextSize(float axisLabelTextSize) {
        this.axisLabelTextSize = axisLabelTextSize;
        return this;
    }

    public int getSpace() {
        return space;
    }

    public Builder setSpace(@IntRange(from = 0) int space) {
        this.space = space;
        return this;
    }

    public int getColumn() {
        return column;
    }

    public Builder setColumn(@IntRange(from = 1) int column) {
        this.column = column;
        return this;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public Builder setLeftOffset(@IntRange(from = 0) int leftOffset) {
        this.leftOffset = leftOffset;
        return this;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public Builder setTopOffset(@IntRange(from = 0) int topOffset) {
        this.topOffset = topOffset;
        return this;
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public Builder setRightOffset(@IntRange(from = 0) int rightOffset) {
        this.rightOffset = rightOffset;
        return this;
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public Builder setBottomOffset(@IntRange(from = 0) int bottomOffset) {
        this.bottomOffset = bottomOffset;
        return this;
    }


    /**
     * 设置XY轴刻度
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
     * @param context context
     * @param unit unit
     * @param value value
     * @return builder
     * @see TypedValue#applyDimension(int, float, DisplayMetrics)
     */
    public Builder setAxisLabelTextSize(@NonNull Context context, int unit, float value) {
        this.axisLabelTextSize = TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
        return this;
    }

    /**
     * 设置柱形间隔以及柱形数目。
     * <br>柱形间隔和柱形数目决定了柱形的宽度。
     * @param space space between two columns
     * @param column column number
     * @return builder
     */
    public Builder setSpaceColumn(@IntRange(from = 0) int space, @IntRange(from = 1) int column){
        this.space = space;
        this.column = column;
        return this;
    }

    /**
     * 设置四个方向的偏移量。
     * <br>偏移量用来留出空间画刻度。
     * @param leftOffset left offset
     * @param topOffset top offset
     * @param rightOffset right offset
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
