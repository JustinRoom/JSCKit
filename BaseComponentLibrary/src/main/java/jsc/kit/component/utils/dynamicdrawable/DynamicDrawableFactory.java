package jsc.kit.component.utils.dynamicdrawable;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public class DynamicDrawableFactory {

    //>>>>>>>rectangle
    public static GradientDrawable cornerRectangleDrawable(@ColorInt int backgroundColor, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(cornerRadius);
        drawable.setColor(backgroundColor);
        return drawable;
    }

    public static GradientDrawable cornerRectangleDrawable(@ColorInt int backgroundColor, float[] cornerRadii) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(cornerRadii);
        drawable.setColor(backgroundColor);
        return drawable;
    }

    public static GradientDrawable cornerRectangleDrawable(@ColorInt int backgroundColor, float[] cornerRadii, @IntRange(from = 1) int borderWidth, @ColorInt int borderColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(cornerRadii);
        drawable.setStroke(borderWidth, borderColor);
        drawable.setColor(backgroundColor);
        return drawable;
    }

    //>>>>>>oval
    public static GradientDrawable ovalDrawable(@ColorInt int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color);
        return drawable;
    }

    /**
     *
     * @param disable the drawable on component's disable state. See {@link View#isEnabled()}.
     * @param pressed   the drawable on component's pressed state. See {@link View#isPressed()}
     * @param selected  the drawable on component's selected state. See {@link View#isSelected()}.
     * @param focused   the drawable on component's focused state. See {@link View#isFocused()}.
     * @param normal    the drawable on component's normal state.
     * @return drawable of combining all drawable on component's states.
     */
    public static StateListDrawable stateListDrawable(Drawable disable, Drawable pressed, Drawable selected, Drawable focused, @NonNull Drawable normal) {
        StateListDrawable listDrawable = new StateListDrawable();
        if (disable != null)
            listDrawable.addState(new int[]{-android.R.attr.state_enabled}, disable);

        if (pressed != null)
            listDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);

        if (selected != null)
            listDrawable.addState(new int[]{android.R.attr.state_selected}, selected);

        if (focused != null)
            listDrawable.addState(new int[]{android.R.attr.state_focused}, focused);

        listDrawable.addState(new int[]{}, normal);

        return listDrawable;
    }

    public static ColorStateList colorStateList(@ColorInt int normal) {
        return ColorStateList.valueOf(normal);
    }

    public static ColorStateList colorStateList(@ColorInt int pressed, @ColorInt int normal) {
        return colorStateList(normal, pressed, normal, normal, normal);
    }

    public static ColorStateList colorStateList(@ColorInt int disable, @ColorInt int pressed, @ColorInt int selected, @ColorInt int focused, @ColorInt int normal) {
        int[] colors = {disable, pressed, selected, focused, normal};
        int[][] states = new int[5][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{};
        return new ColorStateList(states,colors);
    }
}
