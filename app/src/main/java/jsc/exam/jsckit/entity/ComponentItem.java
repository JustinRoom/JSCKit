package jsc.exam.jsckit.entity;

public class ComponentItem extends ClassItem{

    private String shortName;

    public ComponentItem() {
    }

    public ComponentItem(String label, Class<?> cls) {
        super(label, cls);
    }

    public ComponentItem(String label, Class<?> cls, boolean updated) {
        super(label, cls, updated);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
