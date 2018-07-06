package jsc.exam.jsckit.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.kit.component.utils.AntiShakeUtils;
import jsc.kit.component.utils.CompatColorResourceUtils;
import jsc.kit.component.utils.dynamicdrawable.DynamicDrawableFactory;

public class CustomToastActivity extends BaseActivity {

    FrameLayout snackContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_toast);
        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        contentView.setGravity(Gravity.CENTER_HORIZONTAL);
        contentView.setWeightSum(3);
        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        TextView textView = new TextView(this);
        textView.setTextColor(DynamicDrawableFactory.colorStateList(Color.WHITE, CompatColorResourceUtils.getColor(this, R.color.colorAccent)));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setBackground(createRectButtonSelector());
        textView.setText("Show");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        contentView.addView(textView, params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetClick(v);
            }
        });

        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bp.topMargin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        Button button = new Button(this);
        button.setText("Snack");
        contentView.addView(button, bp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AntiShakeUtils.isInvalidClick(v))
                    return;
                Snackbar.make(v, "Hello, I'm snack.", Snackbar.LENGTH_SHORT).show();
            }
        });

        snackContainer = new FrameLayout(this);
        contentView.addView(snackContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
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
        int colorPrimaryDark = CompatColorResourceUtils.getColor(this, R.color.colorPrimaryDark);
        int colorPrimary = CompatColorResourceUtils.getColor(this, R.color.colorPrimary);
        int colorAccent = CompatColorResourceUtils.getColor(this, R.color.colorAccent);

        GradientDrawable pressed = new GradientDrawable();
        pressed.setShape(GradientDrawable.OVAL);
        pressed.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        pressed.setColors(new int[]{colorPrimaryDark, colorAccent, colorPrimaryDark});

        GradientDrawable normal = new GradientDrawable();
        normal.setShape(GradientDrawable.OVAL);
        normal.setStroke(4, colorPrimary);

        return DynamicDrawableFactory.stateListDrawable(null, pressed, null, null, normal);
    }

    private Drawable createRectButtonSelector(){
        GradientDrawable pressed = new GradientDrawable();
        pressed.setShape(GradientDrawable.RECTANGLE);
        pressed.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
        pressed.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        pressed.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        pressed.setColors(new int[]{0x66303F9F, 0xFF303F9F, 0x66303F9F});

        GradientDrawable normal = (GradientDrawable) pressed.getConstantState().newDrawable();
        normal.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return DynamicDrawableFactory.stateListDrawable(null, pressed, null, null, normal);
    }

    private Drawable createButtonSelector(){
        int pressedColor = CompatColorResourceUtils.getColor(this, R.color.colorPrimaryDark);
        int normalColor = CompatColorResourceUtils.getColor(this, R.color.colorAccent);
        float cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        Drawable pressed = DynamicDrawableFactory.cornerRectangleDrawable(pressedColor, cornerRadius);
        Drawable normal = DynamicDrawableFactory.cornerRectangleDrawable(normalColor, cornerRadius);
        return DynamicDrawableFactory.stateListDrawable(null, pressed, null, null, normal);
    }
}
