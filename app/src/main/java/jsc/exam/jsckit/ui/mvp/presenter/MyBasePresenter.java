package jsc.exam.jsckit.ui.mvp.presenter;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import jsc.exam.jsckit.ui.mvp.view.CommonView;
import jsc.kit.component.baseui.basemvp.BasePresenter;
import jsc.kit.component.baseui.basemvp.IBaseModel;
import jsc.kit.component.baseui.basemvp.IBaseView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 * @createTime 2018-06-06 1:47 PM Wednesday
 */
public abstract class MyBasePresenter<V extends IBaseView, M extends IBaseModel> extends BasePresenter<V, M> {

    private CommonView commonView;
    private CompositeDisposable compositeDisposable = null;

    public MyBasePresenter() {
    }

    public MyBasePresenter(@NonNull V view, @NonNull M model) {
        super(view, model);
    }

    public CommonView getCommonView() {
        if (commonView == null)
            throw new NullPointerException("Null exception from class " + getClass().getSimpleName() + ": its' common view is null(or was recycled).");
        return commonView;
    }

    public void setCommonView(@NonNull CommonView commonView) {
        this.commonView = commonView;
    }

    @Override
    public void release() {
        clearAllDisposables();
        super.release();
    }

    /**
     * Add a disposable to manager.
     *
     * @param disposable disposable
     */
    public void add(@NonNull Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * Remove a disposable from manager.
     *
     * @param disposable disposable
     */
    public void remove(@NonNull Disposable disposable) {
        if (compositeDisposable == null) {
            return;
        }
        compositeDisposable.remove(disposable);
    }

    /**
     * Remove all disposables from manager.
     */
    public void clearAllDisposables() {
        if (compositeDisposable == null) {
            return;
        }
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }
}
