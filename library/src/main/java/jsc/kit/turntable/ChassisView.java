package jsc.kit.turntable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ChassisView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();
    private RectF textRectF = new RectF();
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private List<GiftEntity> gifts = new ArrayList<>();

    public ChassisView(Context context) {
        this(context, null);
    }

    public ChassisView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChassisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        textPaint.setColor(0xff333333);
        setLabelTextSize(12.0f);
    }

    public List<GiftEntity> getGifts() {
        return gifts;
    }

    public void setGifts(List<GiftEntity> gifts) {
        this.gifts.clear();
        if (gifts != null)
            this.gifts.addAll(gifts);
        postInvalidate();
    }

    public int getGiftCount() {
        return gifts.size();
    }

    /**
     * @param labelTextSize the unit is sp.
     */
    public void setLabelTextSize(float labelTextSize) {
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, labelTextSize, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int count = gifts.size();
        int perAngle = 360 / count;
        float startAngle = -90 - perAngle / 2.0f;
        for (int i = 0; i < count; i++) {
            GiftEntity entity = gifts.get(i);
            entity.setStartAngle(startAngle + perAngle * i);
            entity.setSweepAngle(perAngle);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.set(0, 0, getWidth(), getWidth());
        textRectF.set(20, 20, getWidth() - 20, getWidth() - 20);
        int count = gifts.size();
        for (int i = 0; i < count; i++) {
            GiftEntity entity = gifts.get(i);
            paint.setColor(entity.getBackgroundColor());
            canvas.drawArc(rectF, entity.getStartAngle(), entity.getSweepAngle(), true, paint);
            drawPathText(canvas, textRectF, entity, textPaint);
        }
    }

    private void drawPathText(Canvas canvas, RectF rectF, GiftEntity entity, TextPaint textPaint) {
        String label = entity.getLabel();
        if (label == null || label.trim().isEmpty())
            return;

        Rect rect = new Rect();
        textPaint.setColor(entity.getLabelTextColor());
        textPaint.getTextBounds(label, 0, label.length(), rect);
        int labelWidth = rect.right - rect.left;
        int labelHeight = rect.bottom - rect.top;
        Path path = new Path();
        path.addArc(rectF, entity.getStartAngle(), entity.getSweepAngle());
        float hOffset = (new PathMeasure(path, false).getLength() - labelWidth) / 2.0f;
        canvas.drawTextOnPath(label, path, hOffset, labelHeight, textPaint);
    }
}
