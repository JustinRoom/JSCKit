package jsc.kit.monthview;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class DayItem {
    private int key;

    private int background;

    private String label;
    private int labelTextColor;
    private float labelTextSize;

    private String subLabel;
    private int subLabelTextColor;
    private float subLabelTextSize;

    private long date;

    public DayItem() {
        setBackground(-1);
        setLabelTextColor(0xFF333333);
        setLabelTextSize(14);
        setSubLabelTextColor(0xFF333333);
        setSubLabelTextSize(8);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
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

    public float getLabelTextSize() {
        return labelTextSize;
    }

    /**
     * 单位是sp
     *
     * @param labelTextSize
     */
    public void setLabelTextSize(float labelTextSize) {
        this.labelTextSize = labelTextSize;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }

    public int getSubLabelTextColor() {
        return subLabelTextColor;
    }

    public void setSubLabelTextColor(int subLabelTextColor) {
        this.subLabelTextColor = subLabelTextColor;
    }

    public float getSubLabelTextSize() {
        return subLabelTextSize;
    }

    /**
     * 单位是sp
     *
     * @param subLabelTextSize
     */
    public void setSubLabelTextSize(float subLabelTextSize) {
        this.subLabelTextSize = subLabelTextSize;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
