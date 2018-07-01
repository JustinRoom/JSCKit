package jsc.kit.component.baseui.baseview;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
        baseViewProvider = new BaseViewProvider(this, getLayoutInflater());
        baseViewProvider.setBaseViewCreateDelegate(this);
        setContentView(baseViewProvider.provide());

        if (baseViewProvider.getEmptyView() != null){
            baseViewProvider.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseViewProvider.isLoading()){
                        baseViewProvider.showLoadingPage(null);
                        reload();
                    }
                }
            });
        }

        if (baseViewProvider.getErrorView() != null){
            baseViewProvider.getErrorView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseViewProvider.isLoading()){
                        baseViewProvider.showLoadingPage(null);
                        reload();
                    }
                }
            });
        }
    }

    public abstract void reload();

    @Nullable
    @Override
    public View createTitleBar(@NonNull LayoutInflater inflater, @NonNull LinearLayout parent) {
        return null;
    }

    @Nullable
    @Override
    public View createContentView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return null;
    }

    @Nullable
    @Override
    public View createEmptyView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return null;
    }

    @Nullable
    @Override
    public View createLoadingView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return null;
    }

    @Nullable
    @Override
    public View createErrorView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent) {
        return null;
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
}
