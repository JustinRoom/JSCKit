package jsc.kit.component.baseui.baseview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import jsc.kit.component.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class BaseViewProvider {

    private Context context;
    private LinearLayout rootView;
    private FrameLayout contentContainer;
    private View titleBar;
    private View contentView;
    private View emptyView;
    private View loadingView;
    private View errorView;

    private BaseViewShowDelegate baseViewShowDelegate;
    private BaseViewCreateDelegate baseViewCreateDelegate;
    private LayoutInflater inflater;
    private boolean isLoading;

    public BaseViewProvider(@NonNull Context context, @Nullable LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
        if (this.inflater == null)
            this.inflater = LayoutInflater.from(context);
        createRootView();
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    public LinearLayout getRootView() {
        return rootView;
    }

    @NonNull
    public FrameLayout getContentContainer() {
        return contentContainer;
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
        rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setFitsSystemWindows(true);

        contentContainer = new FrameLayout(context);
        contentContainer.setId(R.id.fy_content_container);
        rootView.addView(contentContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public View provide() {
        titleBar = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createTitleBar(inflater, rootView);
        contentView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createContentView(inflater, contentContainer);
        emptyView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createEmptyView(inflater, contentContainer);
        errorView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createErrorView(inflater, contentContainer);
        loadingView = baseViewCreateDelegate == null ? null : baseViewCreateDelegate.createLoadingView(inflater, contentContainer);

        if (titleBar != null) {
            titleBar.setId(R.id.page_title_bar);
            if (titleBar.getParent() == null)
                rootView.addView(titleBar, 0);
        }

        if (contentView != null) {
            contentView.setId(R.id.page_content);
            if (contentView.getParent() == null)
                contentContainer.addView(contentView);
        }

        if (emptyView != null) {
            emptyView.setId(R.id.page_empty);
            if (emptyView.getParent() == null)
                contentContainer.addView(emptyView);
        }

        if (errorView != null) {
            errorView.setId(R.id.page_error);
            if (errorView.getParent() == null)
                contentContainer.addView(errorView);
        }

        if (loadingView != null) {
            loadingView.setId(R.id.page_loading);
            if (loadingView.getParent() == null)
                contentContainer.addView(loadingView);
        }

        return rootView;
    }

    /**
     * show load view.
     *
     * @param bundle data
     */
    public final void showLoadingPage(@Nullable Bundle bundle) {
        isLoading = true;
        if (contentView != null)
            contentView.setVisibility(View.GONE);
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            if (baseViewShowDelegate != null) {
                baseViewShowDelegate.onShowErrorPage(loadingView, bundle);
            }
        }
    }

    /**
     * show content view
     *
     * @param bundle data
     */
    public final void showContentPage(@Nullable Bundle bundle) {
        isLoading = false;
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
            if (baseViewShowDelegate != null) {
                baseViewShowDelegate.onShowErrorPage(contentView, bundle);
            }
        }
    }

    /**
     * show empty view
     *
     * @param bundle data
     */
    public final void showEmptyPage(@Nullable Bundle bundle) {
        isLoading = false;
        if (contentView != null)
            contentView.setVisibility(View.GONE);
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            if (baseViewShowDelegate != null) {
                baseViewShowDelegate.onShowErrorPage(emptyView, bundle);
            }
        }
    }

    /**
     * show error view.
     *
     * @param bundle data
     */
    public final void showErrorPage(@Nullable Bundle bundle) {
        isLoading = false;
        if (contentView != null)
            contentView.setVisibility(View.GONE);
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
            if (baseViewShowDelegate != null) {
                baseViewShowDelegate.onShowErrorPage(errorView, bundle);
            }
        }
    }
}
