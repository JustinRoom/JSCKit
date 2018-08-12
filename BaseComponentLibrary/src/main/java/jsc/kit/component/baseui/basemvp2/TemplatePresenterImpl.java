package jsc.kit.component.baseui.basemvp2;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by Justin Qin on 8/12/2018.
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
