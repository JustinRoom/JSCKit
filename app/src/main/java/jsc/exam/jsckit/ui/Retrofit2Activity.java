package jsc.exam.jsckit.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.R;
import jsc.exam.jsckit.entity.User;
import jsc.exam.jsckit.service.ApiService;
import jsc.lib.retrofitlibrary.LoadingDialogObserver;
import jsc.lib.retrofitlibrary.response.BaseResponse;
import jsc.lib.retrofitlibrary.retrofit.CustomHttpClient;
import jsc.lib.retrofitlibrary.retrofit.CustomRetrofit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Retrofit2Activity extends AppCompatActivity {

    final String token = "751sadlsuonERASDFn8asFDSajn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit2);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

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
                .setBaseUrl(getString(R.string.BASE_URL))
                .setOkHttpClient(client)
                .createRetrofit();
        retrofit.create(ApiService.class)
                .login("1002")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingDialogObserver<BaseResponse<User>>(createLoadingDialog()) {
                    @Override
                    public void onNext(BaseResponse<User> userBaseResponse) {
                        User user = userBaseResponse.getResponse();
                    }

                    @Override
                    public void onNetStart(Disposable disposable) {
                        Log.i(getTitle().toString(), "onNetStart: ");
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        Log.i(getTitle().toString(), "onNetError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNetFinish(Disposable disposable) {
                        Log.i(getTitle().toString(), "onNetFinish: ");
                    }
                });
    }

    private AlertDialog createLoadingDialog() {
        return new AlertDialog.Builder(this)
                .setMessage("Loading...")
                .create();
    }
}
