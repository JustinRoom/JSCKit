package jsc.kit.turntable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import jsc.kit.R;

public class TurntableView extends FrameLayout {

    public final static int ROTATION_TYPE_CHASSIS = 0;
    public final static int ROTATION_TYPE_COMPASS = 1;

    @IntDef({ROTATION_TYPE_CHASSIS, ROTATION_TYPE_COMPASS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RotationType {
    }

    private ChassisView chassisView;//转盘底盘
    private ImageView compassView;//转盘指针
    private int rotationType = ROTATION_TYPE_CHASSIS;
    private ObjectAnimator animator;
    private boolean isRotating = false;
    private OnTurnListener onTurnListener;
    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isRotating = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            View target = rotationType == ROTATION_TYPE_CHASSIS ? chassisView : compassView;
            //回收view在GPU的off-screen中的缓存。
            target.setLayerType(LAYER_TYPE_NONE, null);
            int giftCount = chassisView.getGiftCount();
            int perAngle = 360 / giftCount;
            float rotation = target.getRotation() % 360;
            int index = (int) (rotation / perAngle);
            if (rotationType == ROTATION_TYPE_CHASSIS)
                index = giftCount - index;
            index = index % giftCount;
            GiftEntity entity = chassisView.getGifts().get(index);
            if (onTurnListener != null)
                onTurnListener.onTurnEnd(index, entity);
            isRotating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isRotating = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public interface OnTurnListener {
        void onTurnEnd(int index, GiftEntity entity);
    }

    public TurntableView(@NonNull Context context) {
        this(context, null);
    }

    public TurntableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TurntableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        chassisView = new ChassisView(context);
        addView(chassisView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        compassView = new ImageView(context);
        compassView.setImageResource(R.drawable.ic_luck_compass);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(compassView, params);
    }

    public int getRotationType() {
        return rotationType;
    }

    public void setRotationType(@RotationType int rotationType) {
        this.rotationType = rotationType;
    }

    public OnTurnListener getOnTurnListener() {
        return onTurnListener;
    }

    public void setOnTurnListener(OnTurnListener onTurnListener) {
        this.onTurnListener = onTurnListener;
    }

    public void reset() {
        //stop animation
        if (animator != null && animator.isRunning())
            animator.cancel();

        //reset rotation.
        chassisView.setRotation(0);
        compassView.setRotation(0);
    }

    /**
     * it is rotating or not.
     * @return
     */
    public boolean isRotating() {
        return isRotating;
    }

    /**
     * Set the text size of label. The default value is 12.
     *
     * @param labelTextSizeSp the unit is sp.
     */
    public void setLabelTextSize(float labelTextSizeSp) {
        chassisView.setLabelTextSize(labelTextSizeSp);
    }

    public void setGifts(List<GiftEntity> gifts) {
        chassisView.setGifts(gifts);
    }

    /**
     * 刷新视图。<br/>
     * 例如：礼品图片是网络图片，执行以下步骤：<br/>
     * 1、获取网络图片<br/>
     * 2、设置Bitmap。{@link GiftEntity#setBitmap(Bitmap)}<br/>
     * 3、调用此方法重新绘制。<br/>
     */
    public void notifyDataSetChanged() {
        chassisView.postInvalidate();
    }

    public ChassisView getChassisView() {
        return chassisView;
    }

    public ImageView getCompassView() {
        return compassView;
    }

    /**
     * 旋转特定的角度<br/>
     * 假如需要旋转2000度，而礼品种类数是12，那么它实际上旋转2010度。计算如下：<br/>
     * <code>
     * 360 / 12 = 30
     * 2000 / 30 = 66
     * 2000 - 30 * 66 = 20
     * 实际旋转角度 = 30 * (66 + 1) = 2010
     * </code>
     *
     * @param angle
     */
    public void turntableByAngle(int angle) {
        int giftCount = chassisView.getGiftCount();
        if (isRotating || giftCount == 0 || angle == 0)
            return;

        View target = rotationType == ROTATION_TYPE_CHASSIS ? chassisView : compassView;
        int perAngle = 360 / giftCount;
        int turnCount = angle / perAngle;
        if (angle % perAngle > 0)
            turnCount++;
        turntable(target, turnCount, perAngle);
    }

    /**
     * 旋转特定的角度<br/>
     * 假如需要旋转turnCount = 12 * 10 + 7，而礼品种类数是12，那么它实际上旋转3810度。计算如下：<br/>
     * <code>
     * 360 / 12 = 30
     * 实际旋转角度 = 30 * 127 = 3810
     * </code>
     *
     * @param turnCount
     */
    public void turntableByCount(int turnCount) {
        int giftCount = chassisView.getGiftCount();
        if (isRotating || giftCount == 0 || turnCount == 0)
            return;

        View target = rotationType == ROTATION_TYPE_CHASSIS ? chassisView : compassView;
        int perAngle = 360 / giftCount;
        turntable(target, turnCount, perAngle);
    }

    private void turntable(View target, int turnCount, float perAngle) {
        float startAngle = target.getRotation();
        float endAngle = startAngle + turnCount * perAngle;
        //开启GPU的off-screen缓存区，提高动画的流畅度。
        //注意：一定要记得在动画完成之后回收该缓存。
        target.setLayerType(LAYER_TYPE_HARDWARE, null);
        animator = ObjectAnimator.ofFloat(target, View.ROTATION, startAngle, endAngle).setDuration(turnCount * 50);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(animatorListener);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //stop animation
        if (animator != null && animator.isRunning())
            animator.cancel();
    }
}
