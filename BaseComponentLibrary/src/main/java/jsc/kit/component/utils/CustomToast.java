package jsc.kit.component.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public final class CustomToast {

    private static CustomToast instance = null;
    private Context applicationContext;
    private WindowManager windowManager;
    private TextView lastToastView = null;
    private Runnable toastRunnable = null;
    private WindowManager.LayoutParams params = null;

    private CustomToast() {

    }

    public static CustomToast getInstance() {
        if (instance == null)
            synchronized (CustomToast.class) {
                if (instance == null) {
                    instance = new CustomToast();
                }
            }
        return instance;
    }

    public void init(Context applicationContext) {
        this.applicationContext = applicationContext;
        windowManager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void show(@NonNull Context context, @StringRes int resId) {
        show(context.getString(resId));
    }

    public void show(CharSequence text) {
        show(new Builder().text(text));
    }

    /**
     * @param builder builder
     */
    public void show(Builder builder) {
        if (applicationContext == null)
            throw new RuntimeException("Please init this component inside application's onCreate() method first.");

        if (builder == null)
            builder = new Builder();

        destroy(false);
        if (lastToastView == null) {
            DisplayMetrics metrics = applicationContext.getResources().getDisplayMetrics();
            int dp4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);
            lastToastView = new TextView(applicationContext);
            lastToastView.setPadding(dp4 * 3, dp4 * 2, dp4 * 3, dp4 * 2);
            toastRunnable = new Runnable() {
                @Override
                public void run() {
                    if (lastToastView.getParent() != null)
                        windowManager.removeView(lastToastView);
                }
            };
            params = getParams();
            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.width = metrics.widthPixels - dp4 * 32;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.windowAnimations = R.style.customToastWindowAnimations;
        }
        lastToastView.setBackgroundColor(builder.backgroundColor);
        lastToastView.setGravity(builder.textGravity);
        lastToastView.setTextSize(TypedValue.COMPLEX_UNIT_SP, builder.textSize);
        lastToastView.setTextColor(builder.textColor);
        lastToastView.setText(builder.text);
        params.y = builder.topMargin;
        windowManager.addView(lastToastView, params);
        lastToastView.postDelayed(toastRunnable, builder.duration);
    }

    public WindowManager.LayoutParams getParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
            params.type = WindowManager.LayoutParams.TYPE_TOAST;// 系统提示window
        else
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 系统提示window
        params.format = PixelFormat.TRANSLUCENT;// 支持透明
//        mParams.format = PixelFormat.RGBA_8888;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
//        mParams.width = 490;//窗口的宽和高
//        mParams.height = 160;
//        mParams.x = 0;//窗口位置的偏移量
//        mParams.y = 0;
//        mParams.alpha = 0.1f;//窗口的透明度
        return params;
    }

    public void destroy(boolean recycleToastView) {
        if (lastToastView != null && lastToastView.getParent() != null) {
            lastToastView.removeCallbacks(toastRunnable);
            windowManager.removeViewImmediate(lastToastView);
        }
        if (recycleToastView) {
            lastToastView = null;
            toastRunnable = null;
            params = null;
        }
    }

    public static class Builder {
        CharSequence text;
        int backgroundColor;
        int textColor;
        float textSize;
        int textGravity;
        int topMargin;
        long duration;

        public Builder() {
            text = "";
            backgroundColor = 0xEE00FF00;
            textColor = Color.BLACK;
            textSize = 14f;
            textGravity = Gravity.CENTER_HORIZONTAL;
            topMargin = 0;
            duration = 3_000;
        }

        public Builder text(@NonNull Context context, @StringRes int resId) {
            this.text = context.getString(resId);
            return this;
        }

        public Builder text(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder backgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder textColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder textSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder topMargin(int topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        /**
         * @param textGravity text gravity
         * @return builder
         * @see Gravity
         */
        public Builder textGravity(int textGravity) {
            this.textGravity = textGravity;
            return this;
        }
    }
}
