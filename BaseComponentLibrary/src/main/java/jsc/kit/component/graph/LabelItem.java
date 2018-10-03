package jsc.kit.component.graph;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class LabelItem {
    private int color;//标签颜色
    private float size;//标签大小
    private String label;//标签

    public LabelItem() {
        color = 0xFF04DB5B;
    }

    public LabelItem(@ColorInt int color, @FloatRange(from = 0) float size, String label) {
        this.color = color;
        this.size = size;
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(@FloatRange(from = 0)float size) {
        this.size = size;
    }

    /**
     * @param context context
     * @param unit    unit
     * @param value   value
     * @see TypedValue#applyDimension(int, float, DisplayMetrics)
     */
    public void setSize(@NonNull Context context, int unit, @FloatRange(from = 0)float value) {
        this.size = TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
