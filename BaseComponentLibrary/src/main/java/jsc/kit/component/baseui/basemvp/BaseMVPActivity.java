package jsc.kit.component.baseui.basemvp;

import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import jsc.kit.component.baseui.BaseAppCompatActivity;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseMVPActivity extends BaseAppCompatActivity {
    private List<BasePresenter> presenterManager = null;

    /**
     * Add presenter into presenter manager.
     *
     * @param presenter presenter instance
     */
    public final void addToPresenterManager(@NonNull BasePresenter presenter) {
        if (presenterManager == null) {
            presenterManager = new ArrayList<>();
        }
        presenterManager.add(presenter);
    }

    /**
     * Remove presenter from presenter manager.
     *
     * @param presenter presenter instance
     */
    public final void removeFromPresenterManager(@NonNull BasePresenter presenter) {
        if (presenterManager != null && !presenterManager.isEmpty()) {
            presenterManager.remove(presenter);
        }
    }

    /**
     * Release presenters' resources.
     */
    public void recyclePresenterResources() {
        if (presenterManager != null && !presenterManager.isEmpty()) {
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
    public void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus) {

    }

    @Override
    public void onDownloadCompleted(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        recyclePresenterResources();
        super.onDestroy();
    }
}
