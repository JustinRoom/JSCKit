package jsc.kit.component.guide;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import jsc.kit.component.R;
import jsc.kit.component.utils.CompatResourceUtils;
import jsc.kit.component.utils.WindowUtils;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public final class GuidePopupWindow {

    private PopupWindow mPopupWindow;
    private GuideLayout guideLayout;
    private View target;
    private int statusBarHeight, minRippleSize, maxRippleSize;
    private OnTargetCheckedListener onTargetCheckedListener;

    public GuidePopupWindow(Context context) {
        this(
                context,
                WindowUtils.getStatusBarHeight(context),
                CompatResourceUtils.getDimensionPixelSize(context, R.dimen.space_32),
                CompatResourceUtils.getDimensionPixelSize(context, R.dimen.space_64)
        );
    }

    /**
     * @param context         context
     * @param statusBarHeight status bar height
     * @param minRippleSize   minimum ripple size
     * @param maxRippleSize   max ripple size
     */
    public GuidePopupWindow(Context context, int statusBarHeight, int minRippleSize, int maxRippleSize) {
        this.minRippleSize = Math.min(minRippleSize, maxRippleSize);
        this.maxRippleSize = Math.max(minRippleSize, maxRippleSize);
        this.statusBarHeight = statusBarHeight;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        guideLayout = new GuideLayout(context);
        guideLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        guideLayout.setTargetClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onTargetCheckedListener != null)
                    onTargetCheckedListener.onTargetChecked(target);
            }
        });

        mPopupWindow = new PopupWindow();
        mPopupWindow.setContentView(guideLayout);
        mPopupWindow.setWidth(metrics.widthPixels);
        mPopupWindow.setHeight(metrics.heightPixels);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                target.setDrawingCacheEnabled(false);
            }
        });
    }

    @NonNull
    public GuideLayout getGuideLayout() {
        return guideLayout;
    }

    public GuidePopupWindow setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
        return this;
    }

    public GuidePopupWindow setMinRippleSize(int minRippleSize) {
        this.minRippleSize = minRippleSize;
        return this;
    }

    public GuidePopupWindow setMaxRippleSize(int maxRippleSize) {
        this.maxRippleSize = maxRippleSize;
        return this;
    }

    public GuidePopupWindow setOnTargetCheckedListener(OnTargetCheckedListener onTargetCheckedListener) {
        this.onTargetCheckedListener = onTargetCheckedListener;
        return this;
    }

    public <V extends View> void show(@NonNull View target, @NonNull V customView, @NonNull GuideLayout.OnCustomViewInitializeCallback<V> callback) {
        this.target = target;
        target.setDrawingCacheEnabled(true);
        Rect rect = guideLayout.updateMask(target, statusBarHeight, minRippleSize, maxRippleSize);
        guideLayout.setCustomView(customView, callback);
        mPopupWindow.showAsDropDown(target, -rect.left, -rect.bottom);
    }

    public void dismiss() {
        if (mPopupWindow == null)
            return;
        mPopupWindow.dismiss();
    }

    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    public interface OnTargetCheckedListener {
        void onTargetChecked(View target);
    }
}
