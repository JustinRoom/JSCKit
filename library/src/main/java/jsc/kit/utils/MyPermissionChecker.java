package jsc.kit.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>permission check tool</p>
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

        //所请求的permissions都已授权通过
        if (isAllGranted) {
            if (onCheckListener != null)
                onCheckListener.onAllGranted(requestCode);
            return;
        }

        //请求授权
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
        if (onCheckListener == null)
            return;

        List<String> grantedPermissions = new ArrayList<>();
        List<String> deniedPermissions = new ArrayList<>();
        List<String> shouldShowPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                grantedPermissions.add(permissions[i]);
            } else {
                deniedPermissions.add(permissions[i]);
                boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                if (!shouldShow)
                    shouldShowPermissions.add(permission);
            }
        }

        //所请求的permissions中已通过授权的部分permissions
        onCheckListener.onGranted(requestCode, grantedPermissions);
        //所请求的permissions都已授权通过
        if (deniedPermissions.size() == 0) {
            onCheckListener.onAllGranted(requestCode);
            return;
        }
        //所请求的permissions中未通过授权的部分permissions
        onCheckListener.onDenied(requestCode, deniedPermissions);
        //所请求的permissions中未通过授权的部分permissions中的已勾选为【不再提醒】的permissions
        if (shouldShowPermissions.size() > 0)
            onCheckListener.onShouldShowSettingTips(shouldShowPermissions);
    }

    /**
     * Remove check listener:
     * <br/>{@link #onCheckListener} {@code = null;}
     */
    public void removeCheckListener() {
        onCheckListener = null;
    }

    public interface OnCheckListener {
        /**
         * 所请求的permissions都已授权通过
         * @param requestCode
         */
        void onAllGranted(int requestCode);

        /**
         * 所请求的permissions中已通过授权的部分permissions
         * @param requestCode
         * @param grantedPermissions
         */
        void onGranted(int requestCode, @NonNull List<String> grantedPermissions);

        /**
         * 所请求的permissions中未通过授权的部分permissions
         * @param requestCode
         * @param deniedPermissions
         */
        void onDenied(int requestCode, @NonNull List<String> deniedPermissions);

        /**
         * 所请求的permissions中未通过授权的部分permissions中的已勾选为【不再提醒】的permissions
         * @param shouldShowPermissions
         */
        void onShouldShowSettingTips(List<String> shouldShowPermissions);
    }
}
