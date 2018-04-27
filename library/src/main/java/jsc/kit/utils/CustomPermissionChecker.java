package jsc.kit.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
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
 * @see android.support.v4.content.PermissionChecker
 */
public final class CustomPermissionChecker {

    private Activity activity;
    private OnCheckListener onCheckListener;
    private String[] permissions;

    public CustomPermissionChecker() {
    }

    public CustomPermissionChecker(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    /**
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @return
     */
    public void checkPermissions(Activity activity, @IntRange(from = 0) int requestCode, String... permissions) {
        this.activity = activity;
        this.permissions = permissions;
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
                onCheckListener.onResult(
                        requestCode,
                        true,
                        Arrays.asList(permissions),
                        new ArrayList<String>(),
                        new ArrayList<String>()
                );
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

        List<String> grantedPermissions = patchGrantedPermissions(deniedPermissions);
        onCheckListener.onResult(
                requestCode,
                deniedPermissions.size() == 0,
                grantedPermissions,
                deniedPermissions,
                shouldShowPermissions
        );
        //处理收尾工作
        onCheckListener.onFinally(requestCode);
    }

    /**
     * 匹配出已经授权通过的权限
     * @param deniedPermissions
     * @return
     */
    private List<String> patchGrantedPermissions(List<String> deniedPermissions){
        if (deniedPermissions.size() == 0)
            return Arrays.asList(permissions);

        List<String> grantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            boolean isGranted = true;
            for (String deniedPermission : deniedPermissions) {
                isGranted = isGranted && !permission.equals(deniedPermission);
            }
            if (isGranted)
                grantedPermissions.add(permission);
        }
        return grantedPermissions;
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
         *
         * @param requestCode
         * @param isAllGranted if true, all permissions are granted.
         * @param grantedPermissions    所请求的permissions中已通过授权的部分permissions
         * @param deniedPermissions 所请求的permissions中未通过授权的部分permissions
         * @param shouldShowPermissions 所请求的permissions中未通过授权的部分permissions中的已勾选为【不再提醒】的permissions
         */
        void onResult(int requestCode,
                      boolean isAllGranted,
                      @NonNull List<String> grantedPermissions,
                      @NonNull List<String> deniedPermissions,
                      @NonNull List<String> shouldShowPermissions);

        /**
         * 在此方法中做一些收尾工作。例如释放资源。
         * @param requestCode
         */
        void onFinally(int requestCode);
    }
}
