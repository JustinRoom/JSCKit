package jsc.kit.component.baseui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jsc.kit.component.utils.FileProviderCompat;
import jsc.kit.component.utils.CustomPermissionChecker;
import jsc.kit.component.entity.DownloadEntity;

/**
 * <p>
 * a frame of checking permission、downloading file
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public abstract class APermissionCheckActivity extends AppCompatActivity {

    private CustomPermissionChecker customPermissionChecker = null;

    public final boolean checkPermissions(String... permissions) {
        return checkPermissions(0, null, permissions);
    }

    public final boolean checkPermissions(@IntRange(from = 0) int requestCode, CustomPermissionChecker.OnCheckListener checkListener, String... permissions) {
        if (customPermissionChecker == null) {
            customPermissionChecker = new CustomPermissionChecker();
        }
        return customPermissionChecker.checkPermissions(this, requestCode, checkListener, permissions);
    }

    /**
     * Release resource. I suggest that you should call this method in {@link CustomPermissionChecker.OnCheckListener#onFinally(int)}.
     */
    public final void recyclePermissionChecker() {
        if (customPermissionChecker == null)
            return;
        customPermissionChecker = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        customPermissionChecker.onPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Download file.
     * <br>If {@link DownloadEntity#destinationDirectory} is null, it will be downloaded into specific folder.
     * <br>The specific path is: {@code request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, subPath);}.
     * <br>see {@link DownloadManager.Request#setDestinationInExternalFilesDir(Context, String, String)}
     *
     * @param downloadEntity
     * @return
     */
    public final long downloadFile(DownloadEntity downloadEntity) {
        String url = downloadEntity.getUrl();
        if (TextUtils.isEmpty(url))
            return -1;

        Uri uri = Uri.parse(url);
        String subPath = downloadEntity.getSubPath();
        if (subPath == null || subPath.trim().length() == 0) {
            subPath = uri.getLastPathSegment();
        }

        File destinationDirectory = downloadEntity.getDestinationDirectory();
        if (destinationDirectory == null) {
            destinationDirectory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }

        File file = new File(destinationDirectory, subPath);
        File directory = file.getParentFile();
        if (!directory.exists()){//创建文件保存目录
            boolean result = directory.mkdirs();
            if (!result)
                Log.e("APermissionCheck", "Failed to make directories.");
        }

        if (file.exists()){
//            boolean result = file.delete();
//            if (!result)
//                Log.e("APermissionCheck", "Failed to delete file.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DownloadManager.Request request = new DownloadManager.Request(uri);
        //设置title
        request.setTitle(downloadEntity.getTitle());
        // 设置描述
        request.setDescription(downloadEntity.getDesc());
        // 完成后显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //
        Uri destinationUri = Uri.withAppendedPath(Uri.fromFile(destinationDirectory), subPath);
//        Uri destinationUri = FileProviderCompat.getUriForFile(this, file);
        request.setDestinationUri(destinationUri);
//        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, subPath);
        request.setMimeType(downloadEntity.getMimeType());
        request.setVisibleInDownloadsUi(true);

        DownloadManager mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        return mDownloadManager == null ? -1 : mDownloadManager.enqueue(request);
    }

    /**
     * @param downLoadId
     */
    public final void progress(long downLoadId) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downLoadId);
        Cursor c = null;
        int downloadedBytes = 0;
        int totalBytes = 0;
        int downStatus = 0;
        try {
            assert downloadManager != null;
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                downloadedBytes = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                totalBytes = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                downStatus = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        onDownloadProgress(downloadedBytes, totalBytes, downStatus);
    }

    /**
     * Get uri by download id.
     *
     * @param completeDownLoadId
     */
    public final void findDownloadFileUri(long completeDownLoadId) {
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 6.0以下
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            assert downloadManager != null;
            uri = downloadManager.getUriForDownloadedFile(completeDownLoadId);
        } else {
            File file = queryDownloadedFile(completeDownLoadId);
            uri = FileProviderCompat.getUriForFile(this, file);
        }
        onDownloadCompleted(uri);
    }

    private File queryDownloadedFile(long downloadId) {
        File targetFile = null;
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            assert downloadManager != null;
            Cursor cur = downloadManager.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetFile;
    }

    /**
     * Install application.
     *
     * @param uri
     */
    public final void installApk(Uri uri) {
        Intent intentInstall = new Intent();
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setAction(Intent.ACTION_VIEW);
        FileProviderCompat.setDataAndType(intentInstall, uri, "application/vnd.android.package-archive", true);
        startActivity(intentInstall);
    }

    /**
     * Observer downloading progress.
     *
     * @param downloadedBytes
     * @param totalBytes
     * @param downStatus
     */
    protected void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus) {

    }

    /**
     * Download successfully.
     *
     * @param uri
     */
    protected void onDownloadCompleted(Uri uri) {

    }


    /**
     * Get status bar height.
     *
     * @return
     */
    public final int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * Get action bar height.
     *
     * @return
     */
    public final int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{android.R.attr.actionBarSize};
        TypedArray array = obtainStyledAttributes(typedValue.resourceId, attribute);
        int actionBarSize = array.getDimensionPixelSize(0, 0);
        array.recycle();
        return actionBarSize;
    }

    public void showPermissionRationaleDialog(String title, String message, String positiveButton, String negativeButton) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        //跳转到当前应用的设置界面
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(negativeButton, null)
                .show();
    }

    public String getAllPermissionDes(List<String> permissions) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = permissions.size(); i < len; i++) {
            builder.append(i + 1);
            builder.append("、");
            builder.append(getPermissionDes(permissions.get(i)));
            if (i < len - 1)
                builder.append("\n");
        }
        return builder.toString();
    }

    public CharSequence getPermissionDes(String permission) {
        try {
            PermissionInfo info = getPackageManager().getPermissionInfo(permission, PackageManager.GET_META_DATA);
            return "【" + info.loadLabel(getPackageManager()) + "】" + info.loadDescription(getPackageManager());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
