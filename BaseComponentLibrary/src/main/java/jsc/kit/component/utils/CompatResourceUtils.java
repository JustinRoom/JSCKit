package jsc.kit.component.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class CompatResourceUtils {

    //color
    public static int getColor(@NonNull Context context, @ColorRes int resId){
        int color;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            color = context.getResources().getColor(resId, context.getTheme());
        } else {
            color = context.getResources().getColor(resId);
        }
        return color;
    }

    //drawable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId){
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            drawable = context.getResources().getDrawable(resId, context.getTheme());
        } else {
            drawable = context.getResources().getDrawable(resId);
        }
        return drawable;
    }

    //dimension
    public static int getDimensionPixelSize(@NonNull Context context, @DimenRes int id){
        return context.getResources().getDimensionPixelSize(id);
    }

    public static int getDimensionPixelSize(@NonNull View view, @DimenRes int id){
        return view.getResources().getDimensionPixelSize(id);
    }

    public static int getDimensionPixelSize(@NonNull Fragment fragment, @DimenRes int id){
        return fragment.getResources().getDimensionPixelSize(id);
    }
}
