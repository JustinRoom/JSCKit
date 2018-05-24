package jsc.kit.component.bannerview.pageTransformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class AlphaPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        float alpha = 0.0f;
        if (0.0f <= position && position <= 1.0f) {
            alpha = 1.0f - position;
        } else if (-1.0f <= position && position < 0.0f) {
            alpha = position + 1.0f;
        }
        if (alpha < 0.5f)
            alpha = 0.5f;
        page.setAlpha(alpha);
    }
}