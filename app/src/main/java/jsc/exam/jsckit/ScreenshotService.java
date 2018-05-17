package jsc.exam.jsckit;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotService extends Service {

    public static final String ACTION_INIT = "init";
    public static final String ACTION_SCREEN_RECORD = "screen_record";
    public static final String ACTION_SCREEN_SHOT = "screen_shot";

    int resultCode = 0;
    Intent data = null;
    private ImageReader mImageReader = null;
    private MediaProjection mMediaProjection = null;
    private VirtualDisplay mVirtualDisplay = null;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_INIT.equals(action)) {

        } else if (ACTION_SCREEN_RECORD.equals(action)){

        } else if (ACTION_SCREEN_SHOT.equals(action)){
            resultCode = intent.getIntExtra("result_code", 0);
            data = intent.getSelector();
            setUpMediaProjection(resultCode, data);
            startScreenCapture();
            screenshot();
            stopScreenCapture();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tearDownMediaProjection();
    }

    private void init() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mImageReader = ImageReader.newInstance(displayMetrics.widthPixels, displayMetrics.heightPixels, 0x1, 2);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection(int resultCode, Intent data) {
        if (mMediaProjection != null)
            return;

        MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startScreenCapture() {
        if (mMediaProjection == null)
            return;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(
                "ScreenCapture",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    private void stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    private void screenshot(){
        if (mMediaProjection == null)
            return;

        Image image = mImageReader.acquireLatestImage();
        if (image == null)
            return;

        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap mBitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(buffer);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height);
        image.close();
        saveBitmapToFile(mBitmap);
    }

    private void saveBitmapToFile(Bitmap bitmap){
        long startTime = System.currentTimeMillis();
        File directory = new File(Environment.getExternalStorageDirectory(), "Screenshots");
        if (!directory.exists())
            directory.mkdirs();
        String name = "shot" + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + "." + Bitmap.CompressFormat.PNG.toString();
        File file = new File(directory, name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(getClass().getSimpleName(), "saveBitmapToFile: the time for saving screenshot is " + (System.currentTimeMillis() - startTime));
    }
}
