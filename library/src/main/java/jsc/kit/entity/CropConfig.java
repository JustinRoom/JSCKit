package jsc.kit.entity;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CropConfig {

    public static final String EXTRA_CROP = "crop";
    public static final String EXTRA_ASPECT_X = "aspectX";
    public static final String EXTRA_ASPECT_Y = "aspectY";
    public static final String EXTRA_OUTPUT_X = "outputX";
    public static final String EXTRA_OUTPUT_Y = "outputY";
    public static final String EXTRA_RETURN_DATA = "return-data";
    public static final String EXTRA_NO_FACE_DETECTION = "noFaceDetection";

    /**
     * crop photo with fixed width and height.
     * @see #setOutputX(int)
     * @see #setOutputY(int)
     */
    public static final int CROP_TYPE_SIZE = 0;
    /**
     * crop photo with proportion.
     * @see #setAspectX(int)
     * @see #setAspectY(int)
     */
    public static final int CROP_TYPE_ASPECT = 1;
    @IntDef({CROP_TYPE_SIZE, CROP_TYPE_ASPECT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CropType {
    }

    private boolean crop;
    private int aspectX;
    private int aspectY;
    private int outputX;
    private int outputY;
    private boolean returnData;
    private boolean noFaceDetection;

    private int cropType;
    private File directory;
    private String photoName;

    public CropConfig() {
        crop = true;
        aspectX = 1;
        aspectY = 1;
        outputX = 300;
        outputY = 300;
        returnData = false;
        noFaceDetection = true;

        cropType = CROP_TYPE_SIZE;
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

    /**
     * The default value is 1.
     * @param aspectX
     * @return
     */
    public CropConfig setAspectX(@IntRange(from = 1)int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    public int getAspectY() {
        return aspectY;
    }

    /**
     * The default value is 1.
     * @param aspectY
     * @return
     */
    public CropConfig setAspectY(@IntRange(from = 1)int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    public int getOutputX() {
        return outputX;
    }

    /**
     * The default value is 300px.
     * @param outputX
     * @return
     */
    public CropConfig setOutputX(@IntRange(from = 100) int outputX) {
        this.outputX = outputX;
        return this;
    }

    public int getOutputY() {
        return outputY;
    }

    /**
     * The default value is 300px.
     * @param outputY
     * @return
     */
    public CropConfig setOutputY(@IntRange(from = 100) int outputY) {
        this.outputY = outputY;
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

    public int getCropType() {
        return cropType;
    }

    /**
     * one of types:{@link #CROP_TYPE_SIZE}、{@link #CROP_TYPE_ASPECT}
     * <br/> The default value is {@link #CROP_TYPE_SIZE}.
     * @param cropType
     * @return
     */
    public CropConfig setCropType(@CropType int cropType) {
        this.cropType = cropType;
        return this;
    }

    public File getDirectory() {
        return directory;
    }

    /**
     * Set the directory of saving crop photo, no including photo file name.
     * @param directory
     * @return
     */
    public CropConfig setDirectory(@NonNull File directory) {
        this.directory = directory;
        return this;
    }

    public String getPhotoName() {
        return photoName;
    }

    /**
     * Set the photo file name, not including it's directory path.
     *
     * @param photoName
     * @return
     */
    public CropConfig setPhotoName(String photoName) {
        this.photoName = photoName;
        return this;
    }
}
