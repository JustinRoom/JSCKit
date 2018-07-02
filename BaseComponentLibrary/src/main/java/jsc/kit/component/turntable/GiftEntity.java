package jsc.kit.component.turntable;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class GiftEntity {
    /**
     * 备用字段。
     */
    private int key;
    private Bitmap bitmap;
    /**
     * 备用字段。例如：本地图片路径、网络图片路径...假如是图片路径，我们可以通过此字段获取bitmap，然后再调用{@link #setBitmap(Bitmap)}设置图片。
     */
    private String url;
    private String label;
    private int labelTextColor;
    private int backgroundColor;
    /**
     * 图饼的起始角度。会自动赋值，不需要设置。
     */
    private float startAngle;
    /**
     * 图饼的扫描角度。会自动赋值，不需要设置。
     */
    private float sweepAngle;

    public GiftEntity() {
        backgroundColor = 0xFFFF00FF;
        labelTextColor = 0xFF333333;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLabelTextColor() {
        return labelTextColor;
    }

    public void setLabelTextColor(int labelTextColor) {
        this.labelTextColor = labelTextColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public void recycleBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
