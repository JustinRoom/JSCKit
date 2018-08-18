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
public class TemplatePresenterImpl implements TemplateContract.Presenter {

    private WeakReference<TemplateContract.View> viewWeakReference = null;

    public TemplatePresenterImpl() {}

    public TemplatePresenterImpl(@NonNull TemplateContract.View v) {
        attach(v);
    }

    @Override
    public void attach(@NonNull TemplateContract.View v) {
        viewWeakReference = new WeakReference<>(v);
    }

    @Override
    public boolean isAttached() {
        return viewWeakReference != null && viewWeakReference.get() != null;
    }

    @Override
    public TemplateContract.View view() {
        return viewWeakReference == null ? null : viewWeakReference.get();
    }
}
