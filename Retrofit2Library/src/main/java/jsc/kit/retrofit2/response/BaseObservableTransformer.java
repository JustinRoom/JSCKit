package jsc.kit.retrofit2.response;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * Compose {@link BaseResponse#response}.
 * <br><strong>For example:</strong>
 * <p>
 * <pre class="prettyprint">
 * public interface ApiService {
 *      {@code @GET("JustinRoom/JSCKit/master/capture/output.json")}
 *      {@code Observable<BaseResponse<VersionEntity>> getVersionInfo();}
 * }
 *
 *  new CustomRetrofit()
 *    .setBaseUrl("https://www.baidu.com/")
 *    .setOkHttpClient(new CustomHttpClient().createOkHttpClient())
 *    .createRetrofit()
 *    .create(ApiService.class)
 *    .getVersionInfo()
 *    .compose(new BaseObservableTransformer<VersionEntity, BaseResponse<VersionEntity>>())
 *    .subscribeOn(Schedulers.io())
 *    .observeOn(AndroidSchedulers.mainThread())
 *    .subscribe(new Consumer<VersionEntity>() {
 *          {@code @Override}
 *          public void accept(VersionEntity versionEntity) throws Exception {
 *              //todo
 *          }
 *        }, new Consumer<Throwable>() {
 *          {@code @Override}
 *          public void accept(Throwable throwable) throws Exception {
 *              //todo
 *          }
 *        }, new Action() {
 *          {@code @Override}
 *          public void run() throws Exception {
 *              //todo
 *          }
 *    });
 * </pre>
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 */
public class BaseObservableTransformer<T, R extends BaseResponse<T>> implements ObservableTransformer<R, T> {

    @Override
    public ObservableSource<T> apply(Observable<R> upstream) {
        return upstream.flatMap(new Function<R, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(R r) throws Exception {
                if (r != null){
                    dispatch(r.getCode(), r.getMsg());
                    return Observable.just(r.getResponse());
                }
                return null;
            }
        });
    }

    /**
     *
     *
     * @param code code. {@link BaseResponse#code}
     * @param msg message. {@link BaseResponse#msg}
     */
    protected void dispatch(int code, String msg){

    }
}
