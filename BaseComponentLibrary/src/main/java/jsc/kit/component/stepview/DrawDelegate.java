package jsc.kit.component.stepview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * This delegate is for {@link VerticalStepLinearLayout} drawing steps.
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface DrawDelegate {
        /**
         * Draw the vertical line.
         * <br>Override this method to draw the vertical line on your own style.
         *
         * @param canvas canvas
         * @param centerX center x axis
         */
        public void drawLine(@NonNull Canvas canvas, float centerX);

        /**
         * Draw index.
         * <br>Override this method to draw index info on your own style.
         *
         * @param canvas canvas
         * @param index index
         * @param centerX center x axis
         * @param fontMetrics fontMetrics
         */
        public void drawIndex(@NonNull Canvas canvas, int index, float centerX, @NonNull Paint.FontMetrics fontMetrics);
    }