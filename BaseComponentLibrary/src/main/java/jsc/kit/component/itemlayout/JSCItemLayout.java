package jsc.kit.component.itemlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.kit.component.R;
import jsc.kit.component.widget.DotView;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class JSCItemLayout extends FrameLayout {

    private ImageView iconView;
    private TextView labelView;
    private DotView dotView;
    private ImageView arrowView;

    private String label;
    private int labelTextColor;
    private boolean showDot;
    private int dotSize;
    private int dotColor;
    private String dotText;
    private int dotTextColor;
    private float dotTextSize;
    private int iconId;
    private int arrowIconId;

    public JSCItemLayout(Context context) {
        this(context, null);
    }

    public JSCItemLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSCItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JSCItemLayout, defStyleAttr, 0);
        label = a.getString(R.styleable.JSCItemLayout_il_label);
        labelTextColor = a.getColor(R.styleable.JSCItemLayout_il_label_color, 0xFF333333);
        showDot = a.getBoolean(R.styleable.JSCItemLayout_il_show_dot, false);
        int defaultDotSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()) + 0.5f);
        dotSize = a.getDimensionPixelSize(R.styleable.JSCItemLayout_il_dot_size, defaultDotSize);
        dotColor = a.getColor(R.styleable.JSCItemLayout_il_dot_color, Color.RED);
        dotText = a.getString(R.styleable.JSCItemLayout_il_dot_text);
        dotTextColor = a.getColor(R.styleable.JSCItemLayout_il_dot_text_color, Color.WHITE);
        float defaultDotTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, context.getResources().getDisplayMetrics());
        dotTextSize = a.getDimension(R.styleable.JSCItemLayout_il_dot_text_size, defaultDotTextSize);
        iconId = a.getResourceId(R.styleable.JSCItemLayout_il_icon, R.drawable.kit_ic_assignment_blue_24dp);
        arrowIconId = a.getResourceId(R.styleable.JSCItemLayout_il_arrow_icon, R.drawable.kit_ic_chevron_right_gray_24dp);
        a.recycle();

        init(context);
    }

    private void init(Context context) {
        LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        contentParams.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        addView(layout, contentParams);

        iconView = new AppCompatImageView(context);
        iconView.setImageResource(iconId);
        layout.addView(iconView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        labelParams.leftMargin = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()) + 0.5f);
        labelView = new TextView(context);
        labelView.setText(label);
        labelView.setTextColor(labelTextColor);
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        layout.addView(labelView, labelParams);

        dotView = new DotView(context);
        if (dotText != null && !dotText.isEmpty()) {
            dotView.setText(dotText);
        }
        dotView.setBgColor(dotColor);
        dotView.setTextColor(dotTextColor);
        dotView.setTextSize(dotTextSize);
        dotView.setVisibility(showDot ? VISIBLE : INVISIBLE);
        layout.addView(dotView, new LinearLayout.LayoutParams(dotSize, dotSize));

        arrowView = new AppCompatImageView(context);
        arrowView.setImageResource(arrowIconId);
        layout.addView(arrowView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public ImageView getIconView() {
        return iconView;
    }

    public TextView getLabelView() {
        return labelView;
    }

    public DotView getDotView() {
        return dotView;
    }

    public ImageView getArrowView() {
        return arrowView;
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

    public void setLabel(String label) {
        this.label = label;
        labelView.setText(label);
    }
}
