package jsc.kit.component.baseui.basemvp;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import jsc.kit.component.baseui.BaseLazyLoadFragment;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseMVPFragment extends BaseLazyLoadFragment {

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
    public void onDestroyView() {
        recyclePresenterResources();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
