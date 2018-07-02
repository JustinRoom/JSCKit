package jsc.kit.component.monthview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class MonthView extends LinearLayout {

    private List<DayView> dayViews = new ArrayList<>();
    private OnDayClickListener onDayClickListener;
    private OnDayLongClickListener onDayLongClickListener;

    private boolean showSubLabel;
    private int rowSpace;
    private int columnSpace;

    private View.OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onDayClickListener != null)
                onDayClickListener.onDayClick((DayView) v);
        }
    };

    private View.OnLongClickListener longClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return onDayLongClickListener != null && onDayLongClickListener.onDayLongClick((DayView) v);
        }
    };

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonthView, defStyleAttr, 0);
        showSubLabel = a.getBoolean(R.styleable.MonthView_mv_show_sub_label, true);
        rowSpace = a.getDimensionPixelSize(R.styleable.MonthView_mv_row_space, 40);
        columnSpace = a.getDimensionPixelSize(R.styleable.MonthView_mv_column_space, 20);
        a.recycle();
        if (rowSpace < 0)
            rowSpace = 0;
        if (columnSpace < 0)
            columnSpace = 0;
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        for (int i = 0; i < 5; i++) {
            LinearLayout rowLayout = new LinearLayout(context);
            rowLayout.setOrientation(HORIZONTAL);
            rowLayout.setWeightSum(7);
            rowLayout.setPadding(0, rowSpace / 2, 0, rowSpace / 2);
            for (int j = 0; j < 7; j++) {
                LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
                if (j == 0) {
                    params.rightMargin = columnSpace / 2;
                } else if (j == 6) {
                    params.leftMargin = columnSpace / 2;
                } else {
                    params.leftMargin = columnSpace / 2;
                    params.rightMargin = columnSpace / 2;
                }
                DayView dayView = new DayView(context);
                rowLayout.addView(dayView, params);
                dayViews.add(dayView);

                dayView.setOnClickListener(clickListener);
                dayView.setOnLongClickListener(longClickListener);
            }
            addView(rowLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }

        showSubLabel(showSubLabel);
        setCustomTitleView(null);
        setDays(null);
    }

    public void showSubLabel(boolean showSubLabel) {
        this.showSubLabel = showSubLabel;
        for (DayView v : dayViews) {
            v.showSubLabel(showSubLabel);
        }
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    public void setOnDayLongClickListener(OnDayLongClickListener onDayLongClickListener) {
        this.onDayLongClickListener = onDayLongClickListener;
    }

    public void setDays(List<DayItem> days) {
        if (days == null)
            days = new ArrayList<>();

        int len = days.size();
        for (int i = 0; i < dayViews.size(); i++) {
            DayView dayView = dayViews.get(i);
            dayView.setVisibility(i < len ? VISIBLE : INVISIBLE);
            if (i < len) {
                dayView.setDayItem(days.get(i));
            }
        }
    }

    public void notifyItemChanged(int index) {
        if (index < 0 || index > dayViews.size() - 1)
            return;
        dayViews.get(index).notifyDataChanged();
    }

    /**
     * 设置自定义title view
     *
     * @param customTitleView common title bar view
     */
    public void setCustomTitleView(View customTitleView) {
        if (customTitleView == null)
            customTitleView = getDefaultTitleView();

        if (getChildCount() > 5)
            removeViewAt(0);
        addView(customTitleView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private View getDefaultTitleView() {
        String[] title = {"日", "一", "二", "三", "四", "五", "六"};
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(HORIZONTAL);
        layout.setWeightSum(7);

        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(layout.getContext());
            textView.setTextColor(0xFF00BA86);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setGravity(Gravity.CENTER);
            layout.addView(textView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
            textView.setText(title[i]);
        }

        return layout;
    }

    public interface OnDayClickListener {
        void onDayClick(DayView dayView);
    }

    public interface OnDayLongClickListener {
        boolean onDayLongClick(DayView dayView);
    }
}
