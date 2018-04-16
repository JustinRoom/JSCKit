package jsc.exam.jsckit.entity;

public class ClassItem {
    private String label;
    private Class<?> cls;

    public ClassItem(String label, Class<?> cls) {
        this.label = label;
        this.cls = cls;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
