package jsc.exam.jsckit.ui.mvp.presenter;

import android.app.Dialog;
import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jsc.exam.jsckit.ui.mvp.model.ITestModel;
import jsc.exam.jsckit.ui.mvp.view.ITestView;
import jsc.kit.retrofit2.LoadingDialogObserver;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 * @createTime 2018-06-06 2:26 PM Wednesday
 */
public class TestPresenter extends MyBasePresenter<ITestView, ITestModel> {

    private final String TAG = "TestPresenter";

    public TestPresenter() {
    }

    public TestPresenter(@NonNull ITestView view, @NonNull ITestModel model) {
        super(view, model);
    }

    public void loadVersionInfo() {
        if (!isCommonViewAttached())
            return;

        String baseUrl = getCommonView().getBaseUrl();
        String token = getCommonView().getToken();
        boolean isShowNetLog = getCommonView().isShowNetLog();
        String userId = getCommonView().getCurrentUserId();
        Dialog loadindDialog = getCommonView().getLoadingDialog();
        getModel().loadVersionInfo(baseUrl, token, isShowNetLog)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingDialogObserver<String>(loadindDialog) {
                    @Override
                    public void onStart(Disposable disposable) {
                        //加入到事件管理中
                        add(disposable);
                    }

                    @Override
                    public void onResult(String s) {
                        if (isViewAttached())
                            getView().onLoadVersionInfo(s);
                    }

                    @Override
                    public void onException(Throwable e) {

                    }

                    @Override
                    public void onCompleteOrCancel(Disposable disposable) {
                        //该事件已经执行完毕，或者被取消，从事件管理中移除，让GC回收资源。
                        remove(disposable);
                    }
                });
    }
}
