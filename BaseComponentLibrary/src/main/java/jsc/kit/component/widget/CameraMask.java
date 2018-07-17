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
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import jsc.kit.component.R;

/**
 * Camera mask.
 * <p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class CameraMask extends FrameLayout {

    private final static String TAG = "CameraMask";
    public final static int LOCATION_BELOW_CAMERA_LENS = 0;
    public final static int LOCATION_ABOVE_CAMERA_LENS = 1;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect cameraLensRect = new Rect();
    private Bitmap bitmap;
    private Matrix matrix = new Matrix();
    private int maskColor;
    private int topMargin;

    private TextView textView;
    private int textLocation;
    private int textVerticalMargin;

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
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CameraMask, defStyleAttr, 0);
        topMargin = a.getDimensionPixelSize(R.styleable.CameraMask_cm_top_margin, 0);
        maskColor = a.getColor(R.styleable.CameraMask_cm_mask_color, 0x99000000);

        String text = a.getString(R.styleable.CameraMask_cm_text);
        int textColor = a.getColor(R.styleable.CameraMask_cm_text_color, Color.WHITE);
        if (a.hasValue(R.styleable.CameraMask_cm_text_background)) {
            Drawable drawable = a.getDrawable(R.styleable.CameraMask_cm_text_background);
            textView.setBackground(drawable);
        }
        if (a.hasValue(R.styleable.CameraMask_cm_text_background_color)) {
            int textBackgroundColor = a.getColor(R.styleable.CameraMask_cm_text_background_color, Color.TRANSPARENT);
            textView.setBackgroundColor(textBackgroundColor);
        }
        textLocation = a.getInt(R.styleable.CameraMask_cm_text_location, LOCATION_BELOW_CAMERA_LENS);
        textVerticalMargin = a.getDimensionPixelSize(R.styleable.CameraMask_cm_text_margin, 0);
        a.recycle();
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        setWillNotDraw(false);
        setCameraLensBitmap(decodeDefaultCameraLens());
    }

    private Bitmap decodeDefaultCameraLens() {
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

    public TextView getTextView() {
        return textView;
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
        cameraLensRect.top = topMargin;
        cameraLensRect.bottom = topMargin + cameraLensRect.width();
        //The change action of top margin is associated with text view's location.
        requestLayout();
    }

    /**
     *
     * @return the location of text view. One of {@link #LOCATION_BELOW_CAMERA_LENS}、{@link #LOCATION_ABOVE_CAMERA_LENS}
     */
    public int getTextLocation() {
        return textLocation;
    }

    /**
     * Set the location of text view.
     *
     * @param textLocation One of {@link #LOCATION_BELOW_CAMERA_LENS}、{@link #LOCATION_ABOVE_CAMERA_LENS}
     */
    public void setTextLocation(int textLocation) {
        this.textLocation = textLocation;
        requestLayout();
    }

    /**
     *
     * @return the vertical margin of text view from camera lens.
     */
    public int getTextVerticalMargin() {
        return textVerticalMargin;
    }

    /**
     *
     * @param textVerticalMargin the vertical margin of text view from camera lens.
     */
    public void setTextVerticalMargin(int textVerticalMargin) {
        this.textVerticalMargin = textVerticalMargin;
        requestLayout();
    }

    @Deprecated
    public int getMaskAlpha() {
        return paint.getAlpha();
    }

    /**
     * Set the alpha of mask exclude camera lens.
     *
     * @param maskAlpha mask alpha
     * @deprecated use {@link #setMaskColor(int)} instead of.
     */
    @Deprecated
    public void setMaskAlpha(@IntRange(from = 0, to = 0xFF) int maskAlpha) {
//        paint.setAlpha(maskAlpha);
//        invalidate();
    }

    /**
     * Set the mask color.
     *
     * @param maskColor ARGB color
     */
    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
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
    public void setCameraLensBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    /**
     * Get the camera lens location inside it's parent.
     *
     * @return camera lens location
     */
    public Rect getCameraLensRect() {
        return cameraLensRect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        initCameraLensSize(getMeasuredWidth());
        MarginLayoutParams params = (MarginLayoutParams) textView.getLayoutParams();
        params.leftMargin = cameraLensRect.left;
        params.rightMargin = cameraLensRect.left;
        switch (textLocation) {
            case LOCATION_BELOW_CAMERA_LENS:
                params.topMargin = cameraLensRect.bottom + textVerticalMargin;
                break;
            case LOCATION_ABOVE_CAMERA_LENS:
                params.topMargin = cameraLensRect.top - textVerticalMargin;
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: ");

    }

    private void initCameraLensSize(int width) {
        int cameraLensSize = width * 3 / 4;
        int left = (width - cameraLensSize) / 2;
        cameraLensRect.set(left, topMargin, left + cameraLensSize, topMargin + cameraLensSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, getWidth(), topMargin, paint);
        canvas.drawRect(0, cameraLensRect.bottom, getWidth(), getHeight(), paint);
        canvas.drawRect(0, topMargin, cameraLensRect.left, cameraLensRect.bottom, paint);
        canvas.drawRect(cameraLensRect.right, topMargin, getWidth(), cameraLensRect.bottom, paint);

        if (bitmap != null) {
            float scale = cameraLensRect.width() * 1.0f / bitmap.getWidth();
            matrix.setScale(scale, scale);
            canvas.save();
            canvas.translate(cameraLensRect.left, topMargin);
            canvas.drawBitmap(bitmap, matrix, null);
            canvas.restore();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDetachedFromWindow();
    }
}
