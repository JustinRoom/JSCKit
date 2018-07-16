package jsc.kit.component.baseui.baseview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;

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
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;
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
    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
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
        constraintLayout = new ConstraintLayout(context);
        constraintLayout.setFitsSystemWindows(true);
        constraintSet = new ConstraintSet();
    }

    public View provide() {
        titleBar = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createTitleBar(context);
        contentView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createContentView(context);
        emptyView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createEmptyView(context);
        errorView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createErrorView(context);
        loadingView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createLoadingView(context);

        //add title bar
        if (titleBar != null) {
            titleBar.setId(R.id.page_title_bar);
            constraintLayout.addView(titleBar);

            constraintSet.connect(R.id.page_title_bar, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(R.id.page_title_bar, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.constrainWidth(R.id.page_title_bar, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(R.id.page_title_bar, WindowUtils.getActionBarSize(context));
        }

        //add content view
        if (contentView != null) {
            contentView.setId(R.id.page_content);
            constraintLayout.addView(contentView);

            constraintSet.connect(R.id.page_content, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(R.id.page_content, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            if (titleBar == null)
                constraintSet.connect(R.id.page_content, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            else
                constraintSet.connect(R.id.page_content, ConstraintSet.TOP, R.id.page_title_bar, ConstraintSet.BOTTOM);
            constraintSet.connect(R.id.page_content, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(R.id.page_content, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(R.id.page_content, ConstraintSet.MATCH_CONSTRAINT);
        }

        //add empty view
        if (emptyView != null) {
            emptyView.setId(R.id.page_empty);
            constraintLayout.addView(emptyView);

            constraintSet.connect(R.id.page_empty, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(R.id.page_empty, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            if (titleBar == null)
                constraintSet.connect(R.id.page_empty, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            else
                constraintSet.connect(R.id.page_empty, ConstraintSet.TOP, R.id.page_title_bar, ConstraintSet.BOTTOM);
            constraintSet.connect(R.id.page_empty, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(R.id.page_empty, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(R.id.page_empty, ConstraintSet.MATCH_CONSTRAINT);
        }

        //add error view
        if (errorView != null) {
            errorView.setId(R.id.page_error);
            constraintLayout.addView(errorView);

            constraintSet.connect(R.id.page_error, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(R.id.page_error, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            if (titleBar == null)
                constraintSet.connect(R.id.page_error, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            else
                constraintSet.connect(R.id.page_error, ConstraintSet.TOP, R.id.page_title_bar, ConstraintSet.BOTTOM);
            constraintSet.connect(R.id.page_error, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(R.id.page_error, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(R.id.page_error, ConstraintSet.MATCH_CONSTRAINT);
        }

        //add loading view
        if (loadingView != null) {
            loadingView.setId(R.id.page_loading);
            constraintLayout.addView(loadingView);

            constraintSet.connect(R.id.page_loading, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(R.id.page_loading, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            if (titleBar == null)
                constraintSet.connect(R.id.page_loading, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            else
                constraintSet.connect(R.id.page_loading, ConstraintSet.TOP, R.id.page_title_bar, ConstraintSet.BOTTOM);
            constraintSet.connect(R.id.page_loading, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(R.id.page_loading, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(R.id.page_loading, ConstraintSet.MATCH_CONSTRAINT);

        }

        //add custom view
        if (baseViewCreateDelegate != null)
            baseViewCreateDelegate.addCustomView(context, constraintLayout, constraintSet);

        constraintSet.applyTo(constraintLayout);

        showView(contentView, false);
        showView(emptyView, false);
        showView(errorView, false);

        return constraintLayout;
    }

    private void showView(View view, boolean show) {
        if (view != null)
            view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * show load view.
     *
     * @param bundle data
     */
    public final void showLoadingPage(@Nullable Bundle bundle) {
        isLoading = true;
        showView(contentView, false);
        showView(errorView, false);
        showView(emptyView, false);
        showView(loadingView, true);
        if (loadingView != null && baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowErrorPage(loadingView, bundle);
        }
    }

    /**
     * show content view
     *
     * @param bundle data
     */
    public final void showContentPage(@Nullable Bundle bundle) {
        isLoading = false;
        showView(contentView, true);
        showView(errorView, false);
        showView(emptyView, false);
        showView(loadingView, false);
        if (contentView != null && baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowErrorPage(contentView, bundle);
        }
    }

    /**
     * show empty view
     *
     * @param bundle data
     */
    public final void showEmptyPage(@Nullable Bundle bundle) {
        isLoading = false;
        showView(contentView, false);
        showView(errorView, false);
        showView(emptyView, true);
        showView(loadingView, false);
        if (emptyView != null && baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowErrorPage(emptyView, bundle);
        }
    }

    /**
     * show error view.
     *
     * @param bundle data
     */
    public final void showErrorPage(@Nullable Bundle bundle) {
        isLoading = false;
        showView(contentView, false);
        showView(errorView, true);
        showView(emptyView, false);
        showView(loadingView, false);
        if (errorView != null && baseViewShowDelegate != null) {
            baseViewShowDelegate.onShowErrorPage(errorView, bundle);
        }
    }
}
