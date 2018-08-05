package jsc.kit.component.guide;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

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
public final class GuidePopupView {

    private GuideLayout guideLayout;
    private View target;
    private int yOffset, minRippleSize, maxRippleSize;
    private GuideLayout.OnRippleViewUpdateLocationCallback onRippleViewUpdateLocationCallback;

    public GuidePopupView(Context context) {
        this(
                context,
                WindowUtils.getStatusBarHeight(context),
                CompatResourceUtils.getDimensionPixelSize(context, R.dimen.space_32),
                CompatResourceUtils.getDimensionPixelSize(context, R.dimen.space_64)
        );
    }

    /**
     * @param context       context
     * @param yOffset       offset from top
     * @param minRippleSize minimum ripple size
     * @param maxRippleSize max ripple size
     */
    public GuidePopupView(Context context, int yOffset, int minRippleSize, int maxRippleSize) {
        this.minRippleSize = Math.min(minRippleSize, maxRippleSize);
        this.maxRippleSize = Math.max(minRippleSize, maxRippleSize);
        this.yOffset = yOffset;
        guideLayout = new GuideLayout(context);
        guideLayout.setOnClickListener(null);
        guideLayout.setOnLongClickListener(null);
    }

    @NonNull
    public GuideLayout getGuideLayout() {
        return guideLayout;
    }

    public GuidePopupView removeAllCustomView() {
        guideLayout.removeAllCustomViews();
        return this;
    }

    public GuidePopupView setBackgroundColor(@ColorInt int color) {
        guideLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param yOffset status bar height
     * @return {@link GuidePopupView}
     */
    public GuidePopupView setyOffset(int yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param minRippleSize minimum ripple view size
     * @return {@link GuidePopupView}
     */
    public GuidePopupView setMinRippleSize(int minRippleSize) {
        this.minRippleSize = minRippleSize;
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param maxRippleSize maximum ripple size
     * @return {@link GuidePopupView}
     */
    public GuidePopupView setMaxRippleSize(int maxRippleSize) {
        this.maxRippleSize = maxRippleSize;
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param onRippleViewUpdateLocationCallback maximum ripple size
     * @return {@link GuidePopupView}
     */
    public GuidePopupView setOnRippleViewUpdateLocationCallback(GuideLayout.OnRippleViewUpdateLocationCallback onRippleViewUpdateLocationCallback) {
        this.onRippleViewUpdateLocationCallback = onRippleViewUpdateLocationCallback;
        return this;
    }

    public GuidePopupView attachTarget(@NonNull View mTarget) {
        this.target = mTarget;
        this.target.setDrawingCacheEnabled(true);
        guideLayout.updateTargetLocation(mTarget, yOffset, minRippleSize, maxRippleSize, onRippleViewUpdateLocationCallback);
        this.target.postDelayed(new Runnable() {
            @Override
            public void run() {
                target.setDrawingCacheEnabled(false);
            }
        }, 300);
        return this;
    }

    /**
     * It must be called after {@link #attachTarget(View)} if necessary.
     * <br>And you should call it before {@link #show(Activity)}.
     *
     * @param customView custom view
     * @param callback   initialize call back.
     * @param <V>        custom view type
     */
    public <V extends View> GuidePopupView addCustomView(@NonNull V customView, @NonNull GuideLayout.OnAddCustomViewCallback<V> callback) {
        if (target == null)
            throw new IllegalStateException("You need attach target first.");
        guideLayout.addCustomView(customView, callback);
        return this;
    }

    public GuidePopupView addTargetClickListener(@Nullable final OnCustomClickListener listener) {
        guideLayout.setTargetClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null)
                    listener.onCustomClick(v);
            }
        });
        return this;
    }

    public GuidePopupView addCustomClickListener(@NonNull View customView, @Nullable final OnCustomClickListener listener, final boolean needDismiss) {
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needDismiss)
                    dismiss();

                if (listener != null)
                    listener.onCustomClick(v);
            }
        });
        return this;
    }

    /**
     * Before showing action, it must had attached target.
     */
    public void show(@NonNull Activity activity) {
        if (target == null)
            throw new IllegalStateException("You need attach target first.");

        View view = activity.findViewById(android.R.id.content);
        if (view instanceof ViewGroup)
            ((ViewGroup) view).addView(guideLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void dismiss() {
        ViewGroup parent = (ViewGroup) guideLayout.getParent();
        if (parent != null)
            parent.removeView(guideLayout);
    }

    public boolean isShowing(@NonNull Activity activity) {
        return guideLayout.getParent() != null;
    }
}
