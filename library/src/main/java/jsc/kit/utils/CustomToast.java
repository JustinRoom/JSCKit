package jsc.kit.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
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

    /**
     *
     * @param resId
     * @see #show(CharSequence, int, long)
     */
    public void show(@StringRes int resId) {
        show(resId, 0, -1);
    }

    /**
     *
     * @param resId
     * @param topMargin
     * @see #show(CharSequence, int, long)
     */
    public void show(@StringRes int resId, int topMargin) {
        show(resId, topMargin, -1);
    }

    /**
     *
     * @param resId
     * @param topMargin
     * @param duration
     * @see #show(CharSequence, int, long)
     */
    public void show(@StringRes int resId, int topMargin, long duration) {
        if (applicationContext == null)
            throw new RuntimeException("please init first.");

        String txt = applicationContext.getString(resId);
        show(txt, topMargin, duration);
    }

    /**
     *
     * @param txt
     * @see #show(CharSequence, int, long)
     */
    public void show(CharSequence txt) {
        show(txt, 0, -1);
    }

    /**
     *
     * @param txt
     * @param topMargin
     * @see #show(CharSequence, int, long)
     */
    public void show(CharSequence txt, int topMargin) {
        show(txt, topMargin, -1);
    }

    /**
     *
     * @param txt
     * @param topMargin
     * @param duration the minimum value is {@code 3000}
     */
    public void show(CharSequence txt, int topMargin, long duration) {
        if (applicationContext == null)
            throw new RuntimeException("please init first.");

        destroyCustomToast(false);
        DisplayMetrics metrics = applicationContext.getResources().getDisplayMetrics();
        if (lastToastView == null) {
            int dp12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, metrics);
            int dp64 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, metrics);
            lastToastView = new TextView(applicationContext);
            lastToastView.setPadding(dp12, dp12, dp12, dp12);
            lastToastView.setBackgroundColor(0xEE00FF00);
            lastToastView.setGravity(Gravity.CENTER_HORIZONTAL);
            lastToastView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            lastToastView.setTextColor(Color.BLACK);

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
            params.y = topMargin;
            params.windowAnimations = R.style.customToastWindowAnimations;
        }
        lastToastView.setText(txt);
        windowManager.addView(lastToastView, params);
        lastToastView.postDelayed(toastRunnable, duration < 3_000 ? 3_000 : duration);
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
}
