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

    /**
     * You should call method {@link #isViewAttached()} before calling this method.
     * <br>For example:
     * <pre class="prettyprint">
     *     if(isViewAttached()){
     *         V view = getView();
     *         //todo
     *     }
     * </pre>
     *
     * @return view
     */
    public V getView() {
        return viewWeakReference.get();
    }

    public boolean isViewAttached(){
        return viewWeakReference.get() != null;
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
