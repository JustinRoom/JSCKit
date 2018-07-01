package jsc.exam.jsckit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.transition.Transition;

import jsc.kit.component.baseui.BaseAppCompatActivity;
import jsc.kit.component.baseui.transition.TransitionProvider;
import jsc.kit.component.utils.CustomToast;
import jsc.kit.component.utils.WindowUtils;

public abstract class BaseActivity extends BaseAppCompatActivity {



    public final void showCustomToast(@StringRes int resId) {
        CustomToast.Builder builder = new CustomToast.Builder()
                .text(this, resId)
                .topMargin(WindowUtils.getActionBarSize(this));
        showCustomToast(builder);
    }

    public final void showCustomToast(String txt) {
        CustomToast.Builder builder = new CustomToast.Builder()
                .text(txt)
                .topMargin(WindowUtils.getActionBarSize(this));
        showCustomToast(builder);
    }

    public final void showCustomToast(CustomToast.Builder builder) {
        CustomToast.getInstance().show(builder);
    }

    public final void requestSystemAlertWindowPermission() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 0x100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus) {

    }

    @Override
    public void onDownloadCompleted(Uri uri) {

    }

    @Override
    public void handleUIMessage(Message msg) {

    }

    @Override
    public void handleWorkMessage(Message msg) {

    }

    @Override
    public Transition createEnterTransition() {
        return TransitionProvider.createTransition(getIntent().getStringExtra("transition"), 300L);
    }

    @Override
    public Transition createExitTransition() {
        return null;
    }

    @Override
    public Transition createReturnTransition() {
        return null;
    }

    @Override
    public Transition createReenterTransition() {
        return null;
    }

    @Override
    public void initSharedElement() {

    }
}
