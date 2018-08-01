package jsc.kit.component.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public final class CustomToast {

    public static void showCustomToast(Context context, CharSequence txt) {
        showCustomToast(new Builder(context), txt);
    }

    public static void showCustomToast(@NonNull Builder builder, CharSequence txt) {
        showCustomToast(builder.setText(txt));
    }

    public static void showCustomToast(@NonNull Builder builder) {
        createToast(builder, builder.customView == null ? createCustomView(builder) : builder.customView).show();
    }

    private static TextView createCustomView(@NonNull Builder builder) {
        TextView textView = new TextView(builder.context);
        textView.setPadding(builder.paddingLeft, builder.paddingTop, builder.paddingRight, builder.paddingBottom);
        textView.setGravity(builder.textGravity);
        textView.setBackgroundColor(builder.backgroundColor);
        textView.setTextColor(builder.textColor);
        textView.setTextSize(builder.unit, builder.textSize);
        textView.setText(builder.text);
        return textView;
    }

    private static Toast createToast(@NonNull Builder builder, @NonNull View customView) {
        Toast toast = new Toast(builder.context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(builder.gravity, builder.xOffset, builder.yOffset);
        toast.setView(customView);

        try {
            Method method = Toast.class.getMethod("getWindowParams");
            method.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) method.invoke(toast);
            if (params != null) {
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.windowAnimations = R.style.customToastWindowAnimations;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toast;
    }

    public static class Builder {
        Context context;
        //setting for text view
        CharSequence text;
        int unit;
        float textSize;
        int textColor;
        int textGravity;
        int backgroundColor;
        int paddingLeft;
        int paddingTop;
        int paddingRight;
        int paddingBottom;

        // setting for toast
        View customView;
        int gravity;
        int xOffset;
        int yOffset;

        public Builder(Context context) {
            this.context = context;
            initDefaultSetting();
        }

        /**
         * Reset all to the default setting.
         *
         * @return builder
         */
        public Builder reset() {
            initDefaultSetting();
            return this;
        }

        private void initDefaultSetting() {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            setTextColor(0xFF333333);
            setTextGravity(Gravity.CENTER_HORIZONTAL);
            setBackgroundColor(0x33333333);

            setGravity(Gravity.TOP);
            setPadding(0, 10, 0, 10);
            setOffset(0, WindowUtils.getActionBarSize(context));
        }

        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder setText(@StringRes int resId) {
            this.text = context.getResources().getString(resId);
            return this;
        }

        /**
         * @param unit     unit. The default unit is {@link TypedValue#COMPLEX_UNIT_SP}.
         * @param textSize text size, default size is 13.
         * @return builder
         * @see TextView#setTextSize(int, float)
         */
        public Builder setTextSize(int unit, @FloatRange(from = 0) float textSize) {
            this.unit = unit;
            this.textSize = textSize;
            return this;
        }

        /**
         * Set the text color.
         *
         * @param textColor text color, default value is {@code 0xFF333333}.
         * @return builder
         */
        public Builder setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         *
         * @param textGravity text gravity, default value is {@link Gravity#CENTER_HORIZONTAL}.
         * @return builder
         */
        public Builder setTextGravity(int textGravity) {
            this.textGravity = textGravity;
            return this;
        }

        /**
         *
         * @param backgroundColor background color, default value is {@code 0x33333333}.
         * @return builder
         */
        public Builder setBackgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
            this.paddingLeft = paddingLeft;
            this.paddingTop = paddingTop;
            this.paddingRight = paddingRight;
            this.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setCustomView(@Nullable View customView) {
            this.customView = customView;
            return this;
        }

        /**
         * @param gravity {@link Gravity}
         * @return builder
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setOffset(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public void show() {
            showCustomToast(this);
        }
    }
}
