package jsc.exam.jsckit;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import jsc.exam.jsckit.utils.ZXingBarcodeUtils;

public class AboutActivity extends AppCompatActivity {

    final String apkUrl = "https://github.com/JustinRoom/JSCKit/blob/master/capture/JSCKitDemo.apk?raw=true";
    TextView tvVersion;
    ImageView ivQRCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvVersion = findViewById(R.id.tv_version);
        ivQRCode = findViewById(R.id.iv_qr_code);
        showVersion();
        showQRCode();
    }

    private void showVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            int curVersionCode = info.versionCode;
            tvVersion.setText("version:" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showQRCode() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
                Bitmap bitmap = ZXingBarcodeUtils.createQRCodeBitmap(apkUrl, size, size, Color.GREEN, Color.WHITE);
                ivQRCode.setImageBitmap(bitmap);
            }
        }, 1000);

    }
}
