package jsc.exam.jsckit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;

/**
 * @author jsc
 */

public class CustomItemLayoutView extends LinearLayout {

    private ImageView iconView;
    private TextView labelView;
    private DotView dotView;
    private ImageView arrowView;

    private String label;
    private int labelColor;
    private boolean showDot;
    private int dotSize;
    private int dotColor;
    private String dotText;
    private int iconId;
    private int arrowIconId;

    public CustomItemLayoutView(Context context) {
        this(context, null);
    }

    public CustomItemLayoutView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomItemLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomItemLayoutView, defStyleAttr, 0);
        label = a.getString(R.styleable.CustomItemLayoutView_cilv_label);
        labelColor = a.getColor(R.styleable.CustomItemLayoutView_cilv_label_color, 0xFF333333);
        showDot = a.getBoolean(R.styleable.CustomItemLayoutView_cilv_show_dot, false);
        int defaultDotSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()) + 0.5f);
        dotSize = a.getDimensionPixelSize(R.styleable.CustomItemLayoutView_cilv_dot_size, defaultDotSize);
        dotColor = a.getColor(R.styleable.CustomItemLayoutView_cilv_dot_color, Color.RED);
        dotText = a.getString(R.styleable.CustomItemLayoutView_cilv_dot_text);
        iconId = a.getResourceId(R.styleable.CustomItemLayoutView_cilv_icon, R.drawable.ic_assignment_blue_24dp);
        arrowIconId = a.getResourceId(R.styleable.CustomItemLayoutView_cilv_arrow_icon, R.drawable.ic_chevron_right_gray_24dp);
        a.recycle();

        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        iconView = new AppCompatImageView(context);
        labelView = new TextView(context);
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        params.leftMargin = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()) + 0.5f);
        labelView.setLayoutParams(params);
        dotView = new DotView(context);
        arrowView = new AppCompatImageView(context);

        addView(iconView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(labelView);
        addView(dotView, new LayoutParams(dotSize, dotSize));
        addView(arrowView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        labelView.setText(label);
        labelView.setTextColor(labelColor);
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        if (dotText != null && !dotText.isEmpty())
            dotView.setText(dotText);
        dotView.setBgColor(dotColor);
        dotView.setVisibility(showDot ? VISIBLE : INVISIBLE);

        iconView.setImageResource(iconId);
        arrowView.setImageResource(arrowIconId);
    }

    public void setShowDot(boolean showDot) {
        this.showDot = showDot;
        dotView.setVisibility(showDot ? VISIBLE : INVISIBLE);
    }

    public boolean isShowDot() {
        return showDot;
    }

    public String getLabel() {
        return label;
    }
}
