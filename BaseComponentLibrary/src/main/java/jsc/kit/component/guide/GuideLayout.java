package jsc.kit.component.guide;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import jsc.kit.component.IViewAttrDelegate;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class GuideLayout extends FrameLayout implements IViewAttrDelegate {

    final String TAG = "GuideLayout";
    ImageView ivTarget;
    ArrayList<View> customViewArray = null;
    GuideRippleView guideRippleViewView;
    Rect targetRect = new Rect();

    public GuideLayout(@NonNull Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public GuideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public GuideLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GuideLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        setWillNotDraw(false);
        setEnabled(false);
        ivTarget = new ImageView(context);
        addView(ivTarget, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public Rect getTargetRect() {
        return targetRect;
    }

    public ArrayList<View> getCustomViews() {
        return customViewArray;
    }

    @Nullable
    public GuideRippleView getGuideRippleViewView() {
        return guideRippleViewView;
    }

    public void removeAllCustomViews(){
        customViewArray.clear();
        customViewArray = null;
    }

    /**
     * Custom view should be added in {@link OnCustomViewInitializeCallback#onCustomViewInitialize(GuideLayout, View, Rect)} method by yourself.
     *
     * @param customView custom view
     * @param callback   callback
     * @param <V>        custom view type
     */
    public <V extends View> void addCustomView(@Nullable V customView, @NonNull OnCustomViewInitializeCallback<V> callback) {
        if (customViewArray == null)
            customViewArray = new ArrayList<>();
        customViewArray.add(customView);
        callback.onCustomViewInitialize(this, customView, targetRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setTargetClickListener(OnClickListener targetClickListener) {
        ivTarget.setOnClickListener(targetClickListener);
    }

    public Rect calculateTargetLocation(@NonNull View target, int statusBarHeight, int minRippleSize, int maxRippleSize) {
        Bitmap bitmap = target.getDrawingCache();
        int[] location = new int[2];
        target.getLocationOnScreen(location);
        targetRect.set(location[0], location[1], location[0] + target.getWidth(), location[1] + target.getHeight());
        targetRect.offset(0, -statusBarHeight);
        Log.i(TAG, "calculateTargetLocation: bitmap is " + (bitmap == null ? "null" : "not null"));
        ivTarget.setImageBitmap(bitmap);
        MarginLayoutParams params = (MarginLayoutParams) ivTarget.getLayoutParams();
        params.leftMargin = targetRect.left;
        params.topMargin = targetRect.top;
        ivTarget.setLayoutParams(params);
        updateRippleView(targetRect, minRippleSize, maxRippleSize);
        return targetRect;
    }

    private void updateRippleView(@NonNull Rect targetRect, int minRippleSize, int maxRippleSize) {
        int size = Math.max(targetRect.width(), targetRect.height());
        int min = Math.min(minRippleSize, maxRippleSize);
        int max = minRippleSize + maxRippleSize - min;
        if (min > 0) {
            size = Math.max(size, minRippleSize);
        }
        if (max > 0) {
            size = Math.min(size, maxRippleSize);
        }
        LayoutParams params = new LayoutParams(size, 0);
        params.leftMargin = (targetRect.left + targetRect.right - size) / 2;
        params.topMargin = (targetRect.top + targetRect.bottom - size) / 2;
        if (guideRippleViewView == null) {
            guideRippleViewView = new GuideRippleView(getContext());
            addView(guideRippleViewView, params);
        } else {
            guideRippleViewView.setLayoutParams(params);
        }
//        guideRippleView.initCirculars(2, new int[]{Color.BLUE, Color.GREEN}, 32, 1.0f);
//        guideRippleView.setClip(targetRect.width(), targetRect.height());
    }

    public interface OnCustomViewInitializeCallback<V extends View> {
        /**
         * Layout custom view base on target view with target display rect area by yourself.
         *
         * @param guideLayout guide layout.
         * @param customView  custom view.
         * @param targetRect  target rect area.
         */
        public void onCustomViewInitialize(GuideLayout guideLayout, @Nullable V customView, @NonNull Rect targetRect);
    }
}
