package jsc.kit.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 * @see android.support.v4.content.PermissionChecker
 */
public class MyPermissionChecker {

    private Activity activity;
    private OnCheckListener onCheckListener;

    public MyPermissionChecker() {
    }

    public MyPermissionChecker(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    /**
     * @param activity
     * @param requestCode
     * @param permissions
     * @return
     */
    public void checkPermissions(Activity activity, int requestCode, String... permissions) {
        this.activity = activity;
        boolean isAllGranted = true;
        List<String> unGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            boolean isGranted = ActivityCompat.checkSelfPermission(activity.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
            isAllGranted = isAllGranted && isGranted;
            if (!isGranted)
                unGrantedPermissions.add(permission);
        }

        //所有的permission都已经授权允许
        if (isAllGranted) {
            if (onCheckListener != null)
                onCheckListener.onAllGranted(requestCode);
            return;
        }

        //请求permission授权
        String[] needCheckPermissions = new String[unGrantedPermissions.size()];
        for (int i = 0; i < unGrantedPermissions.size(); i++) {
            needCheckPermissions[i] = unGrantedPermissions.get(i);
        }
        ActivityCompat.requestPermissions(activity, needCheckPermissions, requestCode);
    }

    /**
     * Call this method inside {@link Activity#onRequestPermissionsResult(int, String[], int[])}.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> grantedPermissions = new ArrayList<>();
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                grantedPermissions.add(permissions[i]);
            else
                deniedPermissions.add(permissions[i]);
        }

        if (onCheckListener == null)
            return;

        onCheckListener.onGranted(requestCode, grantedPermissions);
        if (deniedPermissions.size() == 0) {
            onCheckListener.onAllGranted(requestCode);
            return;
        }
        onCheckListener.onDenied(requestCode, deniedPermissions);
        List<String> shouldShowPermissions = new ArrayList<>();
        for (String permission : deniedPermissions) {
            boolean tempShouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (!tempShouldShow)
                shouldShowPermissions.add(permission);
        }
        if (shouldShowPermissions.size() > 0)
            onCheckListener.onShouldShowSettingTips(shouldShowPermissions);
    }

    /**
     * Remove check listener.
     */
    public void removeCheckListener() {
        onCheckListener = null;
    }

    public interface OnCheckListener {
        void onAllGranted(int requestCode);

        void onGranted(int requestCode, @NonNull List<String> grantedPermissions);

        void onDenied(int requestCode, @NonNull List<String> deniedPermissions);

        void onShouldShowSettingTips(List<String> shouldShowPermissions);
    }
}
