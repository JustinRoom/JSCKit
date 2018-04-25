package jsc.exam.jsckit.service;

import io.reactivex.Observable;
import jsc.exam.jsckit.entity.User;
import jsc.lib.retrofitlibrary.response.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("user/{userId}")
    Observable<BaseResponse<User>> login(@Path("userId") String userId);

    @GET("JustinRoom/JSCKit/master/capture/output.json")
    Observable<String> getVersionInfo();

}
