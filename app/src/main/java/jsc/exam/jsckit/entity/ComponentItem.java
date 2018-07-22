package jsc.exam.jsckit.entity;

public class ComponentItem extends ClassItem{

    private String shortName;

    public ComponentItem(String label, Class<?> cls) {
        this(label, "", cls);
    }

    public ComponentItem(String label, String shortName, Class<?> cls) {
        super(label, cls);
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
