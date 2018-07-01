package jsc.kit.component.baseui.baseview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface BaseViewCreateDelegate {

    /**
     * Create custom title bar.
     *
     * @param inflater inflater
     * @param parent the parent of custom title bar. It's default orientation is {@link LinearLayout#VERTICAL}.
     * @return custom title bar
     */
    @Nullable
    public View createTitleBar(@NonNull LayoutInflater inflater, @NonNull LinearLayout parent);

    /**
     * Create custom content view.
     *
     * @param inflater inflater
     * @param parent the parent of custom content view, id : "fy_content_container".
     * @return custom content view, id : "page_content"
     */
    @Nullable
    public View createContentView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent);

    /**
     * Create custom content view.
     *
     * @param inflater inflater
     * @param parent the parent of custom content view, id : "fy_content_container".
     * @return custom content view, id : "page_empty"
     */
    @Nullable
    public View createEmptyView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent);

    /**
     * Create custom loading view.
     *
     * @param inflater inflater
     * @param parent the parent of custom loading view, id : "fy_content_container".
     * @return custom loading view, id : "page_loading"
     */
    @Nullable
    public View createLoadingView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent);

    /**
     * Create custom error view.
     *
     * @param inflater inflater
     * @param parent the parent of custom error view, id : "fy_content_container".
     * @return custom error view, id : "page_error"
     */
    @Nullable
    public View createErrorView(@NonNull LayoutInflater inflater, @NonNull FrameLayout parent);
}
