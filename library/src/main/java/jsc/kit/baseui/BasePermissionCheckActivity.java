package jsc.kit.baseui;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import jsc.kit.utils.MyPermissionChecker;

public abstract class BasePermissionCheckActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
//        destroyCustomToast(true);
        super.onDestroy();
    }

    protected MyPermissionChecker myPermissionChecker = null;

    public void checkPermissions(int requestCode, MyPermissionChecker.OnCheckListener checkListener, String... permissions) {
        if (myPermissionChecker == null) {
            myPermissionChecker = new MyPermissionChecker();
        }
        myPermissionChecker.setOnCheckListener(checkListener);
        myPermissionChecker.checkPermissions(this, requestCode, permissions);
    }

    /**
     * Recycle {@code "myPermissionChecker"}.
     */
    public void removePermissionChecker() {
        if (myPermissionChecker == null)
            return;
        myPermissionChecker.removeCheckListener();
        myPermissionChecker = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (myPermissionChecker != null)
            myPermissionChecker.onPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        if (myPermissionChecker != null)
            myPermissionChecker.shouldShowPermission(permission);
        return super.shouldShowRequestPermissionRationale(permission);
    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{android.R.attr.actionBarSize};
        TypedArray array = obtainStyledAttributes(typedValue.resourceId, attribute);
        int actionBarSize = array.getDimensionPixelSize(0, 0);
        array.recycle();
        return actionBarSize;
    }
}
