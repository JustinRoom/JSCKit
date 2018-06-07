package jsc.exam.jsckit.ui.mvp.model;

import android.util.Pair;

import io.reactivex.Observable;
import jsc.exam.jsckit.service.ApiService;
import jsc.kit.retrofit2.retrofit.CustomHttpClient;
import jsc.kit.retrofit2.retrofit.CustomRetrofit;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 * @createTime 2018-06-06 2:31 PM Wednesday
 */
public class TestModel implements ITestModel {

    @Override
    public Observable<String> loadVersionInfo(String baseUrl, String token, boolean showNetLog) {
        return new CustomRetrofit()
                .setBaseUrl(baseUrl)
                .setOkHttpClient(new CustomHttpClient()
                        .addHeader(new Pair<>("token", token))
                        .setConnectTimeout(5_000)
                        .setShowLog(showNetLog)
                        .createOkHttpClient())
                .createRetrofit()
                .create(ApiService.class)
                .getVersionInfo();
    }
}
