package jsc.kit.component.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public final class CustomToast {

    public static void showCustomToast(Context context, CharSequence txt){
        TextView textView = new TextView(context);
        int padding = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()) + 0.5f);
        textView.setPadding(0, padding, 0, padding);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(0x33333333);
        textView.setTextColor(0xFF333333);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setText(txt);
        showCustomToast(context, textView);
    }

    public static void showCustomToast(Context context, @NonNull View view){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, WindowUtils.getActionBarSize(context));
        toast.setView(view);

        try {
            Method method = Toast.class.getMethod("getWindowParams");
            method.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) method.invoke(toast);
            if (params != null){
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.windowAnimations = R.style.customToastWindowAnimations;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        toast.show();
    }
}
