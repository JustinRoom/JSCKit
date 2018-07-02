package jsc.kit.component.baseui.photo;

import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public class CropConfig {

    public static final String EXTRA_CROP = "crop";
    public static final String EXTRA_ASPECT_X = "aspectX";
    public static final String EXTRA_ASPECT_Y = "aspectY";
    public static final String EXTRA_OUTPUT_X = "outputX";
    public static final String EXTRA_OUTPUT_Y = "outputY";
    public static final String EXTRA_SCALE = "scale";
    public static final String EXTRA_CIRCLE_CROP = "circleCrop";
    public static final String EXTRA_RETURN_DATA = "return-data";
    public static final String EXTRA_NO_FACE_DETECTION = "noFaceDetection";
    public static final String EXTRA_OUTPUT_FORMAT = "outputFormat";

    private boolean crop;//发送裁剪信号(不一定有效)
    private int aspectX;//X方向上的比例
    private int aspectY;//Y方向上的比例
    private int outputX;//裁剪后的图片的宽。与裁剪框的宽没有半毛钱关系，不要搞混了！
    private int outputY;//裁剪后的图片的高。与裁剪框的高没有半毛钱关系，不要搞混了！
    private boolean scale;//是否保留比例(不一定有效)
    private boolean circleCrop;//是否是圆形裁剪区域(不一定有效)
    private boolean returnData;//是否将数据保留在Bitmap中返回
    private boolean noFaceDetection;//是否取消人脸识别

    private File directory;//裁剪后的图片的保存目录
    private String photoPathName;//裁剪后的图片名称，可以包含目录路径
    private Bitmap.CompressFormat outputFormat;//裁剪后的图片格式，系统默认为jpeg格式(不一定有效)。如果文件格是png，但图片格式可能是jpeg。

    public CropConfig() {
        crop = true;
        scale = true;
        noFaceDetection = true;
        outputFormat = Bitmap.CompressFormat.JPEG;
    }

    public boolean isCrop() {
        return crop;
    }

    public CropConfig setCrop(boolean crop) {
        this.crop = crop;
        return this;
    }

    public int getAspectX() {
        return aspectX;
    }

    public CropConfig setAspectX(@IntRange(from = 1) int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    public int getAspectY() {
        return aspectY;
    }

    public CropConfig setAspectY(@IntRange(from = 1) int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    public int getOutputX() {
        return outputX;
    }

    public CropConfig setOutputX(@IntRange(from = 100) int outputX) {
        this.outputX = outputX;
        return this;
    }

    public int getOutputY() {
        return outputY;
    }

    public CropConfig setOutputY(@IntRange(from = 100) int outputY) {
        this.outputY = outputY;
        return this;
    }

    public boolean isScale() {
        return scale;
    }

    public CropConfig setScale(boolean scale) {
        this.scale = scale;
        return this;
    }

    public boolean isCircleCrop() {
        return circleCrop;
    }

    public CropConfig setCircleCrop(boolean circleCrop) {
        this.circleCrop = circleCrop;
        return this;
    }

    public boolean isReturnData() {
        return returnData;
    }

    public CropConfig setReturnData(boolean returnData) {
        this.returnData = returnData;
        return this;
    }

    public boolean isNoFaceDetection() {
        return noFaceDetection;
    }

    public CropConfig setNoFaceDetection(boolean noFaceDetection) {
        this.noFaceDetection = noFaceDetection;
        return this;
    }

    public Bitmap.CompressFormat getOutputFormat() {
        return outputFormat;
    }

    public CropConfig setOutputFormat(@NonNull Bitmap.CompressFormat outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public File getDirectory() {
        return directory;
    }

    public CropConfig setDirectory(@NonNull File directory) {
        this.directory = directory;
        return this;
    }

    public String getPhotoPathName() {
        return photoPathName;
    }

    public CropConfig setPhotoPathName(String photoPathName) {
        this.photoPathName = photoPathName;
        return this;
    }
}
