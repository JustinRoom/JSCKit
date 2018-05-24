# DOCUMENT

## Usage
usage reference: [Retrofit2Activity.java](../../app/src/main/java/jsc/exam/jsckit/ui/Retrofit2Activity.java)
```
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
```
<br/>[LoadingDialogObserver](../../Retrofit2Library/src/main/java/jsc/lib/retrofitlibrary/LoadingDialogObserver.java) is a `Observer`.
<br/>It managers `LoadingDialog` and `Disposable`.
<br/>When you cancel the `LoadingDialog` which the `Disposable` associated with is also `dispose()`.

## Parameters
+ about [CustomHttpClient](../../Retrofit2Library/src/main/java/jsc/lib/retrofitlibrary/retrofit/CustomHttpClient.java)

| method | meaning |
| :--- | :--- |
| ```setShowLog(boolean showLog)``` | 是否显示网络日志，默认否 |
| ```setConnectTimeout(int connectTimeout)``` | 请求链接超时时间，默认10s |
| ```setReadTimeout(int readTimeout)``` | 默认10s |
| ```setWriteTimeout(int writeTimeout)``` | 默认10s |
| ```addHeader(@NonNull Pair<String, String> header)``` |  添加头部请求```Interceptor``` |
| ```addInterceptor(Interceptor interceptor)``` | 添加其他```Interceptor``` |
| ```setContext(Application applicationContext)``` | 添加上下文 |
| ```setCache(String cacheFileName, long maxCacheSize)``` | 设置网络缓存，必须调用```setContext(Application applicationContext)```。```cacheFileName```为文件名称(不包含路径，缓存文件默认保存路径为```contextWeakReference.getCacheDir())```，```maxCacheSize```最大缓存大小 |

+ about [CustomRetrofit](../../Retrofit2Library/src/main/java/jsc/lib/retrofitlibrary/retrofit/CustomRetrofit.java)

| method | meaning |
| :--- | :--- |
| ```setBaseUrl(@NonNull String baseUrl)``` | 设置```baseUrl```。如```https://github.com/``` |
| ```setOkHttpClient(@NonNull OkHttpClient okHttpClient)``` | 设置```OkHttpClient```（可调用```CustomHttpClient.createOkHttpClient()```创建）。 |
| ```addConverterFactory(@NonNull Converter.Factory factory)``` | 添加其他的```Converter.Factory```。默认自动添加```ScalarsConverterFactory```和```GsonConverterFactory``` |
| ```addCallAdapterFactory(@NonNull CallAdapter.Factory factory)``` | 添加其他的```CallAdapter.Factory```。默认自动```RxJava2CallAdapterFactory``` |
