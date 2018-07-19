package jsc.kit.component.baseui.baseview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import jsc.kit.component.R;
import jsc.kit.component.utils.WindowUtils;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class BaseViewProvider {

    private Context context;
    private RelativeLayout rootView;
    private View titleBar;
    private View contentView;
    private View emptyView;
    private View loadingView;
    private View errorView;

    private BaseViewShowDelegate baseViewShowDelegate;
    private BaseViewCreateDelegate baseViewCreateDelegate;
    private boolean isLoading;

    public BaseViewProvider(@NonNull Context context) {
        this.context = context;
        createRootView();
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    public RelativeLayout getRootView() {
        return rootView;
    }

    @Nullable
    public View getTitleBar() {
        return titleBar;
    }

    @Nullable
    public View getContentView() {
        return contentView;
    }

    @Nullable
    public View getEmptyView() {
        return emptyView;
    }

    @Nullable
    public View getLoadingView() {
        return loadingView;
    }

    @Nullable
    public View getErrorView() {
        return errorView;
    }


    public void setBaseViewShowDelegate(BaseViewShowDelegate baseViewShowDelegate) {
        this.baseViewShowDelegate = baseViewShowDelegate;
    }

    public void setBaseViewCreateDelegate(BaseViewCreateDelegate baseViewCreateDelegate) {
        this.baseViewCreateDelegate = baseViewCreateDelegate;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void createRootView() {
        rootView = new RelativeLayout(context);
        rootView.setFitsSystemWindows(true);
    }

    public View provide() {
        titleBar = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createTitleBar(context);
        //add title bar
        if (titleBar != null) {
            titleBar.setId(R.id.page_title_bar);
            rootView.addView(titleBar);
        }
        if (baseViewCreateDelegate != null)
            baseViewCreateDelegate.initCustomView(context, rootView);
        return rootView;
    }

    /**
     * show load view.
     *
     * @param bundle data
     */
    public final void showLoadingPage(@Nullable Bundle bundle) {
        isLoading = true;
        removeView(contentView);
        removeView(errorView);
        removeView(emptyView);
        if (loadingView == null){
            loadingView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createLoadingView(context);
            if (loadingView != null){
                loadingView.setId(R.id.page_loading);
            }
        }
        addView(loadingView);
        if (baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowLoadingPage(loadingView, bundle);
        }
    }

    /**
     * show content view
     *
     * @param bundle data
     */
    public final void showContentPage(@Nullable Bundle bundle) {
        isLoading = false;
        removeView(errorView);
        removeView(emptyView);
        removeView(loadingView);
        if (contentView == null){
            contentView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createContentView(context);
            if (contentView != null){
                contentView.setId(R.id.page_content);
            }
        }
        addView(contentView);
        if (baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowContentPage(contentView, bundle);
        }
    }

    /**
     * show empty view
     *
     * @param bundle data
     */
    public final void showEmptyPage(@Nullable Bundle bundle) {
        isLoading = false;
        removeView(contentView);
        removeView(errorView);
        removeView(loadingView);
        if (emptyView == null){
            emptyView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createEmptyView(context);
            if (emptyView != null){
                emptyView.setId(R.id.page_empty);
            }
        }
        addView(emptyView);
        if (baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowEmptyPage(emptyView, bundle);
        }
    }

    /**
     * show error view.
     *
     * @param bundle data
     */
    public final void showErrorPage(@Nullable Bundle bundle) {
        isLoading = false;
        removeView(contentView);
        removeView(emptyView);
        removeView(loadingView);
        if (errorView == null){
            errorView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createErrorView(context);
            if (errorView != null){
                errorView.setId(R.id.page_error);
            }
        }
        addView(errorView);
        if (baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowErrorPage(errorView, bundle);
        }
    }

    private void removeView(View view){
        if (view != null)
            rootView.removeView(view);
    }

    private void addView(View view){
        if (view == null)
            return;
        if (view.getParent() != null)
            throw new IllegalStateException("This view already has parent.");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (titleBar != null){
            params.addRule(RelativeLayout.BELOW, R.id.page_title_bar);
            rootView.addView(view, 1, params);
        } else {
            rootView.addView(view, params);
        }
    }
}
