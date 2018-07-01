package jsc.kit.component.baseui.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>permission check tool</p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public final class PermissionChecker {

    private Activity activity;
    private OnPermissionCheckListener onPermissionCheckListener;
    private List<String> permissions;

    public PermissionChecker() {

    }

    /**
     * check permissions with default request code 0
     *
     * @param activity activity
     * @param permissions permissions
     * @param onPermissionCheckListener check listener
     * @return is all permissions granted.
     */
    public boolean checkPermissions(Activity activity, OnPermissionCheckListener onPermissionCheckListener, String... permissions) {
        return checkPermissions(activity, 0, onPermissionCheckListener, permissions);
    }

    /**
     *
     * @param activity activity
     * @param requestCode requestCode
     * @param permissions permissions
     * @param onPermissionCheckListener check listener
     * @return is all permissions granted.
     */
    public boolean checkPermissions(Activity activity, @IntRange(from = 0) int requestCode, OnPermissionCheckListener onPermissionCheckListener, String... permissions) {
        if (permissions == null || permissions.length == 0)
            throw new NullPointerException("The parameter permissions can't be null.");

        this.activity = activity;
        this.onPermissionCheckListener = onPermissionCheckListener;
        this.permissions = Arrays.asList(permissions);
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
            if (onPermissionCheckListener != null)
                onPermissionCheckListener.onResult(
                        requestCode,
                        true,
                        this.permissions,
                        null,
                        null
                );
            return true;
        }

        //请求授权
        ActivityCompat.requestPermissions(activity, unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), 0);
        return false;
    }

    /**
     * Call this method inside {@link Activity#onRequestPermissionsResult(int, String[], int[])}.
     *
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grant result
     */
    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (onPermissionCheckListener == null)
            return;

        List<String> deniedPermissions = new ArrayList<>();
        List<String> shouldShowPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permissions[i]);
                boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                if (!shouldShow)
                    shouldShowPermissions.add(permission);
            }
        }

        //移除掉已拒绝的之后就是授权通过的
        this.permissions.removeAll(deniedPermissions);
        onPermissionCheckListener.onResult(
                requestCode,
                deniedPermissions.size() == 0,
                this.permissions,
                deniedPermissions,
                shouldShowPermissions
        );
    }

    public static String getAllPermissionDes(@NonNull Context context, List<String> permissions) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = permissions.size(); i < len; i++) {
            builder.append(i + 1);
            builder.append("、");
            builder.append(getPermissionDes(context, permissions.get(i)));
            if (i < len - 1)
                builder.append("\n");
        }
        return builder.toString();
    }

    public static CharSequence getPermissionDes(@NonNull Context context,String permission) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PermissionInfo info = packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);
            return "【" + info.loadLabel(packageManager) + "】" + info.loadDescription(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface OnPermissionCheckListener {
        /**
         *
         * @param requestCode requestCode
         * @param isAllGranted if true, all permissions are granted.
         * @param grantedPermissions    所请求的permissions中已通过授权的部分permissions
         * @param deniedPermissions 所请求的permissions中未通过授权的部分permissions
         * @param shouldShowPermissions 所请求的permissions中未通过授权的部分permissions中的已勾选为【不再提醒】的permissions
         */
        void onResult(int requestCode,
                      boolean isAllGranted,
                      @NonNull List<String> grantedPermissions,
                      @Nullable List<String> deniedPermissions,
                      @Nullable List<String> shouldShowPermissions);

    }
}
