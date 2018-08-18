package jsc.kit.component.baseui.basemvp2;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class LifecyclePresenterImpl implements LifecycleContract.Presenter {

    private WeakReference<LifecycleContract.View> viewWeakReference = null;

    public LifecyclePresenterImpl() {}

    public LifecyclePresenterImpl(@NonNull LifecycleContract.View v) {
        attach(v);
    }

    public void attach(@NonNull LifecycleContract.View v) {
        viewWeakReference = new WeakReference<>(v);
    }

    public boolean isAttached() {
        return viewWeakReference != null && viewWeakReference.get() != null;
    }

    public LifecycleContract.View view() {
        return viewWeakReference.get();
    }

    @Override
    public void start() {
        if (isAttached())
            view().onLifecycleStart();
    }

    @Override
    public void resume() {
        if (isAttached())
            view().onLifecycleResume();
    }

    @Override
    public void pause() {
        if (isAttached())
            view().onLifecyclePause();
    }

    @Override
    public void stop() {
        if (isAttached())
            view().onLifecycleStop();
    }

    @Override
    public void destroy() {
        if (isAttached())
            view().onLifecycleDestroy();
    }
}
