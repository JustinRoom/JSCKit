package jsc.kit.component.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class ScannerCameraMask extends CameraMask {

    private FrameLayout scannerBarContainer;
    private ImageView scannerBar;

    private int translationYDistance;

    public ScannerCameraMask(@NonNull Context context) {
        super(context);
    }

    public ScannerCameraMask(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScannerCameraMask(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScannerCameraMask(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
        LayoutParams scannerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scannerParams.gravity = Gravity.CENTER_HORIZONTAL;
        scannerBarContainer = new FrameLayout(context);
        addView(scannerBarContainer, scannerParams);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            initViewShapeProvider(scannerBarContainer);
//        }

        //>>>
        scannerBar = new ImageView(context);
        scannerBar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        scannerBar.setImageResource(R.drawable.kit_scanner_bar);
        scannerBarContainer.addView(scannerBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams scannerContainerParams = (MarginLayoutParams) scannerBarContainer.getLayoutParams();
        scannerContainerParams.width = getCameraLensRect().width();
        scannerContainerParams.height = getCameraLensRect().height();
        scannerContainerParams.topMargin = getCameraLensRect().top;
        //
        MarginLayoutParams scannerParams = (MarginLayoutParams) scannerBar.getLayoutParams();
        scannerParams.topMargin = -scannerBar.getMeasuredHeight();
        translationYDistance = getCameraLensRect().height() + scannerBar.getMeasuredHeight();
    }

    public void setScannerBarImageResource(@DrawableRes int drawable) {
        scannerBar.setImageResource(drawable);
        performRequestLayout();
    }

    public void setScannerBarImageBitmap(Bitmap bitmap) {
        scannerBar.setImageBitmap(bitmap);
        performRequestLayout();
    }

    @Override
    public void setTopMargin(int topMargin) {
        stopAnimation();
        super.setTopMargin(topMargin);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnimation();
            }
        }, 500);
    }

    @Override
    public void setSizeRatio(float sizeRatio) {
        stopAnimation();
        super.setSizeRatio(sizeRatio);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnimation();
            }
        }, 500);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnimation();
            }
        }, 500);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    /**
     * Set the outline shape provider of target view.
     *
     * @param targetView target view
     * @see ViewOutlineProvider
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initViewShapeProvider(View targetView) {
        targetView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                Rect rect = new Rect();
                int size = Math.min(view.getWidth(), view.getHeight());
                if (view.getWidth() > view.getHeight()) {
                    int left = (view.getWidth() - size) / 2;
                    rect.set(left, 0, left + size, view.getHeight());
                } else {
                    int top = (view.getHeight() - size) / 2;
                    rect.set(0, top, view.getWidth(), top + size);
                }
                switch (getMaskShape()) {
                    case MASK_SHAPE_SQUARE:
                        outline.setRect(rect);
                        break;
                    case MASK_SHAPE_CIRCULAR:
                        outline.setOval(rect);
                        break;
                }
            }
        });
    }

    private void performRequestLayout() {
        //stop animation
        stopAnimation();
        //The change action of top margin is associated with text view's location.
        requestLayout();
        //restart animation
        postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnimation();
            }
        }, 500);
    }

    ObjectAnimator animator;

    private void runAnimation() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(scannerBar, View.TRANSLATION_Y, 0, translationYDistance);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(3_000);
            animator.setRepeatCount(Animation.INFINITE);
        }
        if (!animator.isStarted()) {
            animator.start();
        }
    }

    private void stopAnimation() {
        if (animator != null && animator.isStarted()) {
            animator.cancel();
            animator = null;
        }
    }
}
