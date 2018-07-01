package jsc.kit.datetimepicker.widget;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class TwoPickerDialog extends BaseWheelDialog {

    private final String TAG = getClass().getSimpleName();
    protected WheelView wheelView1;
    protected WheelView wheelView2;
    protected OnSelectedResultHandler selectedResultHandler;

    /**
     *
     * @param key zero presents the first WheelView, 1 second.
     * @param selectedIndex selected index
     * @param selectedItem selected item
     */
    protected void onSelectChange(int key, int selectedIndex, String selectedItem){}

    public TwoPickerDialog(Context context) {
        super(context);
    }

    public void setSelectedResultHandler(OnSelectedResultHandler selectedResultHandler) {
        this.selectedResultHandler = selectedResultHandler;
    }

    @CallSuper
    @Override
    void initView(LinearLayout pickerContainer) {
        Context context = pickerContainer.getContext();
        int verticalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        pickerContainer.setPadding(0, verticalPadding, 0, verticalPadding);
        wheelView1 = new WheelView(context);
        wheelView1.setIsLoop(false);
        wheelView1.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void onSelect(int index, String text) {
                Log.i(TAG, "onSelect: [index = " + index + ", text = " + text + "]");
                onSelectChange(0, index, text);
            }
        });
        pickerContainer.addView(wheelView1, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        wheelView2 = new WheelView(context);
        wheelView2.setIsLoop(false);
        wheelView2.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void onSelect(int index, String text) {
                Log.i(TAG, "onSelect: [index = " + index + ", text = " + text + "]");
                onSelectChange(1, index, text);
            }
        });
        pickerContainer.addView(wheelView2, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

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
                            wheelView1.getCurrentSelectedIndex(),
                            wheelView1.getCurrentSelectedItem(),
                            wheelView2.getCurrentSelectedIndex(),
                            wheelView2.getCurrentSelectedItem()
                    );
                dismiss();
            }
        });
    }

    public  void updateUI(Builder builder) {
        updateBaseUI(builder);
        if (builder != null){
            float maxTextSize = builder.getMaxTextSize() > 0 ? builder.getMaxTextSize() : TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getContext().getResources().getDisplayMetrics());
            float minTextSize = builder.getMinTextSize() > 0 ? builder.getMinTextSize() : TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getContext().getResources().getDisplayMetrics());
            wheelView1.setIsLoop(builder.isLoop());
            wheelView1.setColors(builder.getTextColor(), builder.getSelectedTextColor());
            wheelView1.setTextSize(maxTextSize, minTextSize);
            wheelView1.setTextAlpha(builder.getMaxTextAlpha(), builder.getMinTextAlpha());
            wheelView1.setTextSpaceRatio(builder.getTextSpaceRatio());
            wheelView2.setIsLoop(builder.isLoop());
            wheelView2.setColors(builder.getTextColor(), builder.getSelectedTextColor());
            wheelView2.setTextSize(maxTextSize, minTextSize);
            wheelView2.setTextAlpha(builder.getMaxTextAlpha(), builder.getMinTextAlpha());
            wheelView2.setTextSpaceRatio(builder.getTextSpaceRatio());
        }
    }

    public void setLoop1(boolean loop){
        wheelView1.setIsLoop(loop);
    }

    public void setLoop2(boolean loop){
        wheelView2.setIsLoop(loop);
    }

    public final void setItems1(List<String> items){
        wheelView1.setItems(items);
    }

    public final void setItems2(List<String> items){
        wheelView2.setItems(items);
    }

    public void setSelected1(@IntRange(from = 0) int selected){
        wheelView1.setSelected(selected);
    }

    public void setSelected2(@IntRange(from = 0) int selected){
        wheelView2.setSelected(selected);
    }

    public int getSelected1(){
        return wheelView1.getCurrentSelectedIndex();
    }

    public int getSelected2(){
        return wheelView2.getCurrentSelectedIndex();
    }

    public final void show(@IntRange(from = 0)int selected1, @IntRange(from = 0)int selected2) {
        setSelected1(selected1);
        setSelected2(selected2);
        show();
    }

    public interface OnSelectedResultHandler {
        void handleSelectedResult(int index1, String item1, int index2, String item2);
        void handleSelectedResult(@NonNull Date date);
    }
}
