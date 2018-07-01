package jsc.kit.component.graph;

import android.graphics.RectF;
import android.support.annotation.FloatRange;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class ColumnarItem {
    private int color;//柱形颜色
    private float ratio;//比率
    private float left;//无需设置
    private float top;//无需设置
    private float right;//无需设置
    private float bottom;//无需设置
    private String label;//点击柱形所显示详细信息之标签
    private String value;//点击柱形所显示详细信息之值

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(@FloatRange(from = 0, to = 1.0f) float ratio) {
        this.ratio = ratio;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void initRectF(RectF rect){
        if (rect == null)
            return;
        rect.set(left, top, right, bottom);
    }
}
