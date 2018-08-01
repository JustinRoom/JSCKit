package jsc.kit.component.swiperecyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Vertical step {@link android.support.v7.widget.RecyclerView.ItemDecoration} for {@link RecyclerView}.
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class VerticalStepItemDecoration extends BlankSpaceItemDecoration {

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    @IntDef({LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Location {

    }

    int location = LEFT;
    float radius = 8.0f;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int lineColor = 0xFFCCCCCC;
    int dotColor = Color.BLUE;

    public VerticalStepItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        super(leftSpace, topSpace, rightSpace, bottomSpace);
    }

    public VerticalStepItemDecoration setLocation(@Location int location) {
        this.location = location;
        return this;
    }

    public VerticalStepItemDecoration setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public VerticalStepItemDecoration setDotColor(@ColorInt int dotColor) {
        this.dotColor = dotColor;
        return this;
    }

    public VerticalStepItemDecoration setRadius(@FloatRange(from = 0) float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        float centerX;
        float centerY;
        if (location == LEFT){
            centerX = left + Math.max(leftSpace, radius) / 2.0f;
        } else {
            centerX = right - Math.max(rightSpace, radius) / 2.0f;
        }

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            centerY = child.getTop() + child.getHeight() / 2.0f;
            int startY;
            int endY;
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(lineColor);

            //TODO draw the vertical line above circle
            if (childCount > 1 && i > 0){
                startY = child.getTop() - topSpace;
                endY = (int) (centerY - radius + .5f);
                c.drawLine(centerX, startY, centerX, endY, paint);
            }

            //TODO draw the vertical line above circle
            if (childCount > 1 && i < childCount - 1){
                startY = (int) (centerY + radius + .5f);
                endY = child.getBottom() + bottomSpace;
                c.drawLine(centerX, startY, centerX, endY, paint);
            }

            //TODO draw circle
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(dotColor);
            c.drawCircle(centerX, centerY, radius, paint);
        }
    }
}
