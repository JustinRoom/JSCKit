package jsc.exam.jsckit.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/3/26.
 *
 * @author jsc
 */

public class ZXingBarcodeUtils {
    private final static String TAG = "ZXingBarcodeUtils";

    public static Bitmap createQRCodeBitmap(String str, int width, int height, @ColorInt int visiblePointColor, @ColorInt int invisiblePointColor) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        Map<EncodeHintType, String> map = new HashMap<>();
        map.put(EncodeHintType.MARGIN, "0");
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M.name());
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, map);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? visiblePointColor : invisiblePointColor;
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (WriterException | IllegalArgumentException e) {
            Log.i(TAG, "createQRCodeBitmap: " + e.getLocalizedMessage());
        }
        return bitmap;
    }

    public static Bitmap createQRCodeBitmap(String str, int width, int height, int[] colors) {
        int[] newColors = new int[4];
        for (int i = 0; i < 4; i++) {
            newColors[i] = Color.BLACK;
        }
        if (colors != null)
            System.arraycopy(colors, 0, newColors, 0, colors.length);

        Bitmap bitmap = null;
        BitMatrix result = null;
        Map<EncodeHintType, String> map = new HashMap<>();
        map.put(EncodeHintType.MARGIN, "0");
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M.name());
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, map);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    if (result.get(x, y)) {
                        pixels[offset + x] = newColors[getAreaIndex(w, h, x, y)];
                    } else {
                        pixels[offset + x] = Color.TRANSPARENT;
                    }
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (WriterException | IllegalArgumentException e) {
            Log.i(TAG, "createQRCodeBitmap: " + e.getLocalizedMessage());
        }
        return bitmap;
    }

    private static int getAreaIndex(int w, int h, int x, int y) {
        int centerW = w / 2;
        int centerH = h / 2;
        int index = 0;
        if (x >= centerW)
            if (y >= centerH)
                index = 3;
            else
                index = 1;
        else if (y >= centerH)
            index = 2;
        else
            index = 0;
        return index;
    }
}
