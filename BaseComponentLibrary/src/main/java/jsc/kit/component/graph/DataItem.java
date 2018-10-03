package jsc.kit.component.graph;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class DataItem {
    private float ratio;//比率, 柱形高度百分比

    public DataItem() {
    }

    public DataItem(float ratio) {
        this.ratio = ratio;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
