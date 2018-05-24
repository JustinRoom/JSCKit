package jsc.exam.jsckit.ui.component;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jsc.kit.component.advertisement.AdvertisementView;
import jsc.kit.component.utils.SharePreferencesUtils;

public class AdvertisementViewActivity extends AppCompatActivity {

    private AdvertisementView advertisementView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        advertisementView = new AdvertisementView(this);
        setContentView(advertisementView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //获取ImageView, 设置显示图片
        //这样设计是为了方便调用者使用不同的图片加载框架。比如Picsso、Fresco、ImageLoader...
//        advertisementView.getImageView().setImageResource(R.drawable.header);
        String picPathName = SharePreferencesUtils.getInstance().getString("picPathName");
        loadPicture(new File(picPathName));

        advertisementView.init(5000, 1000, new AdvertisementView.OnComponentActionListener() {
            @Override
            public void onComponentAction(int actionCode, View view) {
                switch (actionCode) {//点击广告图片
                    case AdvertisementView.ACTION_CODE_PREVIEW:
                        String url = "http://www.baidu.com";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        break;
                    case AdvertisementView.ACTION_CODE_JUMP://点击跳过
                        break;
                    case AdvertisementView.ACTION_CODE_FINISH://倒计时结束
                        break;
                }
                //因为广告图片可能比较大，所以我们主动移除所有子view，加快GC回收
                advertisementView.removeAllViews();
                System.gc();
                finish();
            }
        });
    }

    private void loadPicture(File file){
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            Drawable drawable = Drawable.createFromStream(is, null);
            advertisementView.getImageView().setImageDrawable(drawable);
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

    @Override
    protected void onResume() {
        super.onResume();
        advertisementView.startCountDown();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        advertisementView.cancelCountDown();
        super.onPause();
    }
}
