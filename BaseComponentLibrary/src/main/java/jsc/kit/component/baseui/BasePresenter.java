package jsc.kit.component.baseui;

import android.support.annotation.NonNull;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * create time: 2018-06-06 9:35 AM Wednesday
 * @author jiangshicheng
 */
public abstract class BasePresenter<V extends IBaseView, M extends IBaseModel> {
    private V view;
    private M model;

    public BasePresenter() {
    }

    public BasePresenter(@NonNull V view, @NonNull M model) {
        this.view = view;
        this.model = model;
    }

    public M getModel() {
        return model;
    }

    public void setModel(@NonNull M model) {
        this.model = model;
    }

    public V getView() {
        if (view == null)
            throw new NullPointerException("Null exception from class " + getClass().getSimpleName() + ": its' view is null(or was recycled).");
        return view;
    }

    public void setView(@NonNull V view) {
        this.view = view;
    }

    public void release() {
        view = null;
    }
}
