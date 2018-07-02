package jsc.kit.component.advertisement;

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

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class AdvertisementView extends FrameLayout {

    private final static String DEFAULT_COUNT_DOWN_FORMATTER_LABEL = "%1$ds\u2000跳过";
    /**
     * the code of clicking advertisement image
     */
    public final static int ACTION_CODE_PREVIEW = 0x00;
    /**
     * the code of clicking jump_button
     */
    public final static int ACTION_CODE_JUMP = 0x01;
    /**
     * the code of finishing count down timer
     */
    public final static int ACTION_CODE_FINISH = 0x02;

    private AppCompatImageView imageView;
    private TextView textView;
    private OnComponentActionListener onComponentActionListener;

    private long millisInFuture;
    private long countDownInterval;
    private AdvertisementCountDownTimer advertisementCountDownTimer;
    private String countDownFormatterLabel = DEFAULT_COUNT_DOWN_FORMATTER_LABEL;

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

    /**
     * Because this is a formatter string, so it look like:"%1$d...".
     *
     * @param countDownFormatterLabel the formatter string
     * @see java.util.Formatter
     */
    public void setCountDownFormatterLabel(String countDownFormatterLabel) {
        this.countDownFormatterLabel = countDownFormatterLabel;
    }

    public String getCountDownFormatterLabel() {
        return countDownFormatterLabel;
    }

    public AppCompatImageView getImageView() {
        return imageView;
    }

    /**
     * @param millisInFuture            future time
     * @param countDownInterval         interval count down step
     * @param onComponentActionListener action listener
     */
    public void init(long millisInFuture, long countDownInterval, OnComponentActionListener onComponentActionListener) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.onComponentActionListener = onComponentActionListener;
        textView.setText(String.format(countDownFormatterLabel, millisInFuture / 1000));
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
            textView.setText(String.format(countDownFormatterLabel, s));
        }

        @Override
        public void onFinish() {
            textView.setText(String.format(countDownFormatterLabel, 0L));
            textView.setEnabled(false);
            cancelCountDown();
            if (onComponentActionListener != null) {
                onComponentActionListener.onComponentAction(ACTION_CODE_FINISH, textView);
            }
        }
    }
}
