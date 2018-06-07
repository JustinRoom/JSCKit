package jsc.kit.component.baseui;

import android.os.Message;
import android.support.annotation.NonNull;
import android.transition.Transition;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 * @createTime 2018-06-06 9:33 AM Wednesday
 */
public abstract class ABaseMVPActivity extends APermissionCheckActivity {
    private List<BasePresenter> presenterManager = null;

    /**
     * Add presenter into presenter manager.
     * @param presenter presenter instance
     */
    public final void addToPresenterManager(@NonNull BasePresenter presenter){
        if (presenterManager == null){
            presenterManager = new ArrayList<>();
        }
        presenterManager.add(presenter);
    }

    /**
     * Remove presenter from presenter manager.
     * @param presenter presenter instance
     */
    public final void removeFromPresenterManager(@NonNull BasePresenter presenter){
        if (presenterManager != null && !presenterManager.isEmpty()){
            presenterManager.remove(presenter);
        }
    }

    /**
     * Release presenters' resources.
     */
    public void recyclePresenterResources(){
        if (presenterManager != null && !presenterManager.isEmpty()){
            for (BasePresenter presenter : presenterManager) {
                presenter.release();
                presenter = null;
            }
        }
    }

    @Override
    public void handleUIMessage(Message msg) {

    }

    @Override
    public void handleWorkMessage(Message msg) {

    }

    @Override
    public Transition createEnterTransition() {
        return null;
    }

    @Override
    public Transition createExitTransition() {
        return null;
    }

    @Override
    public Transition createReturnTransition() {
        return null;
    }

    @Override
    public Transition createReenterTransition() {
        return null;
    }

    @Override
    public void initSharedElement() {

    }

    @Override
    protected void onDestroy() {
        recyclePresenterResources();
        super.onDestroy();
    }
}
