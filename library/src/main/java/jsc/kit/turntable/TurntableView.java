package jsc.kit.turntable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import jsc.kit.R;

public class TurntableView extends FrameLayout {

    public final static int ROTATION_TYPE_CHASSIS = 0;
    public final static int ROTATION_TYPE_COMPASS = 1;

    @IntDef({ROTATION_TYPE_CHASSIS, ROTATION_TYPE_COMPASS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RotationType {
    }

    private ChassisView chassisView;
    private ImageView compassView;
    private int rotationType = ROTATION_TYPE_CHASSIS;
    private OnTurnListener onTurnListener;
    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            View target = rotationType == ROTATION_TYPE_CHASSIS ? chassisView : compassView;
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
        }

        @Override
        public void onAnimationCancel(Animator animation) {

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
        chassisView.setRotation(0);
        compassView.setRotation(0);
    }

    /**
     * @param labelTextSizeSp the unit is sp.
     */
    public void setLabelTextSize(float labelTextSizeSp) {
        chassisView.setLabelTextSize(labelTextSizeSp);
    }

    public void setGifts(List<GiftEntity> gifts) {
        chassisView.setGifts(gifts);
    }

    public void turntableByAngle(int angle) {
        int giftCount = chassisView.getGiftCount();
        if (giftCount == 0 || angle == 0)
            return;

        View target = rotationType == ROTATION_TYPE_CHASSIS ? chassisView : compassView;
        int perAngle = 360 / giftCount;
        int turnCount = angle / perAngle;
        if (angle % perAngle > 0)
            turnCount++;
        turntable(target, turnCount, perAngle);
    }

    public void turntableByCount(int turnCount) {
        int giftCount = chassisView.getGiftCount();
        if (giftCount == 0 || turnCount == 0)
            return;

        View target = rotationType == ROTATION_TYPE_CHASSIS ? chassisView : compassView;
        int perAngle = 360 / giftCount;
        turntable(target, turnCount, perAngle);
    }

    private void turntable(View target, int turnCount, float perAngle) {
        float startAngle = target.getRotation();
        float endAngle = startAngle + turnCount * perAngle;
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, View.ROTATION, startAngle, endAngle).setDuration(turnCount * 50);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(animatorListener);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
