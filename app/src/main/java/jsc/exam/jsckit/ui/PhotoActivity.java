package jsc.exam.jsckit.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.transition.Transition;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import jsc.exam.jsckit.MyApplication;
import jsc.exam.jsckit.R;
import jsc.kit.component.baseui.photo.BasePhotoActivity;
import jsc.kit.component.baseui.transition.TransitionProvider;
import jsc.kit.component.baseui.photo.CropConfig;
import jsc.kit.component.utils.CompatResourceUtils;
import jsc.kit.component.utils.WindowUtils;

public class PhotoActivity extends BasePhotoActivity {

    final String TAG = getClass().getSimpleName();
    ImageView ivPhoto;
    boolean needCrop = false;
    public boolean enableAdvertisement = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        ivPhoto = findViewById(R.id.iv_photo);
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null)
            return;

        int padding = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_12);
        FrameLayout customView = new FrameLayout(this);
//        customView.setPadding(padding, 0, padding, 0);
        ActionBar.LayoutParams barParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowUtils.getActionBarSize(this));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customView, barParams);
        //添加标题
        TextView tvTitle = new TextView(this);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setText(getClass().getSimpleName().replace("Activity", ""));
        customView.addView(tvTitle, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //添加返回按钮
        ImageView ivBack = new ImageView(this);
        ivBack.setPadding(padding / 2, 0, padding / 2, 0);
        ivBack.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivBack.setImageResource(R.drawable.ic_chevron_left_white_24dp);
        ivBack.setBackground(WindowUtils.getSelectableItemBackgroundBorderless(this));
        customView.addView(ivBack, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //添加menu菜单
        ActionMenuView actionMenuView = new ActionMenuView(this);
        FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        customView.addView(actionMenuView, menuParams);
    }

    @Override
    public Transition createEnterTransition() {
        return TransitionProvider.createTransition(getIntent().getStringExtra("transition"), 300L);
    }

    public void widgetClick(View view) {
        if (!permissionChecker.checkPermissions(this, 0, null, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA))
            return;

        //禁止前台广告
        enableAdvertisement = false;
        switch (view.getId()) {
            case R.id.btn_choose:
                needCrop = false;
                openAlbum();
                break;
            case R.id.btn_choose_crop:
                needCrop = true;
                openAlbum();
                break;
            case R.id.btn_take_photo:
                needCrop = false;
                File targetDirFile1 = new File(Environment.getExternalStorageDirectory(), "photoTake");
                openCamera(targetDirFile1);
                break;
            case R.id.btn_take_crop_photo:
                needCrop = true;
                File targetDirFile2 = new File(Environment.getExternalStorageDirectory(), "photoTake");
                openCamera(targetDirFile2);
                break;
        }
    }

    @Override
    public void onPickPhotoResult(Uri uri) {
        String imagePath = getRealImagePathFromUri(uri);
        Log.i(TAG, "onPickPhotoResult: " + imagePath);
        if (imagePath == null || imagePath.trim().length() == 0) {
            showToast("选取图片失败！");
            return;
        }

        if (needCrop) {
            //禁止前台广告
            enableAdvertisement = false;

            File file = new File(Environment.getExternalStorageDirectory(), "photoCrop");
            CropConfig config = new CropConfig()
                    .setDirectory(file);
            cropPhoto(uri, config);
        } else {
            showImage(imagePath);
        }
    }

    @Override
    public void onTakePhotoResult(Uri uri, @NonNull File tempFile) {
        String imagePath = getRealImagePathFromUri(uri);
        Log.i(TAG, "onPickPhotoResult: " + imagePath);
        Log.i(TAG, "onPickPhotoResult: " + tempFile.getAbsolutePath());
        if (needCrop) {
            //禁止前台广告
            enableAdvertisement = false;

            File file = new File(Environment.getExternalStorageDirectory(), "photoCrop");
            CropConfig config = new CropConfig()
                    .setAspectX(4)
                    .setAspectY(3)
                    .setOutputX(480)
                    .setOutputY(560)
                    .setDirectory(file);
            cropPhoto(uri, config);
        } else {
            showImage(tempFile);
        }
    }

    @Override
    public void onCropPhotoResult(@Nullable Uri uri, @Nullable File tempFile) {
        if (uri != null){
            String imagePath = getRealImagePathFromUri(uri);
            Log.i(TAG, "onCropPhotoResult: " + imagePath);
        }
        if (tempFile != null){
            Log.i(TAG, "onCropPhotoResult: " + tempFile.getAbsolutePath());
            showImage(tempFile);
        }
    }

    private void showImage(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        ivPhoto.setImageBitmap(bitmap);
        Log.i(TAG, "showImage: " + options.outMimeType);
    }

    private void showImage(File file) {
        showImage(file.getAbsolutePath());
    }

}
