package jsc.kit.component.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.io.IOException;
import java.io.InputStream;

import jsc.kit.component.R;

/**
 * Camera mask.
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class CameraMask extends FrameLayout {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private Matrix matrix = new Matrix();
    private int topMargin;

    public CameraMask(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CameraMask(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CameraMask(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraMask(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CameraMask, defStyleAttr, 0);
        topMargin = array.getDimensionPixelSize(R.styleable.CameraMask_cm_top_margin, 0);
        array.recycle();

        paint.setColor(Color.BLACK);
        paint.setAlpha(0x99);
        setWillNotDraw(false);
        setBitmap(decodeDefaultCameraLens());
    }

    private Bitmap decodeDefaultCameraLens(){
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = getResources().getAssets().open("default_camera_lens.png");
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return bitmap;
    }

    public int getTopMargin() {
        return topMargin;
    }

    /**
     * Set the margin of camera lens from top, px unit.
     *
     * @param topMargin to margin
     */
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        invalidate();
    }

    public int getMaskAlpha(){
        return paint.getAlpha();
    }

    /**
     * Set the alpha of mask exclude camera lens.
     *
     * @param maskAlpha mask alpha
     */
    public void setMaskAlpha(@IntRange(from = 0, to = 0xFF) int maskAlpha) {
        paint.setAlpha(maskAlpha);
        invalidate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Set camera lens. The picture should be square size.
     *
     * @param bitmap camera lens
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int targetSize = getWidth() * 3 / 4;
        int left = (getWidth() - targetSize) / 2;

        canvas.drawRect(0, 0, getWidth(), topMargin, paint);
        canvas.drawRect(0, topMargin + targetSize, getWidth(), getHeight(), paint);
        canvas.drawRect(0, topMargin, left, topMargin + targetSize, paint);
        canvas.drawRect(left + targetSize, topMargin, left + targetSize + left, topMargin + targetSize, paint);

        if (bitmap != null){
            float scale = targetSize * 1.0f / bitmap.getWidth();
            matrix.setScale(scale, scale);
            canvas.save();
            canvas.translate(left, topMargin);
            canvas.drawBitmap(bitmap, matrix, null);
            canvas.restore();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
        super.onDetachedFromWindow();
    }
}
