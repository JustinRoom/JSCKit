package jsc.exam.jsckit.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.transition.Transition;

import jsc.kit.component.baseui.APermissionCheckActivity;
import jsc.kit.component.utils.CustomToast;

public abstract class ABaseActivity extends APermissionCheckActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void handleUIMessage(Message msg) {

    }

    @Override
    public void handleWorkMessage(Message msg) {

    }

    @Override
    public Transition createEnterTransition() {
        return createTransition(getIntent().getStringExtra("transition"), 300L);
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

    public final void showCustomToast(@StringRes int resId) {
        CustomToast.Builder builder = new CustomToast.Builder()
                .text(this, resId)
                .topMargin(getActionBarSize());
        showCustomToast(builder);
    }

    public final void showCustomToast(String txt) {
        CustomToast.Builder builder = new CustomToast.Builder()
                .text(txt)
                .topMargin(getActionBarSize());
        showCustomToast(builder);
    }

    public final void showCustomToast(CustomToast.Builder builder) {
        CustomToast.getInstance().show(builder);
    }
}
