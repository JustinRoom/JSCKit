package jsc.exam.jsckit.ui.zxing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.baseui.permission.PermissionChecker;
import jsc.kit.zxing.zxing.QRCodeEncoder;
import jsc.kit.zxing.zxing.ui.ZXingFragment;

public class ZXingQRCodeActivity extends BaseActivity {

    final String apkUrl = "https://github.com/JustinRoom/JSCKit/blob/master/capture/JSCKitDemo.apk?raw=true";
    ImageView ivQRCode;
    TextView tvScanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_xing_qr_code);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        ivQRCode = findViewById(R.id.iv_qr_code);
        tvScanResult = findViewById(R.id.tv_scan_result);
        handlerProvider.sendUIEmptyMessageDelay(0, 350);
    }

    @Override
    public void handleUIMessage(Message msg) {
        super.handleUIMessage(msg);
        createAndShowQRCode();
    }

    private void createAndShowQRCode() {
        Disposable disposable = Observable.just(apkUrl)
                .flatMap(new Function<String, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(String s) throws Exception {
                        int[] colors = {Color.GREEN, 0x99FF4081, Color.BLUE, Color.CYAN};
                        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
                        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(s, size, size, colors);
                        return Observable.just(bitmap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        ivQRCode.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public void widgetClick(View v) {
        //打开相机权限
        permissionChecker.checkPermissions(this,0x100, new PermissionChecker.OnPermissionCheckListener() {
            @Override
            public void onResult(int requestCode, boolean isAllGranted, @NonNull List<String> grantedPermissions, @Nullable List<String> deniedPermissions, @Nullable List<String> shouldShowPermissions) {
                if (isAllGranted) {
                    toScannerActivity();
                    return;
                }

                if (shouldShowPermissions != null && shouldShowPermissions.size() > 0) {
                    String message = "当前应用需要以下权限:\n\n" + PermissionChecker.getAllPermissionDes(getBaseContext(), shouldShowPermissions);
                    showPermissionRationaleDialog("温馨提示", message, "设置", "知道了");
                }
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
