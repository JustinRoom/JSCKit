package jsc.kit.baseui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
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
 * a frame of choosing photo、taking photo、cropping photo
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
    private File takePhotoTempFile;// 拍照产生的临时文件
    private File cropPhotoTempFile;// 裁剪产生的临时文件

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
            photoName = getDefaultTakePhotoFileName(Bitmap.CompressFormat.JPEG);
        takePhotoTempFile = new File(directory, photoName);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProviderCompat.getUriForFile(this, takePhotoTempFile));
        startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * create default taking photo file name like "IMG_20180426_140554.JPEG" with current system time.
     *
     * @param outputFormat
     * @return
     */
    public String getDefaultTakePhotoFileName(@NonNull Bitmap.CompressFormat outputFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return "IMG_" + dateFormat.format(new Date()) + "." + outputFormat.toString();
    }

    /**
     * create default cropping photo file name like "CROP_IMG_20180426_140554.JPEG" with current system time.
     *
     * @param outputFormat
     * @return
     */
    public String getDefaultCropPhotoFileName(@NonNull Bitmap.CompressFormat outputFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return "CROP_IMG_" + dateFormat.format(new Date()) + "." + outputFormat.toString();
    }

    /**
     * @param file
     * @param config
     * @see #cropPhoto(Uri, CropConfig)
     */
    public void cropPhoto(File file, @NonNull CropConfig config) {
        if (file == null || !file.exists())
            return;

        cropPhoto(FileProviderCompat.getUriForFile(this, file), config);
    }

    /**
     * open system crop photo feature.
     * <br/>1、如果有设置aspectX、aspectY或者outputX、outputY，那么它就是按比例裁剪；否则是自由裁剪
     * <br/>2、比例优先。举个栗子：如果有设置aspectX=4、aspectY=3、outputX=480、outputY=560，那么它裁剪时真正的outputX=480、outputY=360。
     * <br/>Caller must ensure {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
     *
     * @param uri
     * @param config
     */
    public void cropPhoto(Uri uri, @NonNull CropConfig config) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        FileProviderCompat.setDataAndType(intent, uri, "image/*", true);
        intent.putExtra(CropConfig.EXTRA_CROP, String.valueOf(config.isCrop()));
        int outputX = config.getOutputX();
        int outputY = config.getOutputY();
        int aspectX = config.getAspectX();
        int aspectY = config.getAspectY();
        boolean isOutputSet = outputX > 0 && outputY > 0;
        boolean isAspectSet = aspectX > 0 && aspectY > 0;

        if (isAspectSet) {
            intent.putExtra(CropConfig.EXTRA_ASPECT_X, aspectX);
            intent.putExtra(CropConfig.EXTRA_ASPECT_Y, aspectY);
            //如果有设置outputX、outputY，则根据aspectX、aspectY计算出合适的outputX、outputY
            if (isOutputSet) {
                if (aspectX == aspectY) {
                    outputX = outputY = Math.min(outputX, outputY);
                } else {
                    double ratioX = outputX * 1.0 / aspectX;
                    double ratioY = outputY * 1.0 / aspectY;
                    if (ratioX > ratioY) {//以outputY为标准
                        int avg = outputY / aspectY;
                        outputX = avg * aspectX;
                        outputY = avg * aspectY;
                    } else if (ratioX < ratioY) {//以outputX为标准
                        int avg = outputX / aspectX;
                        outputX = avg * aspectX;
                        outputY = avg * aspectY;
                    }
                }
                config.setOutputX(outputX);
                config.setOutputY(outputY);
            }
        }
        if (isOutputSet) {
            intent.putExtra(CropConfig.EXTRA_OUTPUT_X, outputX);
            intent.putExtra(CropConfig.EXTRA_OUTPUT_Y, outputY);
        }
        intent.putExtra(CropConfig.EXTRA_SCALE, config.isScale());
        intent.putExtra(CropConfig.EXTRA_CIRCLE_CROP, String.valueOf(config.isCircleCrop()));

        //如果没有设置文件夹，裁剪后则保存在sdk根目录的Pictures文件夹下面
        File directory = config.getDirectory();
        if (directory == null){
            directory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
            config.setDirectory(directory);
        }

        //如果没有设置裁剪后的图片名称，则更加当前系统时间设置默认的图片名称
        String photoPathName = config.getPhotoPathName();
        if (photoPathName == null || photoPathName.trim().length() == 0){
            photoPathName = getDefaultCropPhotoFileName(config.getOutputFormat());
            config.setPhotoPathName(photoPathName);
        }
        cropPhotoTempFile = new File(directory, photoPathName);
        String cropPhotoTempFilePath = cropPhotoTempFile.getPath();
        //如果保存图片的文件夹还没有创建，则创建文件夹
        File dir = new File(cropPhotoTempFilePath.substring(0, cropPhotoTempFilePath.lastIndexOf(File.separator)));
        if (!dir.exists())
            dir.mkdirs();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProviderCompat.getUriForFile(this, cropPhotoTempFile));
        intent.putExtra(CropConfig.EXTRA_RETURN_DATA, config.isReturnData());
        intent.putExtra(CropConfig.EXTRA_NO_FACE_DETECTION, config.isNoFaceDetection());
        intent.putExtra(CropConfig.EXTRA_OUTPUT_FORMAT, config.getOutputFormat().name());
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
