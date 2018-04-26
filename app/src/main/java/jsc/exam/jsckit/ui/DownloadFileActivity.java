package jsc.exam.jsckit.ui;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.kit.entity.DownloadEntity;

public class DownloadFileActivity extends ABaseActivity {

    private int index;
    private List<Long> downloadIds = new ArrayList<>();
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                findDownloadFileUri(downloadId);
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())){
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);
        setTitle(getClass().getSimpleName().replace("Activity", ""));
    }

    public long download(){
        DownloadEntity entity = new DownloadEntity();
        entity.setUrl("https://raw.githubusercontent.com/JustinRoom/JSCKit/master/capture/JSCKitDemo.apk");
        entity.setSubPath("jsckit/JSCKitDemo"+ index + ".apk");
        entity.setTitle("JSCKitDemo"+ index + ".apk");
        entity.setDesc("test");
        entity.setMimeType("application/vnd.android.package-archive");
        return downloadFile(entity);
    }

    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.btn_add_download_task:
                index ++;
                long id = download();
                downloadIds.add(id);
                break;
        }
    }

    private void registerDownloadCompleteReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    protected void onDownloadCompleted(Uri uri) {
        if (uri != null){
            showCustomToast(uri.getLastPathSegment() + " was downloaded successfully.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerDownloadCompleteReceiver();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(downloadReceiver);
        super.onDestroy();
    }
}
