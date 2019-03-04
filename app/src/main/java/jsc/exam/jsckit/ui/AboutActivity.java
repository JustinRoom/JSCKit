package jsc.exam.jsckit.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.R;
import jsc.kit.zxing.zxing.QRCodeEncoder;

public class AboutActivity extends BaseActivity {

    final String apkUrl = "https://raw.githubusercontent.com/JustinRoom/JSCKit/master/capture/JSCKitDemo.apk";
    TextView tvVersion;
    ImageView ivQRCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        tvVersion = findViewById(R.id.tv_version);
        ivQRCode = findViewById(R.id.iv_qr_code);
        showVersion();
        handlerProvider.sendUIEmptyMessageDelay(0, 350);

    }

    private void showVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            tvVersion.setText("version:" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
                        int size = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
                        Bitmap shape = BitmapFactory.decodeResource(getResources(), R.drawable.kiss256);
                        Bitmap bitmap = QRCodeEncoder.encodeQRCode(s, size, size, shape);
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
}
