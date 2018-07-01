package jsc.kit.datetimepicker.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.kit.datetimepicker.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseWheelDialog {

    protected Context context;
    protected Dialog dialog;
    protected FrameLayout fyHeader;
    protected TextView btnCancel, tvTitle, btnOK;
    protected View segmentLineView;
    protected LinearLayout lyPickerContainer;

    abstract void initView(LinearLayout pickerContainer);

    public BaseWheelDialog(Context context) {
        this.context = context;
        initDialog();
        initView(lyPickerContainer);
    }

    private void initDialog(){
        if (dialog == null) {
            dialog = new Dialog(context, R.style.DateTimePickerDialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.base_picker_layout);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = context.getResources().getDisplayMetrics().widthPixels;
            window.setAttributes(lp);

            fyHeader = dialog.findViewById(R.id.fy_header);
            btnCancel = dialog.findViewById(R.id.btn_cancel);
            tvTitle = dialog.findViewById(R.id.tv_title);
            btnOK = dialog.findViewById(R.id.btn_ok);
            segmentLineView = dialog.findViewById(R.id.segment_line_view);
            lyPickerContainer = dialog.findViewById(R.id.ly_picker_container);


        }
    }

    public Context getContext() {
        return context;
    }

    public  void updateBaseUI(BaseBuilder builder){
        if (builder != null){
            tvTitle.setText(builder.getTitle());
            btnCancel.setText(builder.getLeftText());
            btnOK.setText(builder.getRightText());
            tvTitle.setTextColor(builder.getTitleColor());
            btnCancel.setTextColor(builder.getLeftTextColor());
            btnOK.setTextColor(builder.getRightTextColor());
            segmentLineView.setBackgroundColor(builder.getSegmentLineColor());
            fyHeader.setBackgroundColor(builder.getTitleBarBackgroundColor());
            lyPickerContainer.setBackgroundColor(builder.getContentBackgroundColor());
        }
    }

    public void show(){
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    public void dismiss(){
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    protected void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }
}
