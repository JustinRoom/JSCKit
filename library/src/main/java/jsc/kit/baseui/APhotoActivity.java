package jsc.kit.baseui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jsc.kit.entity.CropConfig;
import jsc.kit.utils.FileProviderCompat;
import jsc.kit.utils.UriUtils;

/**
 * <p>
 *     a frame of choosing photo、taking photo、cropping photo
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public abstract class APhotoActivity extends APermissionCheckActivity {

    public final static int REQUEST_CODE_PIC_PHOTO = 0x2000;
    public final static int REQUEST_CODE_TAKE_PHOTO = 0x2001;
    public final static int REQUEST_CODE_PHOTO_CROP = 0x2002;
    private File takePhotoTempFile;// 拍照产生的临时图片
    private File cropPhotoTempFile;// 裁剪产生的临时图片

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_PIC_PHOTO://手机相册
                onPickPhotoResult(data.getData());
                break;
            case REQUEST_CODE_TAKE_PHOTO://拍照
                onTakePhotoResult(FileProviderCompat.getUriForFile(this, takePhotoTempFile), takePhotoTempFile);
                break;
            case REQUEST_CODE_PHOTO_CROP://裁剪
                onCropPhotoResult(data.getData(), cropPhotoTempFile);
                break;
        }
    }

    /**
     * open system album to choose a picture.
     * <br/>Caller must ensure {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} permission.
     */
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PIC_PHOTO);
    }

    /**
     * @param directory
     * @see #openCamera(File, String)
     */
    public void openCamera(@NonNull File directory) {
        openCamera(directory, null);
    }

    /**
     * open system camera to take a photo.
     * <br/>Caller must ensure {@link android.Manifest.permission#CAMERA} permission.
     *
     * @param directory the directory for saving photo
     * @param photoName photo file name, not including directory path
     */
    public void openCamera(@NonNull File directory, String photoName) {
        if (!directory.exists())
            directory.mkdirs();
        if (photoName == null || photoName.trim().length() == 0)
            photoName = getDefaultTakePhotoFileName();
        takePhotoTempFile = new File(directory, photoName);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProviderCompat.getUriForFile(this, takePhotoTempFile));
        startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * create default taking photo file name like "IMG_20180426_140554.JPEG" with current system time.
     *
     * @return
     */
    public String getDefaultTakePhotoFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return "IMG_" + dateFormat.format(new Date()) + "." + Bitmap.CompressFormat.JPEG;
    }

    /**
     * create default cropping photo file name like "CROP_IMG_20180426_140554.JPEG" with current system time.
     *
     * @return
     */
    public String getDefaultCropPhotoFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return "CROP_IMG_" + dateFormat.format(new Date()) + "." + Bitmap.CompressFormat.JPEG;
    }

    /**
     * @param file
     * @param config
     * @see #cropPhoto(Uri, CropConfig)
     */
    public void cropPhoto(File file, CropConfig config) {
        if (file == null || !file.exists())
            return;

        cropPhoto(FileProviderCompat.getUriForFile(this, file), config);
    }

    /**
     * open system crop photo feature.
     * <br/>Caller must ensure {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
     *
     * @param uri
     * @param config
     */
    public void cropPhoto(Uri uri, CropConfig config) {
        if (config == null)
            config = new CropConfig();

        Intent intent = new Intent("com.android.camera.action.CROP");
        FileProviderCompat.setDataAndType(intent, uri, "image/*", true);
        intent.putExtra(CropConfig.EXTRA_CROP, config.isCrop());
        if (config.getCropType() == CropConfig.CROP_TYPE_SIZE) {
            intent.putExtra(CropConfig.EXTRA_OUTPUT_X, config.getOutputX());
            intent.putExtra(CropConfig.EXTRA_OUTPUT_Y, config.getOutputY());
        } else {
            intent.putExtra(CropConfig.EXTRA_ASPECT_X, config.getAspectX());
            intent.putExtra(CropConfig.EXTRA_ASPECT_Y, config.getAspectY());
        }

        cropPhotoTempFile = null;
        File directory = config.getDirectory();
        if (directory != null) {
            String photoName = config.getPhotoName();
            if (photoName == null || photoName.trim().length() == 0) {
                photoName = getDefaultCropPhotoFileName();
                config.setPhotoName(photoName);
            }
            if (!directory.exists())
                directory.mkdirs();

            cropPhotoTempFile = new File(directory, photoName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProviderCompat.getUriForFile(this, cropPhotoTempFile));
        }
        intent.putExtra(CropConfig.EXTRA_RETURN_DATA, config.isReturnData());
        intent.putExtra(CropConfig.EXTRA_RETURN_DATA, true);
        intent.putExtra(CropConfig.EXTRA_NO_FACE_DETECTION, config.isNoFaceDetection());
        startActivityForResult(intent, REQUEST_CODE_PHOTO_CROP);
    }

    /**
     * @param uri
     */
    public abstract void onPickPhotoResult(Uri uri);

    /**
     * @param uri
     * @param tempFile
     */
    public abstract void onTakePhotoResult(Uri uri, @NonNull File tempFile);

    /**
     * @param uri
     * @param tempFile
     */
    public abstract void onCropPhotoResult(@Nullable Uri uri, @Nullable File tempFile);

    /**
     * Get image path by uri.
     *
     * @param uri
     * @return
     */
    public String getRealImagePathFromUri(Uri uri) {
        return UriUtils.getImagePathByUri(this, uri);
    }
}
