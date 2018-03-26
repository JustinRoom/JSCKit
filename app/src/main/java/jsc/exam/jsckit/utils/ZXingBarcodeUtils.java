package jsc.exam.jsckit.utils;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/3/26.
 *
 * @author jsc
 */

public class ZXingBarcodeUtils {
    private final static String TAG = "ZXingBarcodeUtils";

    public static Bitmap createQRCodeBitmap(String str, int width, int height, @ColorInt int foregroundColor, int backgroundColor) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        Map<EncodeHintType, String> map = new HashMap<>();
        map.put(EncodeHintType.MARGIN, "0");
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, map);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? foregroundColor : backgroundColor;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (WriterException | IllegalArgumentException e) {
            Log.i(TAG, "createQRCodeBitmap: " + e.getLocalizedMessage());
        }
        return bitmap;
    }
}
