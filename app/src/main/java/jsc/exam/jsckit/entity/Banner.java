package jsc.exam.jsckit.entity;

/**
 * Created on 2018/3/10.
 *
 * @author jsc
 */

public class Banner {
    private String label;
    private String url;

    public Banner(String url) {
        this("", url);
    }

    public Banner(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
