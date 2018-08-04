package jsc.kit.component.guide;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
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

    public GuidePopupWindow setBackgroundColor(@ColorInt int color){
        guideLayout.setBackgroundColor(color);
        return this;
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

    public GuidePopupWindow attachTarget(@NonNull View target) {
        this.target = target;
        this.target.setDrawingCacheEnabled(true);
        guideLayout.calculateTargetLocation(target, statusBarHeight, minRippleSize, maxRippleSize);
        return this;
    }

    /**
     * It must be called after {@link #attachTarget(View)} if necessary.
     * <br>And you should call it before {@link #show()}.
     *
     * @param customView custom view
     * @param callback initialize call back.
     * @param <V> custom view type
     */
    public <V extends View> GuidePopupWindow addCustomView(@NonNull V customView, @NonNull GuideLayout.OnCustomViewInitializeCallback<V> callback) {
        if (target == null)
            throw new IllegalStateException("");
        guideLayout.addCustomView(customView, callback);
        return this;
    }

    /**
     * Before showing action, it must had attached target.
     */
    public void show(){
        if (target == null)
            throw new IllegalStateException("");
        mPopupWindow.showAsDropDown(target, -guideLayout.getTargetRect().left, -guideLayout.getTargetRect().bottom);
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
