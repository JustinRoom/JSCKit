package jsc.exam.jsckit.ui;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.adapter.ClassItemAdapter;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.exam.jsckit.entity.VersionEntity;
import jsc.exam.jsckit.service.ApiService;
import jsc.exam.jsckit.ui.zxing.ZXingQRCodeActivity;
import jsc.kit.swiperecyclerview.OnItemClickListener;
import jsc.kit.utils.download.DownloadEntity;
import jsc.lib.retrofitlibrary.LoadingDialogObserver;
import jsc.lib.retrofitlibrary.retrofit.CustomHttpClient;
import jsc.lib.retrofitlibrary.retrofit.CustomRetrofit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class MainActivity extends ABaseActivity {

    private BroadcastReceiver downloadReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setContentView(recyclerView);

        ClassItemAdapter adapter = new ClassItemAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener<ClassItem>() {
            @Override
            public void onItemClick(View view, ClassItem item) {
                startActivity(new Intent(view.getContext(), item.getCls()));
            }

            @Override
            public void onItemClick(View view, ClassItem item, int adapterPosition, int layoutPosition) {

            }
        });
        adapter.setItems(getClassItems());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadVersionInfo();
            }
        }, 500);
    }

    private List<ClassItem> getClassItems() {
        List<ClassItem> classItems = new ArrayList<>();
        classItems.add(new ClassItem("ComponentList", ComponentListActivity.class));
        classItems.add(new ClassItem("ZXingQRCode", ZXingQRCodeActivity.class));
        classItems.add(new ClassItem("Retrofit2", Retrofit2Activity.class));
        classItems.add(new ClassItem("DateTimePicker", DateTimePickerActivity.class));
        classItems.add(new ClassItem("CustomToast", CustomToastActivity.class));
        classItems.add(new ClassItem("DownloadFile", DownloadFileActivity.class));
        classItems.add(new ClassItem("AboutActivity", AboutActivity.class));
        return classItems;
    }

    private void loadVersionInfo() {
        OkHttpClient client = new CustomHttpClient()
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
                    public void onNext(String s) {
                        s = s.substring(1, s.length() - 1);
                        VersionEntity entity = VersionEntity.fromJson(s);
                        showUpdateTipsDialog(entity);
                    }

                    @Override
                    public void onNetStart(Disposable disposable) {
                        Log.i("MainActivity", "onNetStart: ");
                    }

                    @Override
                    public void onNetError(Throwable e) {

                    }

                    @Override
                    public void onNetFinish(Disposable disposable) {

                    }
                });
    }

    private AlertDialog createLoadingDialog() {
        return new AlertDialog.Builder(this)
                .setMessage("正在检查更新...")
                .create();
    }

    /**
     *
     * @param entity
     */
    private void showUpdateTipsDialog(final VersionEntity entity) {
        if (entity == null)
            return;

        int curVersionCode = 0;
        String curVersionName = "";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            curVersionCode = info.versionCode;
            curVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (curVersionCode > 0 && entity.getApkInfo().getVersionCode() > curVersionCode)
            new AlertDialog.Builder(this)
                    .setTitle("更新提示")
                    .setMessage("1、当前版本：" + curVersionName + "\n2、最新版本：" + entity.getApkInfo().getVersionName())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadApk(entity.getApkInfo().getVersionName());
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
    }

    public void downloadApk(String versionName){
        registerDownloadCompleteReceiver();
        DownloadEntity entity = new DownloadEntity();
        entity.setUrl("https://raw.githubusercontent.com/JustinRoom/JSCKit/master/capture/JSCKitDemo.apk");
        entity.setSubPath("JSCKitDemo"+ versionName + ".apk");
        entity.setTitle("JSCKitDemo"+ versionName + ".apk");
        entity.setDesc("JSCKit Library");
        entity.setMimeType("application/vnd.android.package-archive");
        downloadFile(entity);
    }

    /**
     * 注册下载完成监听
     */
    private void registerDownloadCompleteReceiver(){
        if (downloadReceiver == null)
            downloadReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
                        unRegisterDownloadCompleteReceiver();
                        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        findDownloadFileUri(downloadId);
                    }
                }
            };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, intentFilter);
    }

    /**
     * 注销下载完成监听
     */
    private void unRegisterDownloadCompleteReceiver(){
        if (downloadReceiver != null){
            unregisterReceiver(downloadReceiver);
            downloadReceiver = null;
        }
    }

    @Override
    protected void onDownloadCompleted(Uri uri) {
        if (uri == null)
            return;

        //8.0有未知应用安装请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //先获取是否有安装未知来源应用的权限
            if (getPackageManager().canRequestPackageInstalls())
                installApk(uri);
        } else {
            installApk(uri);
        }
    }
}
