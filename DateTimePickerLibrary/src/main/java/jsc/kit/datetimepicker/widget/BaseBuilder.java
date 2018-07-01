package jsc.kit.datetimepicker.widget;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**<br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class BaseBuilder {

    private CharSequence title = "Choose";
    private CharSequence leftText = "Cancel";
    private CharSequence rightText = "OK";
    private int titleColor = 0xFF999999;
    private int leftTextColor = 0xFF222222;
    private int rightTextColor = 0xFF1DC360;
    private int segmentLineColor = 0xFFF2F2F2;
    private int titleBarBackgroundColor = 0xFFF2F2F2;
    private int contentBackgroundColor = Color.WHITE;

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getLeftText() {
        return leftText;
    }

    public void setLeftText(CharSequence leftText) {
        this.leftText = leftText;
    }

    public CharSequence getRightText() {
        return rightText;
    }

    public void setRightText(CharSequence rightText) {
        this.rightText = rightText;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
    }

    public int getLeftTextColor() {
        return leftTextColor;
    }

    public void setLeftTextColor(@ColorInt int leftTextColor) {
        this.leftTextColor = leftTextColor;
    }

    public int getRightTextColor() {
        return rightTextColor;
    }

    public void setRightTextColor(@ColorInt int rightTextColor) {
        this.rightTextColor = rightTextColor;
    }

    public int getSegmentLineColor() {
        return segmentLineColor;
    }

    public void setSegmentLineColor(@ColorInt int segmentLineColor) {
        this.segmentLineColor = segmentLineColor;
    }

    public int getTitleBarBackgroundColor() {
        return titleBarBackgroundColor;
    }

    public void setTitleBarBackgroundColor(@ColorInt int titleBarBackgroundColor) {
        this.titleBarBackgroundColor = titleBarBackgroundColor;
    }

    public int getContentBackgroundColor() {
        return contentBackgroundColor;
    }

    public void setContentBackgroundColor(@ColorInt int contentBackgroundColor) {
        this.contentBackgroundColor = contentBackgroundColor;
    }
}
