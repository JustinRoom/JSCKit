package jsc.kit.baseui;

import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import java.util.List;

import jsc.kit.utils.MyPermissionChecker;

public abstract class APermissionCheckActivity extends AppCompatActivity {

    private MyPermissionChecker myPermissionChecker = null;

    public final void checkPermissions(int requestCode, MyPermissionChecker.OnCheckListener checkListener, String... permissions) {
        if (myPermissionChecker == null) {
            myPermissionChecker = new MyPermissionChecker();
        }
        myPermissionChecker.setOnCheckListener(checkListener);
        myPermissionChecker.checkPermissions(this, requestCode, permissions);
    }

    /**
     * Release resource. I suggest that you should call this method in {@link MyPermissionChecker.OnCheckListener#onFinally(int)}.
     * <br/>It does two things:
     * <br/>1、set {@link MyPermissionChecker#onCheckListener} as {@code null}
     * <br/>2、set {@link #myPermissionChecker} as {@code null}
     */
    public final void removePermissionChecker() {
        if (myPermissionChecker == null)
            return;
        myPermissionChecker.removeCheckListener();
        myPermissionChecker = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        myPermissionChecker.onPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Get status bar height.
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
