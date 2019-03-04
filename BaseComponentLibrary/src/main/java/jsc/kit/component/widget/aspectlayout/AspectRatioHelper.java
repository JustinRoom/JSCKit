package jsc.kit.component.widget.aspectlayout;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class AspectRatioHelper {

    public static final int BASE_HORIZONTAL = 0;
    public static final int BASE_VERTICAL = 1;
    @IntDef({BASE_HORIZONTAL, BASE_VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BaseWhat {
    }

    private int baseWhat;
    private int aspectX;
    private int aspectY;

    public AspectRatioHelper() {
    }

    public AspectRatioHelper(int baseWhat, int aspectX, int aspectY) {
        this.baseWhat = baseWhat;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        if (this.aspectX <= 0)
            this.aspectX = 1;
        if (this.aspectY <= 0)
            this.aspectY = 1;
    }

    public int getBaseWhat() {
        return baseWhat;
    }

    public void setBaseWhat(@AspectRatioHelper.BaseWhat int baseWhat) {
        this.baseWhat = baseWhat;
    }

    public int getAspectX() {
        return aspectX;
    }

    public void setAspectX(int aspectX) {
        this.aspectX = aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }

    public void setAspectY(int aspectY) {
        this.aspectY = aspectY;
    }

    public int calHeightMeasureSpec(int width){
        int height =  width * aspectY / aspectX;
        return View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
    }

    public int calWidthMeasureSpec(int height){
        int width = height * aspectX / aspectY;
        return View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
    }
}
