package jsc.kit.component.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import jsc.kit.component.R;
import jsc.kit.component.utils.dynamicdrawable.DynamicDrawableFactory;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public class LoadingDialog extends Dialog {

    private TextView textView;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
        FrameLayout contentView = new FrameLayout(context);
        contentView.setBackground(DynamicDrawableFactory.cornerRectangleDrawable(0x33000000, dp8));
        setContentView(contentView, new ViewGroup.LayoutParams(dp8 * 15, dp8 * 15));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        contentView.addView(linearLayout, params);
        //
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        linearLayout.addView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtParams.topMargin = dp8 / 2;
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        linearLayout.addView(textView, txtParams);
    }

    public void setMessage(String message) {
        textView.setText(message);
    }

    public void showMessageView(boolean show){
        textView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
