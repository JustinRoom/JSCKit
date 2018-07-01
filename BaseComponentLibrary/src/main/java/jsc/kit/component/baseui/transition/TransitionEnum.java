package jsc.kit.component.baseui.transition;

import android.support.annotation.Nullable;

public enum TransitionEnum {
    SLIDE((byte) 0, "slide"),
    EXPLODE((byte)1, "explode"),
    FADE((byte)2, "fade");

    private byte value;
    private String label;

    TransitionEnum(byte value, String label) {
        this.value = value;
        this.label = label;
    }

    public byte getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Nullable
    public static TransitionEnum createTransitionByValue(byte value){
        if (value == 0)
            return SLIDE;
        if (value == 1)
            return EXPLODE;
        if (value == 2)
            return FADE;
        return null;
    }

    @Nullable
    public static TransitionEnum createTransitionByLabel(String label){
        if (SLIDE.getLabel().equalsIgnoreCase(label))
            return SLIDE;
        if (EXPLODE.getLabel().equalsIgnoreCase(label))
            return EXPLODE;
        if (FADE.getLabel().equalsIgnoreCase(label))
            return FADE;
        return null;
    }
}
