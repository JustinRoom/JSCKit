package jsc.exam.jsckit.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.kit.component.baseui.download.DownloadEntity;
import jsc.kit.component.baseui.permission.PermissionChecker;

public class DownloadFileActivity extends BaseActivity {

    private int index;
    private List<Long> downloadIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
    }

    private void checkPermissionBeforeDownloadApk(){
        permissionChecker.checkPermissions(this, 0, new PermissionChecker.OnPermissionCheckListener() {
            @Override
            public void onResult(int requestCode, boolean isAllGranted, @NonNull List<String> grantedPermissions, @Nullable List<String> deniedPermissions, @Nullable List<String> shouldShowPermissions) {
                if (isAllGranted){
                    index ++;
                    long id = download();
                    downloadIds.add(id);
                    return;
                }

                if (shouldShowPermissions != null && shouldShowPermissions.size() > 0){
                    String message = "当前应用需要以下权限:\n\n" + PermissionChecker.getAllPermissionDes(getBaseContext(), shouldShowPermissions);
                    showPermissionRationaleDialog("温馨提示", message, "设置", "知道了");
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public long download(){
        DownloadEntity entity = new DownloadEntity();
        entity.setUrl("https://raw.githubusercontent.com/JustinRoom/JSCKit/master/capture/JSCKitDemo.apk");
        entity.setDestinationDirectory(new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS));
        entity.setSubPath("downloadList/JSCKitDemo"+ index + ".apk");
        entity.setTitle("JSCKitDemo"+ index + ".apk");
        entity.setDesc("test");
        entity.setMimeType("application/vnd.android.package-archive");
        return fileDownloader.downloadFile(entity);
    }

    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.btn_add_download_task:
                checkPermissionBeforeDownloadApk();
                break;
        }
    }

    @Override
    public void onDownloadCompleted(Uri uri) {
        if (uri != null){
            showCustomToast(uri.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileDownloader.registerDownloadCompleteReceiver();
    }

    @Override
    protected void onDestroy() {
        fileDownloader.unRegisterDownloadCompleteReceiver();
        super.onDestroy();
    }
}
