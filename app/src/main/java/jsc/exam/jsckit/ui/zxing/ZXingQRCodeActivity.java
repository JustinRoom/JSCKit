package jsc.exam.jsckit.ui.zxing;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
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
                removePermissionChecker();
                toScannerActivity();
                showCustomToast("Request CAMERA permission successfully.");
            }

            @Override
            public void onGranted(int requestCode, @NonNull List<String> grantedPermissions) {

            }

            @Override
            public void onDenied(int requestCode, @NonNull List<String> deniedPermissions) {
                showCustomToast("You have denied permission:" + deniedPermissions.get(0));
            }

            @Override
            public void shouldShowDeniedPermission(@NonNull String permission) {
                showPermissionRationaleDialog(permission);
            }
        }, Manifest.permission.CAMERA);
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

    public void showPermissionRationaleDialog(@NonNull String permission) {
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("当前应用需要【" + getPermissionDes(permission) + "】权限。")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("知道了", null)
                .show();
    }

    private CharSequence getPermissionDes(String permission) {
        try {
            PermissionInfo info = getPackageManager().getPermissionInfo(permission, PackageManager.GET_META_DATA);
            return info.loadDescription(getPackageManager());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
