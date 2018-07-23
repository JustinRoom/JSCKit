package jsc.kit.component.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

    @IntDef({LOCATION_BELOW_CAMERA_LENS, LOCATION_ABOVE_CAMERA_LENS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextLocation {
    }

    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect cameraLensRect = new Rect();
    private Matrix cameraLensMatrix;
    private Bitmap cameraLensBitmap;
    private int maskColor;//相机镜头遮罩颜色
    private Path scannerBoxAnglePath;
    private int scannerBoxBorderColor;//扫描框边的颜色
    private int scannerBoxBorderWidth;//扫描框边的粗细
    private int scannerBoxAngleColor;//扫描框四个角的颜色
    private int scannerBoxAngleBorderWidth;//扫描框四个角边的粗细
    private int scannerBoxAngleLength;//扫描框四个角边的长度
    private int topMargin;//相机镜头（或扫描框）与顶部的间距
    private float sizeRatio;//相机镜头（或扫描框）大小占View宽度的百分比

    protected TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    protected StaticLayout textStaticLayout;
    private String text;//提示文字
    private boolean textSingleLine;//提示文字是否填充父View的宽度。true与View等宽，false与相机镜头（或扫描框）等宽。
    private int textLocation;//提示文字位于相机镜头（或扫描框）上方（或下方）
    private int textVerticalMargin;//提示文字与相机镜头（或扫描框）的间距
    private int textLeftMargin;//提示文字与View（或相机镜头或扫描框）的左间距
    private int textRightMargin;//提示文字与View（或相机镜头或扫描框）的右间距

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CameraMask, defStyleAttr, 0);
        topMargin = a.getDimensionPixelSize(R.styleable.CameraMask_cm_topMargin, 0);
        scannerBoxBorderColor = a.getColor(R.styleable.CameraMask_cm_scannerBoxBorderColor, Color.WHITE);
        scannerBoxBorderWidth = a.getDimensionPixelSize(R.styleable.CameraMask_cm_scannerBoxBorderWidth, 1);
        scannerBoxAngleColor = a.getColor(R.styleable.CameraMask_cm_scannerBoxAngleColor, Color.YELLOW);
        int defaultScannerBoxAngleBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        scannerBoxAngleBorderWidth = a.getDimensionPixelSize(R.styleable.CameraMask_cm_scannerBoxAngleBorderWidth, defaultScannerBoxAngleBorderWidth);
        int defaultScannerBoxAngleLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        scannerBoxAngleLength = a.getDimensionPixelSize(R.styleable.CameraMask_cm_scannerBoxAngleLength, defaultScannerBoxAngleLength);
        maskColor = a.getColor(R.styleable.CameraMask_cm_maskColor, 0x99000000);
        maskShape = a.getInt(R.styleable.CameraMask_cm_maskShape, MASK_SHAPE_SQUARE);
        sizeRatio = a.getFloat(R.styleable.CameraMask_cm_sizeRatio, .6f);
        if (sizeRatio < .3f)
            sizeRatio = .3f;
        if (sizeRatio > 1.0f)
            sizeRatio = 1.0f;

        text = a.getString(R.styleable.CameraMask_cm_text);
        int textColor = a.getColor(R.styleable.CameraMask_cm_textColor, Color.WHITE);
        int defaultTextSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()) + .5f);
        float textSize = a.getDimensionPixelSize(R.styleable.CameraMask_cm_textSize, defaultTextSize);
        textSingleLine = a.getBoolean(R.styleable.CameraMask_cm_textSingleLine, false);
        textLocation = a.getInt(R.styleable.CameraMask_cm_textLocation, LOCATION_BELOW_CAMERA_LENS);
        textVerticalMargin = a.getDimensionPixelSize(R.styleable.CameraMask_cm_textVerticalMargin, 0);
        textLeftMargin = a.getDimensionPixelSize(R.styleable.CameraMask_cm_textLeftMargin, 0);
        textRightMargin = a.getDimensionPixelSize(R.styleable.CameraMask_cm_textRightMargin, 0);
        a.recycle();

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        setWillNotDraw(false);
    }

    public String getText() {
        return text;
    }

    /**
     * Set tip text.
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
        textStaticLayout = null;
        invalidate();
    }

    /**
     * Set tip text color.
     *
     * @param textColor text color
     */
    public void setTextColor(@ColorInt int textColor) {
        textPaint.setColor(textColor);
        invalidate();
    }

    /**
     * Set tip text size.
     *
     * @param textSize text size
     */
    public void setTextSize(@FloatRange(from = 0) float textSize) {
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public boolean isTextSingleLine() {
        return textSingleLine;
    }

    /**
     * @param textSingleLine single line text. {@code true}, the maximum width of text is {@link #getWidth()}, else is camera lens' width.
     */
    public void setTextSingleLine(boolean textSingleLine) {
        this.textSingleLine = textSingleLine;
        textStaticLayout = null;
        invalidate();
    }

    public int getTextLocation() {
        return textLocation;
    }

    /**
     * Set tip text location.
     *
     * @param textLocation text location. One fo {@link #LOCATION_BELOW_CAMERA_LENS}、{@link #LOCATION_ABOVE_CAMERA_LENS}
     */
    public void setTextLocation(@TextLocation int textLocation) {
        this.textLocation = textLocation;
        invalidate();
    }

    public int getTextVerticalMargin() {
        return textVerticalMargin;
    }

    /**
     * @param textVerticalMargin the text vertical margin from camera lens
     */
    public void setTextVerticalMargin(int textVerticalMargin) {
        this.textVerticalMargin = textVerticalMargin;
        invalidate();
    }

    public int getTextLeftMargin() {
        return textLeftMargin;
    }

    public int getTextRightMargin() {
        return textRightMargin;
    }

    /**
     * Set tip text left margin and right margin.
     *
     * @param textLeftMargin  text left margin
     * @param textRightMargin text right margin
     */
    public void setTextHorizontalMargin(int textLeftMargin, int textRightMargin) {
        this.textLeftMargin = textLeftMargin;
        this.textRightMargin = textRightMargin;
        textStaticLayout = null;
        invalidate();
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
        requestLayout();
    }

    public float getSizeRatio() {
        return sizeRatio;
    }

    /**
     * Set the size ratio of camera lens. The mini ratio is {@code 0.3f}, the maximum ratio is {@code 1.0f}.
     *
     * @param sizeRatio camera lens size ratio
     */
    public void setSizeRatio(@FloatRange(from = .3f, to = 1.0f) float sizeRatio) {
        this.sizeRatio = sizeRatio;
        textStaticLayout = null;
        requestLayout();
    }

    public Bitmap getCameraLensBitmap() {
        return cameraLensBitmap;
    }

    /**
     * Set a square_size camera lens {@link Bitmap}.
     *
     * @param bitmap camera lens
     */
    public void setCameraLensBitmap(Bitmap bitmap) {
        this.cameraLensBitmap = bitmap;
        invalidate();
    }

    public void recycle() {
        if (cameraLensBitmap != null) {
            cameraLensBitmap.recycle();
            cameraLensBitmap = null;
        }
    }

    public int getScannerBoxBorderColor() {
        return scannerBoxBorderColor;
    }

    public void setScannerBoxBorderColor(@ColorInt int scannerBoxBorderColor) {
        this.scannerBoxBorderColor = scannerBoxBorderColor;
        invalidate();
    }

    public int getScannerBoxBorderWidth() {
        return scannerBoxBorderWidth;
    }

    public void setScannerBoxBorderWidth(@IntRange(from = 1) int scannerBoxBorderWidth) {
        this.scannerBoxBorderWidth = scannerBoxBorderWidth;
        invalidate();
    }

    public int getScannerBoxAngleColor() {
        return scannerBoxAngleColor;
    }

    public void setScannerBoxAngleColor(@ColorInt int scannerBoxAngleColor) {
        this.scannerBoxAngleColor = scannerBoxAngleColor;
        invalidate();
    }

    public int getScannerBoxAngleBorderWidth() {
        return scannerBoxAngleBorderWidth;
    }

    public void setScannerBoxAngleBorderWidth(@IntRange(from = 1) int scannerBoxAngleBorderWidth) {
        this.scannerBoxAngleBorderWidth = scannerBoxAngleBorderWidth;
        invalidate();
    }

    public int getScannerBoxAngleLength() {
        return scannerBoxAngleLength;
    }

    public void setScannerBoxAngleLength(@IntRange(from = 0) int scannerBoxAngleLength) {
        this.scannerBoxAngleLength = scannerBoxAngleLength;
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
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        initCameraLensSize(getMeasuredWidth());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    private void initCameraLensSize(int width) {
        int cameraLensSize = (int) (width * sizeRatio);
        int left = (width - cameraLensSize) / 2;
        cameraLensRect.set(left, topMargin, left + cameraLensSize, topMargin + cameraLensSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawMask(canvas);
        drawMask(canvas, maskShape);

        float translateX = 0;
        float translateY = 0;
        if (cameraLensBitmap != null) {
            if (cameraLensMatrix == null) {
                cameraLensMatrix = new Matrix();
            }
            translateX = cameraLensRect.left;
            translateY = topMargin;
            float scale = cameraLensRect.width() * 1.0f / cameraLensBitmap.getWidth();
            cameraLensMatrix.setScale(scale, scale);
            canvas.save();
            canvas.translate(translateX, translateY);
            canvas.drawBitmap(cameraLensBitmap, cameraLensMatrix, null);
            canvas.translate(-translateX, -translateY);
            canvas.restore();
        } else {
            paint.setStyle(Paint.Style.STROKE);
            if (scannerBoxAnglePath == null) {
                scannerBoxAnglePath = new Path();
            }
            paint.setStrokeWidth(scannerBoxBorderWidth);
            paint.setColor(scannerBoxBorderColor);
            canvas.drawRect(cameraLensRect, paint);

            paint.setStrokeWidth(scannerBoxAngleBorderWidth);
            paint.setColor(scannerBoxAngleColor);
            //左上角
            scannerBoxAnglePath.reset();
            scannerBoxAnglePath.moveTo(cameraLensRect.left, cameraLensRect.top + scannerBoxAngleLength);
            scannerBoxAnglePath.lineTo(cameraLensRect.left, cameraLensRect.top);
            scannerBoxAnglePath.lineTo(cameraLensRect.left + scannerBoxAngleLength, cameraLensRect.top);
            canvas.drawPath(scannerBoxAnglePath, paint);
            //右上角
            scannerBoxAnglePath.reset();
            scannerBoxAnglePath.moveTo(cameraLensRect.right - scannerBoxAngleLength, cameraLensRect.top);
            scannerBoxAnglePath.lineTo(cameraLensRect.right, cameraLensRect.top);
            scannerBoxAnglePath.lineTo(cameraLensRect.right, cameraLensRect.top + scannerBoxAngleLength);
            canvas.drawPath(scannerBoxAnglePath, paint);
            //右下角
            scannerBoxAnglePath.reset();
            scannerBoxAnglePath.moveTo(cameraLensRect.right, cameraLensRect.bottom - scannerBoxAngleLength);
            scannerBoxAnglePath.lineTo(cameraLensRect.right, cameraLensRect.bottom);
            scannerBoxAnglePath.lineTo(cameraLensRect.right - scannerBoxAngleLength, cameraLensRect.bottom);
            canvas.drawPath(scannerBoxAnglePath, paint);
            //左下角
            scannerBoxAnglePath.reset();
            scannerBoxAnglePath.moveTo(cameraLensRect.left + scannerBoxAngleLength, cameraLensRect.bottom);
            scannerBoxAnglePath.lineTo(cameraLensRect.left, cameraLensRect.bottom);
            scannerBoxAnglePath.lineTo(cameraLensRect.left, cameraLensRect.bottom - scannerBoxAngleLength);
            canvas.drawPath(scannerBoxAnglePath, paint);
        }

        //提示文字
        if (text != null && text.trim().length() > 0) {
            canvas.save();
            if (textStaticLayout == null) {
                int textWidth = textSingleLine ? getWidth() : cameraLensRect.width();
                textWidth = textWidth - textLeftMargin - textRightMargin;
                textStaticLayout = new StaticLayout(text, textPaint, textWidth, StaticLayout.Alignment.ALIGN_CENTER, 1.0f, 0, true);
            }
            translateX = textSingleLine ? 0 : cameraLensRect.left;
            translateX = translateX + textLeftMargin;
            translateY = textLocation == LOCATION_BELOW_CAMERA_LENS ? cameraLensRect.bottom + textVerticalMargin : cameraLensRect.top - textVerticalMargin - textStaticLayout.getHeight();
            canvas.translate(translateX, translateY);
            textStaticLayout.draw(canvas);
            canvas.translate(-translateX, -translateY);
            canvas.restore();
        }
    }

    /**
     * The first way to draw square mask.
     * Only the square mask supported in this way.
     *
     * @param canvas canvas
     */
    private void drawMask(Canvas canvas) {
        paint.setColor(maskColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), topMargin, paint);
        canvas.drawRect(0, cameraLensRect.bottom, getWidth(), getHeight(), paint);
        canvas.drawRect(0, topMargin, cameraLensRect.left, cameraLensRect.bottom, paint);
        canvas.drawRect(cameraLensRect.right, topMargin, getWidth(), cameraLensRect.bottom, paint);
    }

    public final static int MASK_SHAPE_SQUARE = 0;
    public final static int MASK_SHAPE_CIRCULAR = 1;
    @IntDef({MASK_SHAPE_SQUARE, MASK_SHAPE_CIRCULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MaskShape {
    }
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private int maskShape;

    public int getMaskShape() {
        return maskShape;
    }

    public void setMaskShape(@MaskShape int maskShape) {
        this.maskShape = maskShape;
        invalidate();
    }

    /**
     * The second way to draw mask. In this way, there are two different shapes.
     * Square: {@link #MASK_SHAPE_SQUARE}、Circular: {@link #MASK_SHAPE_CIRCULAR}.
     *
     * @param canvas  canvas
     * @param maskShape mask shape. One of {@link #MASK_SHAPE_SQUARE}、{@link #MASK_SHAPE_CIRCULAR}.
     */
    private void drawMask(Canvas canvas, int maskShape) {
        //满屏幕bitmap
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bitmap);
        paint.setColor(maskColor);
        paint.setStyle(Paint.Style.FILL);
        mCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setXfermode(xfermode);
        switch (maskShape) {
            case MASK_SHAPE_SQUARE:
                mCanvas.drawRect(cameraLensRect, paint);
                break;
            case MASK_SHAPE_CIRCULAR:
                float radius = cameraLensRect.height() / 2.0f;
                mCanvas.drawCircle(getWidth() / 2.0f, cameraLensRect.top + radius, radius, paint);
                break;
        }
        paint.setXfermode(null);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
