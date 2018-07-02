package jsc.exam.jsckit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.kit.component.baseui.baseview.BaseViewActivity;
import jsc.kit.component.baseui.baseview.BaseViewShowDelegate;
import jsc.kit.component.baseui.transition.TransitionProvider;
import jsc.kit.component.utils.AntiShakeUtils;

/**
 * @author jiangshicheng
 */
public class BaseViewShowActivity extends BaseViewActivity implements BaseViewShowDelegate {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseViewProvider.setBaseViewShowDelegate(this);
        handlerProvider.sendUIEmptyMessageDelay(0, 350L);
    }

    @Override
    public void handleUIMessage(Message msg) {
        switch (msg.what){
            case 0:
                baseViewProvider.showLoadingPage(null);
                int what = new Random().nextInt(3) + 1;
                handlerProvider.sendUIEmptyMessageDelay(what, 2000);
                break;
            case 1:
                baseViewProvider.showContentPage(null);
                break;
            case 2:
                baseViewProvider.showEmptyPage(null);
                break;
            case 3:
                baseViewProvider.showErrorPage(null);
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View createTitleBar(@NonNull LayoutInflater inflater, @NonNull LinearLayout parent) {
        return null;
    }

    @Override
    public View createContentView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return inflater.inflate(R.layout.content_base_view_layout, parent, false);
    }

    @Override
    public View createEmptyView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return inflater.inflate(R.layout.page_empty_layout, parent, false);
    }

    @Override
    public View createLoadingView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        View view = inflater.inflate(R.layout.page_loading_layout, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        parent.addView(view, params);
        return view;
    }

    @Override
    public View createErrorView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return inflater.inflate(R.layout.page_errror_layout, parent, false);
    }

    @Override
    public void reload() {
        int what = new Random().nextInt(3) + 1;
        handlerProvider.sendUIEmptyMessageDelay(what, 1000);
    }

    @Override
    public void onShowContentPage(@NonNull View contentView, @Nullable Bundle bundle) {

    }

    @Override
    public void onShowEmptyPage(@NonNull View emptyView, @Nullable Bundle bundle) {

    }

    @Override
    public void onShowLoadingPage(@NonNull View loadingView, @Nullable Bundle bundle) {

    }

    @Override
    public void onShowErrorPage(@NonNull View errorView, @Nullable Bundle bundle) {

    }
}
