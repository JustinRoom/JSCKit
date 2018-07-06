package jsc.kit.component.utils;

import android.content.Context;
import android.support.annotation.ColorRes;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class CompatColorResourceUtils {

    public static int getColor(Context context, @ColorRes int resId){
        int color;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            color = context.getResources().getColor(resId, context.getTheme());
        } else {
            color = context.getResources().getColor(resId);
        }
        return color;
    }
}
