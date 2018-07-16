package jsc.kit.component.baseui.baseview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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
     * @param context context
     * @return custom title bar, id : "page_title_bar"
     */
    @Nullable
    public View createTitleBar(@NonNull Context context);

    /**
     * Create custom content view.
     *
     * @param context context
     * @return custom content view, id : "page_content"
     */
    @Nullable
    public View createContentView(@NonNull Context context);

    /**
     * Create custom content view.
     *
     * @param context context
     * @return custom content view, id : "page_empty"
     */
    @Nullable
    public View createEmptyView(@NonNull Context context);

    /**
     * Create custom loading view.
     *
     * @param context context
     * @return custom loading view, id : "page_loading"
     */
    @Nullable
    public View createLoadingView(@NonNull Context context);

    /**
     * Create custom error view.
     *
     * @param context context
     * @return custom error view, id : "page_error"
     */
    @Nullable
    public View createErrorView(@NonNull Context context);

    /**
     * Add custom view into root view.
     * @param context context
     * @param constraintLayout root view
     * @param constraintSet constraint set
     */
    public void addCustomView(@NonNull Context context, @NonNull ConstraintLayout constraintLayout, @NonNull ConstraintSet constraintSet);
}
