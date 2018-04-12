package jsc.kit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class DotView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    @ColorInt
    private int bgColor = Color.RED;
    private String text;
    private float textSize;
    @ColorInt
    private int textColor = Color.WHITE;

    private Rect rect = new Rect();

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint.setStyle(Paint.Style.FILL);
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, context.getResources().getDisplayMetrics());
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        postInvalidate();
    }

    public int getUnReadCount(){
        int unReadCount;
        try {
            unReadCount = Integer.parseInt(text);
        } catch (NumberFormatException ex){
            unReadCount = 0;
        }
        return unReadCount;
    }

    public void setUnReadCount(int count) {
        if (count < 1)
            text = "";

        else if (count > 99)
            text = "99+";

        else
            text = String.valueOf(count);
        postInvalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float center = getWidth() / 2.0f;
        paint.setColor(bgColor);
        canvas.drawCircle(center, center, center, paint);
        //draw text
        drawText(canvas, getWidth(), getWidth(), text, textColor, textSize, textPaint);
    }

    private void drawText(Canvas canvas, int w, int h, String text, int textColor, float textSize, TextPaint textPaint) {
        if (text == null || text.length() == 0)
            return;

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.getTextBounds(text, 0, text.length(), rect);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float baseLine = (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, (w - rect.width()) / 2.0f, baseLine, textPaint);
    }
}
