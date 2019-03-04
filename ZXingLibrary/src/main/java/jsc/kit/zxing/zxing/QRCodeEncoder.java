package jsc.kit.zxing.zxing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/4/8 下午11:22
 * 描述:创建二维码图片
 */
public class QRCodeEncoder {
    public static final Map<EncodeHintType, Object> HINTS = new EnumMap<>(EncodeHintType.class);

    static {
        HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        HINTS.put(EncodeHintType.MARGIN, 0);
    }

    private QRCodeEncoder() {
    }

    /**
     * 同步创建黑色前景色、白色背景色的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content 要生成的二维码图片内容
     * @param size    图片宽高，单位为px
     * @return bitmap
     */
    public static Bitmap syncEncodeQRCode(String content, int size) {
        return syncEncodeQRCode(content, size, Color.BLACK, Color.WHITE, null);
    }

    /**
     * 同步创建指定前景色、白色背景色的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content         要生成的二维码图片内容
     * @param size            图片宽高，单位为px
     * @param foregroundColor 二维码图片的前景色
     * @return bitmap
     */
    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor) {
        return syncEncodeQRCode(content, size, foregroundColor, Color.WHITE, null);
    }

    /**
     * 同步创建指定前景色、白色背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content         要生成的二维码图片内容
     * @param size            图片宽高，单位为px
     * @param foregroundColor 二维码图片的前景色
     * @param logo            二维码图片的logo
     * @return bitmap
     */
    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, Bitmap logo) {
        return syncEncodeQRCode(content, size, foregroundColor, Color.WHITE, logo);
    }

    /**
     * 同步创建指定前景色、指定背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content         要生成的二维码图片内容
     * @param size            图片宽高，单位为px
     * @param foregroundColor 二维码图片的前景色
     * @param backgroundColor 二维码图片的背景色
     * @param logo            二维码图片的logo
     * @return bitmap
     */
    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, int backgroundColor, Bitmap logo) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, HINTS);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * size + x] = foregroundColor;
                    } else {
                        pixels[y * size + x] = backgroundColor;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return addLogoToQRCode(bitmap, logo);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 添加logo到二维码图片上
     *
     * @param src  source
     * @param logo logo
     * @return bitmap
     */
    private static Bitmap addLogoToQRCode(Bitmap src, Bitmap logo) {
        if (src == null || logo == null) {
            return src;
        }

        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 同步创建指定前景色、指定背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content          text
     * @param width            width
     * @param height           height
     * @param foregroundColors 上下左右四部分前景色，背景默认透明。
     * @return bitmap
     */
    public static Bitmap syncEncodeQRCode(String content, int width, int height, int[] foregroundColors) {
        int[] newColors = new int[4];
        for (int i = 0; i < 4; i++) {
            newColors[i] = Color.BLACK;
        }
        if (foregroundColors != null)
            System.arraycopy(foregroundColors, 0, newColors, 0, foregroundColors.length);

        try {
            BitMatrix result = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
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

            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException | IllegalArgumentException e) {
            return null;
        }
    }

    public static Bitmap encodeQRCode(String content, int width, int height, Bitmap shape) {
        try {
            int sw = shape.getWidth();
            int sh = shape.getHeight();
            int[] sPixels = new int[sw * sh];
            shape.getPixels(sPixels, 0, sw, 0, 0, sw, sh);


            BitMatrix result = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
            int rows = result.getHeight();//行
            int columns = result.getWidth();//列

            int startRowIndex = (rows - sh) / 2 - 1;
            int startColumnIndex = (columns - sw) / 2 - 1;
            int endRowIndex = startRowIndex + sh;
            int endColumnIndex = startColumnIndex + sw;
            Rect rect = new Rect(startColumnIndex, startRowIndex, endColumnIndex, endRowIndex);

            int[] pixels = new int[rows * columns];
            for (int i = 0; i < rows; i++) {
                int offset = i * columns;
                int shapeOffset = (i - startRowIndex) * sw;
                for (int j = 0; j < columns; j++) {
                    int index = offset + j;
                    int shapeIndex = shapeOffset + (j - startColumnIndex);
                    if (result.get(i, j)) {
                        if (rect.contains(i, j)) {
                            int color = sPixels[shapeIndex];
                            if (color == Color.TRANSPARENT
                                    || color == Color.WHITE)
                                color = Color.BLACK;
                            pixels[index] = color;
                        } else {
                            pixels[index] = Color.BLACK;
                        }
                    } else {
                        if (rect.contains(i, j)) {
                            int color = sPixels[shapeIndex];
                            if (color == Color.WHITE)
                                color = Color.TRANSPARENT;
                            pixels[index] = color;
                        } else {
                            pixels[index] = Color.TRANSPARENT;
                        }
//                        pixels[index] = Color.TRANSPARENT;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(columns, columns, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, columns, 0, 0, columns, columns);
            return bitmap;
        } catch (WriterException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean contains(Rect rect, int x, int y) {
        return (x >= rect.left && x <= rect.right) && (y >= rect.top && y <= rect.bottom);
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