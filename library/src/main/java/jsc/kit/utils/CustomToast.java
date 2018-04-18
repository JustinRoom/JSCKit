package jsc.kit.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import jsc.kit.R;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public final class CustomToast {

    private Application applicationContext;
    private WindowManager windowManager;
    private TextView lastToastView = null;
    private Runnable toastRunnable = null;
    private WindowManager.LayoutParams params = null;

    public CustomToast(Application applicationContext) {
        this.applicationContext = applicationContext;
        windowManager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void show(@StringRes int resId) {
        String txt = applicationContext.getString(resId);
        show(txt);
    }

    public void show(String text) {
        show(new Builder().text(text));
    }

    /**
     *
     * @param builder
     */
    public void show(Builder builder) {
        if (builder == null)
            builder = new Builder();

        destroyCustomToast(false);
        DisplayMetrics metrics = applicationContext.getResources().getDisplayMetrics();
        if (lastToastView == null) {
            int dp12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, metrics);
            int dp64 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, metrics);
            lastToastView = new TextView(applicationContext);
            lastToastView.setPadding(dp12, dp12, dp12, dp12);
            toastRunnable = new Runnable() {
                @Override
                public void run() {
                    if (lastToastView.getParent() != null)
                        windowManager.removeView(lastToastView);
                }
            };
            params = getParams();
            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.width = metrics.widthPixels - dp64 * 2;
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
        params.type = WindowManager.LayoutParams.TYPE_TOAST;// 系统提示window
        params.format = PixelFormat.TRANSLUCENT;// 支持透明
//        mParams.format = PixelFormat.RGBA_8888;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
//        mParams.width = 490;//窗口的宽和高
//        mParams.height = 160;
//        mParams.x = 0;//窗口位置的偏移量
//        mParams.y = 0;
//        mParams.alpha = 0.1f;//窗口的透明度
        return params;
    }

    public void destroyCustomToast(boolean recycleToastView) {
        if (lastToastView != null && lastToastView.getParent() != null) {
            lastToastView.removeCallbacks(toastRunnable);
            windowManager.removeView(lastToastView);
        }
        if (recycleToastView) {
            lastToastView = null;
            toastRunnable = null;
            params = null;
        }
    }

    public static class Builder {
        CharSequence text = "";
        int backgroundColor = 0xEE00FF00;
        int textColor = Color.BLACK;
        float textSize = 16.0f;
        int textGravity = Gravity.CENTER_HORIZONTAL;
        int topMargin  = 0;
        long duration = 3_000;

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
            this.duration = duration < 3_000 ? 3_000 : duration;
            return this;
        }

        /**
         *
         *
         * @param textGravity
         * @return
         * @see Gravity
         */
        public Builder textGravity(int textGravity) {
            this.textGravity = textGravity;
            return this;
        }
    }
}
