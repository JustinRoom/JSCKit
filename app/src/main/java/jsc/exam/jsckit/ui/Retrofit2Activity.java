package jsc.exam.jsckit.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.R;
import jsc.exam.jsckit.service.ApiService;
import jsc.kit.retrofit2.LoadingDialogObserver;
import jsc.kit.retrofit2.retrofit.CustomHttpClient;
import jsc.kit.retrofit2.retrofit.CustomRetrofit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Retrofit2Activity extends BaseActivity {

    final String token = "751sadlsuonERASDFn8asFDSajn";
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_retrofit2);
        FrameLayout layout = new FrameLayout(this);
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        layout.setPadding(padding, 0, padding, 0);
        setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        textView  = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(0xff333333);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        layout.addView(textView, params);
        handlerProvider.sendUIEmptyMessageDelay(0, 400);
    }

    @Override
    public void handleUIMessage(Message msg) {
        super.handleUIMessage(msg);
        loadUserInfo();
    }

    private void loadUserInfo() {
        OkHttpClient client = new CustomHttpClient()
                .addHeader(new Pair<>("token", token))
                .setConnectTimeout(5_000)
                .setShowLog(true)
                .createOkHttpClient();
        Retrofit retrofit = new CustomRetrofit()
                //我在app的build.gradle文件的defaultConfig标签里定义了BASE_URL
                .setBaseUrl("https://raw.githubusercontent.com/")
                .setOkHttpClient(client)
                .createRetrofit();
        retrofit.create(ApiService.class)
                .getVersionInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingDialogObserver<String>(createLoadingDialog()) {
                    @Override
                    public void onStart(Disposable disposable) {

                    }

                    @Override
                    public void onResult(String s) {
                        textView.setText(s);
                    }

                    @Override
                    public void onException(Throwable e) {

                    }

                    @Override
                    public void onCompleteOrCancel(Disposable disposable) {

                    }
                });
    }
}
