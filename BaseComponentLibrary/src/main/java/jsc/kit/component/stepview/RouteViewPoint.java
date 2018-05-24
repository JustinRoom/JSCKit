package jsc.kit.component.stepview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class RouteViewPoint {
    public static final int DEFAULT_TEXT_COLOR = 0xFF333333;
    private int key;//标识键

    private int basicX;//基点(X轴方向坐标)
    private int basicY;//基点(Y轴方向坐标)

    private Drawable icon;

    private float radius = 10;//圆半径
    private int borderColor = DEFAULT_TEXT_COLOR;//圆框颜色
    private int borderWidth = 2;//圆框粗细
    private int backgroundColor = Color.WHITE;//圆背景颜色
    private int distance;//与前一个圆的距离

    //序号
    private String index;
    private int indexColor = DEFAULT_TEXT_COLOR;
    private float indexSize;
    private boolean indexBold;

    //公交
    private String transit;
    private int transitColor = DEFAULT_TEXT_COLOR;
    private float transitSize;
    private boolean transitBold;//是否加粗显示

    //上、转、下
    private String cursor;
    private int cursorColor = DEFAULT_TEXT_COLOR;
    private float cursorSize;
    private boolean cursorBold;//是否加粗显示

    //起点、站、终点
    private String label;
    private int labelColor = DEFAULT_TEXT_COLOR;
    private float labelSize;
    private boolean labelBold;//是否加粗显示

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getBasicX() {
        return basicX;
    }

    public void setBasicX(int basicX) {
        this.basicX = basicX;
    }

    public int getBasicY() {
        return basicY;
    }

    public void setBasicY(int basicY) {
        this.basicY = basicY;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getIndexColor() {
        return indexColor;
    }

    public void setIndexColor(int indexColor) {
        this.indexColor = indexColor;
    }

    public float getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(float indexSize) {
        this.indexSize = indexSize;
    }

    public boolean isIndexBold() {
        return indexBold;
    }

    public void setIndexBold(boolean indexBold) {
        this.indexBold = indexBold;
    }

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public int getTransitColor() {
        return transitColor;
    }

    public void setTransitColor(int transitColor) {
        this.transitColor = transitColor;
    }

    public float getTransitSize() {
        return transitSize;
    }

    public void setTransitSize(float transitSize) {
        this.transitSize = transitSize;
    }

    public boolean isTransitBold() {
        return transitBold;
    }

    public void setTransitBold(boolean transitBold) {
        this.transitBold = transitBold;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public int getCursorColor() {
        return cursorColor;
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }

    public float getCursorSize() {
        return cursorSize;
    }

    public void setCursorSize(float cursorSize) {
        this.cursorSize = cursorSize;
    }

    public boolean isCursorBold() {
        return cursorBold;
    }

    public void setCursorBold(boolean cursorBold) {
        this.cursorBold = cursorBold;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public float getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(float labelSize) {
        this.labelSize = labelSize;
    }

    public boolean isLabelBold() {
        return labelBold;
    }

    public void setLabelBold(boolean labelBold) {
        this.labelBold = labelBold;
    }
}