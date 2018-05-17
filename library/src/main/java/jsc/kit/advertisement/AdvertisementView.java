package jsc.kit.advertisement;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import jsc.kit.R;

public class AdvertisementView extends FrameLayout {

    /**the code of clicking advertisement image*/
    public final static int ACTION_CODE_PREVIEW = 0x00;
    /**the code of clicking jump_button*/
    public final static int ACTION_CODE_JUMP = 0x01;
    /**the code of finishing count down timer*/
    public final static int ACTION_CODE_FINISH = 0x02;

    private AppCompatImageView imageView;
    private TextView textView;
    private OnComponentActionListener onComponentActionListener;

    private long millisInFuture;
    private long countDownInterval;
    private AdvertisementCountDownTimer advertisementCountDownTimer;

    public AdvertisementView(@NonNull Context context) {
        this(context, null);
    }

    public AdvertisementView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvertisementView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        imageView = new AppCompatImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(dp8 * 2, 0, dp8 * 2, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        textView.setBackgroundResource(R.drawable.kit_jump_background_shape);
        addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, dp8 * 3);
        params.gravity = Gravity.END;
        params.rightMargin = dp8 * 2;
        params.topMargin = dp8 * 2;
        addView(textView, params);


        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCountDown();
                if (onComponentActionListener != null) {
                    onComponentActionListener.onComponentAction(ACTION_CODE_PREVIEW, v);
                }
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCountDown();
                if (onComponentActionListener != null) {
                    onComponentActionListener.onComponentAction(ACTION_CODE_JUMP, v);
                }
            }
        });
    }

    public AppCompatImageView getImageView() {
        return imageView;
    }

    /**
     *
     * @param millisInFuture
     * @param countDownInterval
     * @param onComponentActionListener
     */
    public void init(long millisInFuture, long countDownInterval, OnComponentActionListener onComponentActionListener) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.onComponentActionListener = onComponentActionListener;
        textView.setText((millisInFuture / 1000) + "s\u2000跳过");
    }

    public void startCountDown() {
        if (advertisementCountDownTimer == null) {
            advertisementCountDownTimer = new AdvertisementCountDownTimer(millisInFuture, countDownInterval);
        }
        advertisementCountDownTimer.start();
    }

    public void cancelCountDown() {
        if (advertisementCountDownTimer != null) {
            advertisementCountDownTimer.cancel();
            advertisementCountDownTimer = null;
        }
    }

    public interface OnComponentActionListener {
        void onComponentAction(int actionCode, View view);
    }

    private class AdvertisementCountDownTimer extends CountDownTimer {

        AdvertisementCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long s = millisUntilFinished / 1000;
            textView.setText(s + "s\u2000跳过");
        }

        @Override
        public void onFinish() {
            textView.setText("0s\u2000跳过");
            textView.setEnabled(false);
            cancelCountDown();
            if (onComponentActionListener != null) {
                onComponentActionListener.onComponentAction(ACTION_CODE_FINISH, textView);
            }
        }
    }
}
