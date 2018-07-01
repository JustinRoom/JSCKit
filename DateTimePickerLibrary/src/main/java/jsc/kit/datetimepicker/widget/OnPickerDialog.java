package jsc.kit.datetimepicker.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class OnPickerDialog extends BaseWheelDialog {

    private final String TAG = getClass().getSimpleName();
    private WheelView wheelView;
    private OnSelectedResultHandler selectedResultHandler;

    public OnPickerDialog(Context context) {
        super(context);
    }

    public void setSelectedResultHandler(OnSelectedResultHandler selectedResultHandler) {
        this.selectedResultHandler = selectedResultHandler;
    }

    @Override
    void initView(LinearLayout pickerContainer) {
        Context context = pickerContainer.getContext();
        int horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        int verticalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        pickerContainer.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        wheelView = new WheelView(context);
        wheelView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void onSelect(int index, String text) {
                Log.i(TAG, "onSelect: [index = " + index + ", text = " + text + "]");
            }
        });
        pickerContainer.addView(wheelView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedResultHandler != null)
                    selectedResultHandler.handleSelectedResult(
                            wheelView.getCurrentSelectedIndex(),
                            wheelView.getCurrentSelectedItem()
                    );
                dismiss();
            }
        });
    }

    public void updateUI(Builder builder) {
        updateBaseUI(builder);
        if (builder != null){
            wheelView.setIsLoop(builder.isLoop());
            wheelView.setColors(builder.getTextColor(), builder.getSelectedTextColor());
            float maxTextSize = builder.getMaxTextSize() > 0 ? builder.getMaxTextSize() : TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getContext().getResources().getDisplayMetrics());
            float minTextSize = builder.getMinTextSize() > 0 ? builder.getMinTextSize() : TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getContext().getResources().getDisplayMetrics());
            wheelView.setTextSize(maxTextSize, minTextSize);
            wheelView.setTextAlpha(builder.getMaxTextAlpha(), builder.getMinTextAlpha());
            wheelView.setTextSpaceRatio(builder.getTextSpaceRatio());
        }
    }

    public void setLoop(boolean loop){
        wheelView.setIsLoop(loop);
    }

    public void setItems(List<String> items){
        wheelView.setItems(items);
    }

    public void setSelected(@IntRange(from = 0) int selected){
        wheelView.setSelected(selected);
    }

    public void show(@IntRange(from = 0) int selected) {
        setSelected(selected);
        show();
    }

    public interface OnSelectedResultHandler {
        void handleSelectedResult(int index, String item);
    }
}
