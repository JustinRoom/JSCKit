package jsc.kit.archeaderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import jsc.kit.R;

public class PictureArcHeaderView extends BaseArcHeaderView {
    private Bitmap bitmap;

    public PictureArcHeaderView(Context context) {
        super(context);
    }

    public PictureArcHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureArcHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, TypedArray a) {
        int drawableId = a.getResourceId(R.styleable.BaseArcHeaderView_ahv_src, -1);
        if (drawableId != -1)
            bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
    }

    @NonNull
    @Override
    protected Shader getShader() {
        if (bitmap == null)
            return new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(), 0xFFF2F2F2, 0xFFF2F2F2, Shader.TileMode.MIRROR);

        Bitmap tempBitmap;
        if (bitmap.getWidth() >= getWidth() && bitmap.getHeight() >= getHeight()){
            int x = (bitmap.getWidth() - getWidth()) / 2;
            int y = (bitmap.getHeight() - getHeight()) / 2;
            tempBitmap = Bitmap.createBitmap(bitmap, x, y, getWidth(), getHeight());
        } else {
            tempBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
        }

        return new BitmapShader(tempBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    public void setSrc(@DrawableRes int drawableId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        setSrc(bitmap);
    }

    public void setSrc(@NonNull Bitmap bitmap) {
        resetShader();
        this.bitmap = bitmap;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }
}