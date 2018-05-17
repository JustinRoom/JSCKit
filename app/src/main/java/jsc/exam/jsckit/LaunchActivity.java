package jsc.exam.jsckit;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jsc.exam.jsckit.ui.ABaseActivity;
import jsc.exam.jsckit.ui.MainActivity;
import jsc.kit.entity.DownloadEntity;
import jsc.kit.utils.SharePreferencesUtils;

public class LaunchActivity extends ABaseActivity {

    AppCompatImageView imageView;
    String url = "https://raw.githubusercontent.com/JustinRoom/JSCKit/master/app/src/main/assets/img/6.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        imageView = new AppCompatImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        File directory = new File(getExternalCacheDir(), Environment.DIRECTORY_PICTURES);
        if (!directory.exists())
            directory.mkdirs();
        String[] splits = url.split(File.separator);
        String fileName = splits[splits.length - 1];
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
            downloadFile(entity);
        }
    }

    /**
     * 跳转到主界面。如果业务需要，可跳转到引导页。
     */
    private void toGuideIfNecessary(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                downloadPictureIfNecessary();
                toGuideIfNecessary();
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
