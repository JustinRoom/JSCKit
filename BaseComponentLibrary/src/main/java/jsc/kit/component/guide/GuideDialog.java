package jsc.kit.component.guide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
public final class GuideDialog extends AppCompatDialog {

    private GuideLayout guideLayout;
    private View target;
    private int statusBarHeight, minRippleSize, maxRippleSize;
    private GuideLayout.OnRippleViewUpdateLocationCallback onRippleViewUpdateLocationCallback;

    public GuideDialog(Context context) {
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
    public GuideDialog(Context context, int statusBarHeight, int minRippleSize, int maxRippleSize) {
        super(context);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.minRippleSize = Math.min(minRippleSize, maxRippleSize);
        this.maxRippleSize = Math.max(minRippleSize, maxRippleSize);
        this.statusBarHeight = statusBarHeight;
        guideLayout = new GuideLayout(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(guideLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    public GuideLayout getGuideLayout() {
        return guideLayout;
    }

    public GuideDialog removeAllCustomView() {
        guideLayout.removeAllCustomViews();
        return this;
    }

    public GuideDialog setCanceledOnTouchOutside1(boolean canceledOnTouchOutside) {
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        return this;
    }

    public GuideDialog setCancelable1(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    public GuideDialog setBackgroundColor(@ColorInt int color) {
        guideLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param statusBarHeight status bar height
     * @return {@link GuideDialog}
     */
    public GuideDialog setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param minRippleSize minimum ripple view size
     * @return {@link GuideDialog}
     */
    public GuideDialog setMinRippleSize(int minRippleSize) {
        this.minRippleSize = minRippleSize;
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param maxRippleSize maximum ripple size
     * @return {@link GuideDialog}
     */
    public GuideDialog setMaxRippleSize(int maxRippleSize) {
        this.maxRippleSize = maxRippleSize;
        return this;
    }

    /**
     * It's invalid after {@link #attachTarget(View)}.
     *
     * @param onRippleViewUpdateLocationCallback maximum ripple size
     * @return {@link GuideDialog}
     */
    public GuideDialog setOnRippleViewUpdateLocationCallback(GuideLayout.OnRippleViewUpdateLocationCallback onRippleViewUpdateLocationCallback) {
        this.onRippleViewUpdateLocationCallback = onRippleViewUpdateLocationCallback;
        return this;
    }

    public GuideDialog attachTarget(@NonNull View mTarget) {
        this.target = mTarget;
        this.target.setDrawingCacheEnabled(true);
        guideLayout.updateTargetLocation(target, statusBarHeight, minRippleSize, maxRippleSize, onRippleViewUpdateLocationCallback);
        this.target.postDelayed(new Runnable() {
            @Override
            public void run() {
                target.setDrawingCacheEnabled(false);
            }
        }, 300);
        return this;
    }

    /**
     * Before showing action, it must had attached target.
     */
    @Override
    public void show() {
        if (target == null)
            throw new IllegalStateException("You need attach target first.");
        super.show();
    }

    /**
     * It must be called after {@link #attachTarget(View)} if necessary.
     * <br>And you should call it before {@link #show()}.
     *
     * @param customView custom view
     * @param callback   initialize call back.
     * @param <V>        custom view type
     */
    public <V extends View> GuideDialog addCustomView(@NonNull V customView, @NonNull GuideLayout.OnAddCustomViewCallback<V> callback) {
        if (target == null)
            throw new IllegalStateException("You need attach target first.");
        guideLayout.addCustomView(customView, callback);
        return this;
    }

    public GuideDialog addTargetClickListener(@Nullable final OnCustomClickListener listener) {
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

    public GuideDialog addCustomClickListener(@NonNull View customView, @Nullable final OnCustomClickListener listener, final boolean needDismiss) {
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


}
