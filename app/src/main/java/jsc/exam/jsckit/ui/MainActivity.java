package jsc.exam.jsckit.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
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
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.adapter.ClassItemAdapter;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.exam.jsckit.entity.VersionEntity;
import jsc.exam.jsckit.service.ApiService;
import jsc.exam.jsckit.ui.fragment.DefaultFragment;
import jsc.exam.jsckit.ui.mvp.activity.TestActivity;
import jsc.exam.jsckit.ui.zxing.ZXingQRCodeActivity;
import jsc.kit.component.baseui.transition.TransitionProvider;
import jsc.kit.component.baseui.download.DownloadEntity;
import jsc.kit.component.baseui.transition.TransitionEnum;
import jsc.kit.component.swiperecyclerview.OnItemClickListener;
import jsc.kit.component.baseui.permission.PermissionChecker;
import jsc.kit.retrofit2.LoadingDialogObserver;
import jsc.kit.retrofit2.retrofit.CustomHttpClient;
import jsc.kit.retrofit2.retrofit.CustomRetrofit;
import okhttp3.OkHttpClient;

public class MainActivity extends BaseActivity {

    @Override
    public Transition createExitTransition() {
        return TransitionProvider.createFade(300L);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setContentView(recyclerView);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        showTitleBarBackView(false);

        ClassItemAdapter adapter = new ClassItemAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener<ClassItem>() {
            @Override
            public void onItemClick(View view, ClassItem item) {
                toNewActivity(item);
            }

            @Override
            public void onItemClick(View view, ClassItem item, int adapterPosition, int layoutPosition) {

            }
        });
        adapter.setItems(getClassItems());

