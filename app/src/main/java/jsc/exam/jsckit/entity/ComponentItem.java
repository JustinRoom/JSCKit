package jsc.exam.jsckit.entity;

public class ComponentItem extends ClassItem{

    private String fragmentClassName;

    public ComponentItem() {
    }

    public ComponentItem(String label, Class<?> cls) {
        super(label, cls);
    }

    public ComponentItem(String label, Class<?> cls, boolean updated) {
        super(label, cls, updated);
    }

    public ComponentItem(String label, Class<?> cls, boolean updated, String fragmentClassName) {
        super(label, cls, updated);
        this.fragmentClassName = fragmentClassName;
    }

    public String getFragmentClassName() {
        return fragmentClassName;
    }

    public void setFragmentClassName(String fragmentClassName) {
        this.fragmentClassName = fragmentClassName;
    }
}
