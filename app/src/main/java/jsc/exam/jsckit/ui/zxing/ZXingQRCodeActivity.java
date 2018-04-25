package jsc.exam.jsckit.ui.zxing;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.ABaseActivity;
import jsc.kit.utils.MyPermissionChecker;
import jsc.lib.zxinglibrary.zxing.QRCodeEncoder;
import jsc.lib.zxinglibrary.zxing.ui.ZXingFragment;

public class ZXingQRCodeActivity extends ABaseActivity {

    final String apkUrl = "https://github.com/JustinRoom/JSCKit/blob/master/capture/JSCKitDemo.apk?raw=true";
    ImageView ivQRCode;
    TextView tvScanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_xing_qr_code);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        ivQRCode = findViewById(R.id.iv_qr_code);
        tvScanResult = findViewById(R.id.tv_scan_result);
        showQRCode();
    }

    private void showQRCode() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] colors = {Color.GREEN, 0x99FF4081, Color.BLUE, Color.CYAN};
                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(apkUrl, size, size, colors);
                ivQRCode.setImageBitmap(bitmap);
            }
        }, 300);
    }

    public void widgetClick(View v) {
        //打开相机权限
        checkPermissions(0x100, new MyPermissionChecker.OnCheckListener() {
            @Override
            public void onAllGranted(int requestCode) {
                toScannerActivity();
            }

            @Override
            public void onGranted(int requestCode, @NonNull List<String> grantedPermissions) {

            }

            @Override
            public void onDenied(int requestCode, @NonNull List<String> deniedPermissions) {

            }

            @Override
            public void onShouldShowSettingTips(@NonNull List<String> shouldShowPermissions) {
                String message = "当前应用需要以下权限:\n\n" + getAllPermissionDes(shouldShowPermissions);
                showPermissionRationaleDialog("温馨提示", message, "设置", "知道了");
            }

            @Override
            public void onFinally(int requestCode) {
                removePermissionChecker();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private void toScannerActivity() {
        Intent intent = new Intent(this, ZXingScannerActivity.class);
            Uri uri = Uri.parse("zxinglibrary://" + getPackageName()).buildUpon()
                    .appendPath("scanner")
                    .appendQueryParameter(ZXingFragment.SHOW_FLASH_LIGHT, "true") //是否显示灯光按钮
                    .build();
            intent.setData(uri);
        startActivityForResult(intent, 0x666);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 0x666) {
            String result = data.getStringExtra("result");
            tvScanResult.setText("扫描结果：\n" + result);
        }
    }
}
