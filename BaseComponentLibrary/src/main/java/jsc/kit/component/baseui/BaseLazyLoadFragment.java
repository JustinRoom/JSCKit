package jsc.kit.component.baseui;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jsc.kit.component.baseui.handler.HandlerDelegate;
import jsc.kit.component.baseui.handler.HandlerProvider;
import jsc.kit.component.baseui.permission.PermissionChecker;
import jsc.kit.component.baseui.transition.TransitionProvider;

/**
 * 懒加载fragment
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseLazyLoadFragment extends BaseFragment implements HandlerDelegate{

    public HandlerProvider handlerProvider;
    public PermissionChecker permissionChecker;

    protected abstract View createRootView(@NonNull LayoutInflater inflater, ViewGroup container);

    public abstract void initView(View rootView);

    protected void firstLazyLoad() {
    }

    protected void reLazyLoad() {
    }

    protected void resume() {
    }

    protected boolean isFirstLazyLoad = true;
    protected boolean isPrepared = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = createRootView(inflater, container);
        if (rootView == null) {
            TextView txt = new TextView(this.getActivity());
            txt.setText("Fragment");
            txt.setTextColor(0xff333333);
            txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            rootView = txt;
        }
        initView(rootView);
        initComponent();
        isPrepared = true;//初始化完成标记
        lazyLoad();//因为setUserVisibleHint(boolean isVisibleToUser)方法是在onCreateView之前调用的，所以isVisible的值总是true
        return rootView;
    }

    /**
     * Initialize components here.
     */
    @CallSuper
    public void initComponent() {
//        handlerProvider = new HandlerProvider(getClass().getSimpleName());
//        permissionChecker = new PermissionChecker();
    }

    /**
     * Destroy components here.
     */
    @CallSuper
    public void destroyComponent() {
        if (handlerProvider != null){
            handlerProvider.destroy();
            handlerProvider = null;
        }
        permissionChecker = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLazyLoad)
            resume();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;

        if (isFirstLazyLoad) {
            isFirstLazyLoad = false;
            firstLazyLoad();
        } else {
            reLazyLoad();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden || !isPrepared)
            return;

        if (isFirstLazyLoad) {
            isFirstLazyLoad = false;
            firstLazyLoad();
        } else {
            reLazyLoad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionChecker != null)
            permissionChecker.onPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void handleUIMessage(Message msg) {

    }

    @Override
    public void handleWorkMessage(Message msg) {

    }

    @Override
    public void onDestroyView() {
        destroyComponent();
        super.onDestroyView();
    }
}
