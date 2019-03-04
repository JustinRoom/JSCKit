package jsc.kit.component.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class LabelValueView extends LinearLayout {

    TextView labelTextView;
    TextView valueTextView;

    public LabelValueView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LabelValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LabelValueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LabelValueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);

        labelTextView = new TextView(context);
        addView(labelTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        valueTextView = new TextView(context);
        valueTextView.setGravity(Gravity.END);
        addView(valueTextView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        labelTextView.setHint("label");
        valueTextView.setHint("value");
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14, 14);
        setTextColor(0xFF666666, 0xFF333333);
    }

    public TextView getLabelTextView() {
        return labelTextView;
    }

    public TextView getValueTextView() {
        return valueTextView;
    }

    public void setText(CharSequence label, CharSequence value){
        labelTextView.setText(label);
        valueTextView.setText(value);
    }

    public void setTextColor(@ColorInt int labelTextColor, @ColorInt int valueTextColor){
        labelTextView.setTextColor(labelTextColor);
        valueTextView.setTextColor(valueTextColor);
    }

    /**
     *
     * @param unit unit
     * @param labelTextSize label text size. It's unit is {@link TypedValue#COMPLEX_UNIT_SP}.
     * @param valueTextSize value text size. It's unit is {@link TypedValue#COMPLEX_UNIT_SP}.
     */
    public void setTextSize(int unit, float labelTextSize, float valueTextSize){
        labelTextView.setTextSize(unit,labelTextSize);
        valueTextView.setTextSize(unit,valueTextSize);
    }

    public void setTextGravity(int labelGravity, int valueGravity){
        labelTextView.setGravity(labelGravity);
        valueTextView.setGravity(valueGravity);
    }
}
