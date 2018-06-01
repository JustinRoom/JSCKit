package jsc.exam.jsckit.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.kit.component.utils.dynamicdrawable.DynamicDrawableFactory;

public class CustomToastActivity extends ABaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_toast);
        FrameLayout contentView = new FrameLayout(this);
        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        TextView textView = new TextView(this);
        textView.setTextColor(DynamicDrawableFactory.colorStateList(Color.WHITE, getResources().getColor(R.color.colorAccent)));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setBackground(createCircleButtonSelector());
        textView.setText("Show");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        contentView.addView(textView, params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetClick(v);
            }
        });
    }

    public void widgetClick(View v) {
        showBottomSheetDialog();
    }

    BottomSheetDialog dialog;

    private void showBottomSheetDialog() {
        if (dialog == null) {
            Drawable.ConstantState constantState = createButtonSelector().getConstantState();
            int padding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(padding, padding * 3 / 4, padding, padding * 3 / 4);
            //
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.bottomMargin = padding / 4;
            Button btnToast = new Button(this);
            btnToast.setBackground(constantState == null ? null : constantState.newDrawable());
            btnToast.setTextColor(Color.WHITE);
            btnToast.setText("Show Toast");
            layout.addView(btnToast, params1);
            btnToast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    showCustomToast("Hello, I am a custom toast.");
                }
            });
            //
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.bottomMargin = padding / 4;
            Button btnCancel = new Button(this);
            btnCancel.setBackground(constantState == null ? null : constantState.newDrawable());
            btnCancel.setTextColor(Color.WHITE);
            btnCancel.setText("Cancel");
            layout.addView(btnCancel, params2);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog = new BottomSheetDialog(this);
            dialog.setContentView(layout);
        }
        if (!dialog.isShowing())
            dialog.show();
    }

    private Drawable createCircleButtonSelector(){
        int colorPrimaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        int colorAccent = getResources().getColor(R.color.colorAccent);

        GradientDrawable pressed = new GradientDrawable();
        pressed.setShape(GradientDrawable.OVAL);
        pressed.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        pressed.setColors(new int[]{colorPrimaryDark, colorAccent, colorPrimaryDark});

        GradientDrawable normal = new GradientDrawable();
        normal.setShape(GradientDrawable.OVAL);
        normal.setStroke(4, colorPrimary);

        return DynamicDrawableFactory.stateListDrawable(null, pressed, null, null, normal);
    }

    private Drawable createButtonSelector(){
        int pressedColor = getResources().getColor(R.color.colorPrimaryDark);
        int normalColor = getResources().getColor(R.color.colorAccent);
        float cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        Drawable pressed = DynamicDrawableFactory.cornerRectangleDrawable(pressedColor, cornerRadius);
        Drawable normal = DynamicDrawableFactory.cornerRectangleDrawable(normalColor, cornerRadius);
        return DynamicDrawableFactory.stateListDrawable(null, pressed, null, null, normal);
    }
}
