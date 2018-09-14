package jsc.kit.component.baseui.resizable;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import jsc.kit.component.swiperecyclerview.BaseRecyclerViewAdapter;

/**
 * 大小屏切换基础适配器。
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseResizableAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseRecyclerViewAdapter<T, VH> {

    public static final int VIEW_TYPE_SMALL = 0;
    public static final int VIEW_TYPE_FULL = 1;

    @IntDef({VIEW_TYPE_SMALL, VIEW_TYPE_FULL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
    }

    private int viewType = VIEW_TYPE_FULL;

    /**
     * 大小屏切换。
     * @param viewType one of {@link #VIEW_TYPE_SMALL}、{@link #VIEW_TYPE_FULL}
     * @param layoutId layoutId
     */
    public void updateViewType(@ViewType int viewType, @LayoutRes int layoutId) {
        if (this.viewType == viewType)
            return;

        this.viewType = viewType;
        this.layoutId = layoutId;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }
}
