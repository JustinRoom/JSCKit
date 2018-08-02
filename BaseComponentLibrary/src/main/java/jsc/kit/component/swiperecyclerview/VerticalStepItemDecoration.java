package jsc.kit.component.swiperecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.logging.Level;

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

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    Rect rect = new Rect();
    Path path = new Path();
    Context context;
    int lineColor;
    int indexColor;
    float indexRadius;
    int indexTextColor;
    float indexTextSize;
    int location;

    public VerticalStepItemDecoration(Context context, int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        super(leftSpace, topSpace, rightSpace, bottomSpace);
        this.context = context;
        initializeDefaultSetting();
    }

    private void initializeDefaultSetting(){
        lineColor = 0xFFCCCCCC;
        indexColor = Color.BLUE;
        indexRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
        indexTextColor = Color.WHITE;
        indexTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, context.getResources().getDisplayMetrics());
        location = LEFT;
    }

    public VerticalStepItemDecoration setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public VerticalStepItemDecoration setIndexColor(int indexColor) {
        this.indexColor = indexColor;
        return this;
    }

    public VerticalStepItemDecoration setIndexRadius(float indexRadius) {
        this.indexRadius = indexRadius;
        return this;
    }

    public VerticalStepItemDecoration setIndexTextColor(int indexTextColor) {
        this.indexTextColor = indexTextColor;
        return this;
    }

    public VerticalStepItemDecoration setIndexTextSize(float indexTextSize) {
        this.indexTextSize = indexTextSize;
        return this;
    }

    public VerticalStepItemDecoration setLocation(int location) {
        this.location = location;
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
            centerX = left + Math.max(leftSpace / 2.0f, indexRadius);
        } else {
            centerX = right - Math.max(rightSpace / 2.0f, indexRadius);
        }

        textPaint.setColor(indexTextColor);
        textPaint.setTextSize(indexTextSize);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            centerY = child.getTop() + child.getHeight() / 2.0f;
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(lineColor);

            //TODO draw the vertical line
            if (i == 0){
                path.reset();
                path.moveTo(centerX, centerY);
                path.lineTo(centerX, child.getBottom() + bottomSpace);
                c.drawPath(path, paint);
            } else if (i == childCount - 1){
                path.reset();
                path.moveTo(centerX, child.getTop() - topSpace);
                path.lineTo(centerX, centerY);
                c.drawPath(path, paint);
            } else {
                path.reset();
                path.moveTo(centerX, child.getTop() - topSpace);
                path.lineTo(centerX, child.getBottom() + bottomSpace);
                c.drawPath(path, paint);
            }

            //TODO draw circle
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(indexColor);
            c.drawCircle(centerX, centerY, indexRadius, paint);

            //TODO draw index
            String s = String.valueOf(parent.getChildAdapterPosition(child) + 1);
            textPaint.getTextBounds(s, 0, s.length(), rect);
            float start = centerX - rect.width() / 2.0f;
            float baseLine = centerY - rect.height() / 2.0f + (rect.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            c.drawText(s, start, baseLine, textPaint);
        }
    }
}
