package jsc.kit.component.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class CompatDrawableResourceUtils {

    public static Drawable getDrawable(Context context, @DrawableRes int resId){
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            drawable = context.getResources().getDrawable(resId, context.getTheme());
        } else {
            drawable = context.getResources().getDrawable(resId);
        }
        return drawable;
    }
}
