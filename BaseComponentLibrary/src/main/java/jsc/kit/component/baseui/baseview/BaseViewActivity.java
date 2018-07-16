package jsc.kit.component.baseui.baseview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import jsc.kit.component.baseui.BaseAppCompatActivity;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseViewActivity extends BaseAppCompatActivity implements BaseViewCreateDelegate {

    public BaseViewProvider baseViewProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        baseViewProvider = new BaseViewProvider(this);
        baseViewProvider.setBaseViewCreateDelegate(this);
        setContentView(baseViewProvider.provide(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (baseViewProvider.getEmptyView() != null) {
            baseViewProvider.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseViewProvider.isLoading()) {
                        baseViewProvider.showLoadingPage(null);
                        reload();
                    }
                }
            });
        }

        if (baseViewProvider.getErrorView() != null) {
            baseViewProvider.getErrorView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseViewProvider.isLoading()) {
                        baseViewProvider.showLoadingPage(null);
                        reload();
                    }
                }
            });
        }
    }

    public void setEmptyViewEnable(boolean enable){
        if (baseViewProvider.getEmptyView() != null)
            baseViewProvider.getEmptyView().setEnabled(enable);
    }

    public void setErrorViewEnable(boolean enable){
        if (baseViewProvider.getErrorView() != null)
            baseViewProvider.getErrorView().setEnabled(enable);
    }

    public abstract void reload();



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
        return null;
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



    @Nullable
    @Override
    public View createTitleBar(@NonNull Context context) {
        return null;
    }

    @Nullable
    @Override
    public View createContentView(@NonNull Context context) {
        return null;
    }

    @Nullable
    @Override
    public View createEmptyView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText("Empty!!!\nClick to reload.");
        return textView;
    }

    @Nullable
    @Override
    public View createLoadingView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText("Loading...!!!\nPlease wait a minute.");
        return textView;
    }

    @Nullable
    @Override
    public View createErrorView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText("Error!!!\nClick to reload.");
        return textView;
    }

    @Override
    public void addCustomView(@NonNull Context context, @NonNull ConstraintLayout constraintLayout, @NonNull ConstraintSet constraintSet) {

    }
}