        handlerProvider.sendUIEmptyMessageDelay(0, 350L);
    }

    @Override
    public void handleUIMessage(Message msg) {
        super.handleUIMessage(msg);
        switch (msg.what) {
            case 0:
                loadVersionInfo();
                break;
            case 1:

                break;
        }
    }

    private List<ClassItem> getClassItems() {
        List<ClassItem> classItems = new ArrayList<>();
        classItems.add(new ClassItem("Components", ComponentsActivity.class));
        classItems.add(new ClassItem("ZXingQRCode", ZXingQRCodeActivity.class));
        classItems.add(new ClassItem("Retrofit2", Retrofit2Activity.class));
        classItems.add(new ClassItem("DateTimePicker", DateTimePickerActivity.class));
        classItems.add(new ClassItem("CustomToast", CustomToastActivity.class));
        classItems.add(new ClassItem("DownloadFile", DownloadFileActivity.class));
        classItems.add(new ClassItem("Photo", PhotoActivity.class));
        classItems.add(new ClassItem("BottomNavigationView", BottomNavigationViewActivity.class));
        classItems.add(new ClassItem("SharedTransition", SharedTransitionActivity.class));
        classItems.add(new ClassItem("Test(MVP)", TestActivity.class));
        classItems.add(new ClassItem("BaseView", BaseViewShowActivity.class));
        classItems.add(new ClassItem("EmptyFragment", EmptyFragmentActivity.class));
        classItems.add(new ClassItem("About", AboutActivity.class));
        return classItems;
    }

    private void toNewActivity(ClassItem item) {
        if (item.getLabel().equals("EmptyFragment")) {
            Bundle bundle = new Bundle();
            bundle.putString(DefaultFragment.EXTRA_CONTENT, "empty activity with fragment");
            bundle.putString(EmptyFragmentActivity.EXTRA_TITLE, "TestTitle");
            bundle.putBoolean(EmptyFragmentActivity.EXTRA_FULL_SCREEN, true);
            bundle.putString(EmptyFragmentActivity.EXTRA_FRAGMENT_CLASS_NAME, DefaultFragment.class.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bundle.putString("transition", TransitionEnum.SLIDE.getLabel());
                EmptyFragmentActivity.launchTransition(this, bundle, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                EmptyFragmentActivity.launch(this, bundle);
            }
            return;
        }

        Intent mIntent = new Intent();
        mIntent.setClass(this, item.getCls());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIntent.putExtra("transition", TransitionEnum.SLIDE.getLabel());
            startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(mIntent);
        }
    }

    private void loadVersionInfo() {
        OkHttpClient client = new CustomHttpClient()
                .setConnectTimeout(5_000)
                .setShowLog(true)
                .createOkHttpClient();
        new CustomRetrofit()
                //我在app的build.gradle文件的defaultConfig标签里定义了BASE_URL
                .setBaseUrl("https://raw.githubusercontent.com/")
                .setOkHttpClient(client)
                .createRetrofit()
                .create(ApiService.class)
                .getVersionInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingDialogObserver<String>(createLoadingDialog()) {
                    @Override
                    public void onStart(Disposable disposable) {

                    }

                    @Override
                    public void onResult(String s) {
                        s = s.substring(1, s.length() - 1);
                        VersionEntity entity = VersionEntity.fromJson(s);
                        showUpdateTipsDialog(entity);
                    }

                    @Override
                    public void onException(Throwable e) {

                    }

                    @Override
                    public void onCompleteOrCancel(Disposable disposable) {

                    }
                });
    }

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
                            checkPermissionBeforeDownloadApk(entity.getApkInfo().getVersionName());
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
    }

    private void checkPermissionBeforeDownloadApk(final String versionName) {
        permissionChecker.checkPermissions(this, 0, new PermissionChecker.OnPermissionCheckListener() {
            @Override
            public void onResult(int requestCode, boolean isAllGranted, @NonNull List<String> grantedPermissions, @Nullable List<String> deniedPermissions, @Nullable List<String> shouldShowPermissions) {
                if (isAllGranted) {
                    downloadApk(versionName);
                    return;
                }

                if (shouldShowPermissions != null && shouldShowPermissions.size() > 0) {
                    String message = "当前应用需要以下权限:\n\n" + PermissionChecker.getAllPermissionDes(getBaseContext(), shouldShowPermissions);
                    showPermissionRationaleDialog("温馨提示", message, "设置", "知道了");
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void downloadApk(String versionName) {
        DownloadEntity entity = new DownloadEntity();
        entity.setUrl("https://raw.githubusercontent.com/JustinRoom/JSCKit/master/capture/JSCKitDemo.apk");
        entity.setDestinationDirectory(new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS));
        entity.setSubPath("jsckit/JSCKitDemo" + versionName + ".apk");
        entity.setTitle("JSCKitDemo" + versionName + ".apk");
        entity.setDesc("JSCKit Library");
        entity.setMimeType("application/vnd.android.package-archive");
        fileDownloader.registerDownloadCompleteReceiver();
        fileDownloader.downloadFile(entity);
    }

    @Override
    public void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus) {

    }

    @Override
    public void onDownloadCompleted(Uri uri) {
        fileDownloader.unRegisterDownloadCompleteReceiver();
        if (uri == null)
            return;

        //8.0有未知应用安装请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            if (getPackageManager().canRequestPackageInstalls()) {
                installApk(uri);
            } else {
                requestInstallPackages(uri);
            }
        } else {
            installApk(uri);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestInstallPackages(final Uri uri) {
        permissionChecker.checkPermissions(this, 0, new PermissionChecker.OnPermissionCheckListener() {
            @Override
            public void onResult(int requestCode, boolean isAllGranted, @NonNull List<String> grantedPermissions, @Nullable List<String> deniedPermissions, @Nullable List<String> shouldShowPermissions) {
                if (isAllGranted) {
                    installApk(uri);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES));
                    }
                }
            }
        }, Manifest.permission.REQUEST_INSTALL_PACKAGES);
    }

    long lastClickTime = 0;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (lastClickTime > 0 && (curTime - lastClickTime < 3_000)) {
            super.onBackPressed();
        } else {
            showCustomToast("再次点击返回按钮退出应用");
            lastClickTime = curTime;
        }
    }
}
