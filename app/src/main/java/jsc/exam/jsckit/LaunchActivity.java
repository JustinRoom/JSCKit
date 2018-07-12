package jsc.exam.jsckit;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jsc.exam.jsckit.ui.BaseActivity;
import jsc.exam.jsckit.ui.MainActivity;
import jsc.kit.component.baseui.download.DownloadEntity;
import jsc.kit.component.baseui.permission.PermissionChecker;
import jsc.kit.component.utils.SharePreferencesUtils;

public class LaunchActivity extends BaseActivity {

    AppCompatImageView imageView;
    String url = "https://raw.githubusercontent.com/JustinRoom/JSCKit/master/app/src/main/assets/img/6.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = new AppCompatImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadPicture();
        showAnim();
    }

    private void loadPicture(){
        InputStream is = null;
        try {
            is = getAssets().open("img/1.jpg");
            Drawable drawable = Drawable.createFromStream(is, null);
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 后台下载广告图片。下载图片存放在当前应用的cache文件夹下，不需要SD的读写权限
     */
    private void downloadPictureIfNecessary(){
        File directory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
        String[] splits = url.split(File.separator);
        String fileName = "advertisement" + File.separator + splits[splits.length - 1];
        File pic = new File(directory, fileName);
        SharePreferencesUtils.getInstance().saveString(Configration.SP_ADVERTISEMENT_PICTURE, pic.getPath());
        if (!pic.exists()){
            DownloadEntity entity = new DownloadEntity();
            entity.setUrl(url);
            entity.setDestinationDirectory(directory);
            entity.setSubPath(fileName);
            entity.setTitle("Download advertisement picture");
            entity.setDesc("");
            entity.setMimeType("image/jpeg");
            fileDownloader.downloadFile(entity);
        }
    }

    private void checkPermissionBeforeDownloadPicture() {
        permissionChecker.checkPermissions(this, 0, new PermissionChecker.OnPermissionCheckListener() {
            @Override
            public void onResult(int requestCode, boolean isAllGranted, @NonNull List<String> grantedPermissions, @Nullable List<String> deniedPermissions, @Nullable List<String> shouldShowPermissions) {
                if (isAllGranted) {
                    downloadPictureIfNecessary();
                    toGuideIfNecessary();
                    return;
                }

                if (shouldShowPermissions != null && shouldShowPermissions.size() > 0) {
                    String message = "当前应用需要以下权限:\n\n" + PermissionChecker.getAllPermissionDes(getBaseContext(), shouldShowPermissions);
                    showPermissionRationaleDialog("温馨提示", message, "设置", "知道了");
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 跳转到主界面。如果业务需要，可跳转到引导页。
     */
    private void toGuideIfNecessary(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showAnim(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, View.ALPHA, 0.4f, 1.0f)
                .setDuration(3000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkPermissionBeforeDownloadPicture();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
