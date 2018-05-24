package jsc.kit.component.turntable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

/**
 * <p>转盘底盘</p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public class ChassisView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private List<GiftEntity> gifts = new ArrayList<>();
    private int[] bitmapTopMargin;

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
        bitmapTopMargin = new int[this.gifts.size()];
        postInvalidate();
    }

    public int getGiftCount() {
        return gifts.size();
    }

    /**
     * Set the text size of label. The default value is 12.
     *
     * @param labelTextSizeSp the unit is sp.
     */
    public void setLabelTextSize(float labelTextSizeSp) {
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, labelTextSizeSp, getResources().getDisplayMetrics()));
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int count = gifts.size();
        if (count == 0)
            return;

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
        int count = gifts.size();
        for (int i = 0; i < count; i++) {
            GiftEntity entity = gifts.get(i);
            paint.setColor(entity.getBackgroundColor());
            canvas.drawArc(rectF, entity.getStartAngle(), entity.getSweepAngle(), true, paint);
            bitmapTopMargin[i] = drawPathText(canvas, rectF, entity, textPaint);
        }

        for (int i = 0; i < count; i++) {
            GiftEntity entity = gifts.get(i);
            drawBitmap(canvas, i, entity);
        }
    }

    private int drawPathText(Canvas canvas, RectF rectF, GiftEntity entity, TextPaint textPaint) {
        int vOffset = 16;
        String label = entity.getLabel();
        if (label == null || label.trim().isEmpty())
            return vOffset;

        Rect rect = new Rect();
        textPaint.setColor(entity.getLabelTextColor());
        textPaint.getTextBounds(label, 0, label.length(), rect);
        int labelWidth = rect.right - rect.left;
        vOffset = vOffset + rect.bottom - rect.top;
        Path path = new Path();
        path.addArc(rectF, entity.getStartAngle(), entity.getSweepAngle());
        float hOffset = (new PathMeasure(path, false).getLength() - labelWidth) / 2.0f;
        canvas.drawTextOnPath(label, path, hOffset, vOffset, textPaint);
        return vOffset;
    }

    private void drawBitmap(Canvas canvas, int index, GiftEntity entity) {
        Bitmap bitmap = entity.getBitmap();
        if (bitmap == null)
            return;

        Rect dst = new Rect();
        dst.left = (getWidth() - bitmap.getWidth()) / 2;
        dst.top = bitmapTopMargin[index] + 16;
        dst.right = dst.left + bitmap.getWidth();
        dst.bottom = dst.top + bitmap.getHeight();
        canvas.save();
        canvas.rotate(-entity.getSweepAngle() * index, getWidth() / 2.0f, getHeight() / 2.0f);
        canvas.drawBitmap(bitmap, null, dst, null);
        canvas.restore();
    }
}
