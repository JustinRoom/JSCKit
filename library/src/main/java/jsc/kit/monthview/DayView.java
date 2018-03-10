package jsc.kit.monthview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author jsc
 */

public class DayView extends RelativeLayout {

    private TextView tvLabel;
    private TextView tvSubLabel;
    private DayItem dayItem;

    public DayView(@NonNull Context context) {
        this(context, null);
    }

    public DayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(layout, params);

        tvLabel = new TextView(context);
        tvLabel.setTextColor(0xFF333333);
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        layout.addView(tvLabel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        tvSubLabel = new TextView(context);
        tvSubLabel.setTextColor(Color.YELLOW);
        tvSubLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        layout.addView(tvSubLabel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setDayItem(DayItem dayItem) {
        this.dayItem = dayItem;
        notifyDataChanged();
    }

    public DayItem getDayItem() {
        return dayItem;
    }

    public void showSubLabel(boolean show){
        tvSubLabel.setVisibility(show ? VISIBLE : GONE);
    }

    public void notifyDataChanged(){
        if (dayItem == null)
            return;

        if (dayItem.getBackground() != -1)
            setBackgroundResource(dayItem.getBackground());

        tvLabel.setTextColor(dayItem.getLabelTextColor());
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, dayItem.getLabelTextSize());
        tvLabel.setText(TextUtils.isEmpty(dayItem.getLabel()) ? "" : dayItem.getLabel());

        tvSubLabel.setTextColor(dayItem.getSubLabelTextColor());
        tvSubLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, dayItem.getSubLabelTextSize());
        tvSubLabel.setText(TextUtils.isEmpty(dayItem.getSubLabel()) ? "" : dayItem.getSubLabel());
    }
}
