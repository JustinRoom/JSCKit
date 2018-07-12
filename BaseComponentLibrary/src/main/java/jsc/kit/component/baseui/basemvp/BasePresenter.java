package jsc.kit.component.baseui.basemvp;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BasePresenter<V extends IBaseView, M extends IBaseModel> {
    private WeakReference<V> viewWeakReference;
    private M model;

    public BasePresenter() {
    }

    public BasePresenter(@NonNull V view, @NonNull M model) {
        viewWeakReference = new WeakReference<>(view);
        this.model = model;
    }

    public M getModel() {
        return model;
    }

    public void setModel(@NonNull M model) {
        this.model = model;
    }

    public V getView() {
        if (viewWeakReference == null || viewWeakReference.get() == null)
            throw new NullPointerException("View was recycled.");
        return viewWeakReference.get();
    }

    public void setView(@NonNull V view) {
        viewWeakReference = new WeakReference<>(view);
    }

    public void release() {
        if (viewWeakReference != null){
            V view = viewWeakReference.get();
            view = null;
        }
    }
}
