package jsc.kit.component.baseui.resizable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jsc.kit.component.baseui.BaseFragment;

/**
 * 大小屏切换架构类。
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public abstract class BaseResizableFragment extends BaseFragment implements OnScreenChangeListener {

    public static final int FLAG_FULL_SCREEN = 0x1;//大屏视图标识
    public static final int FLAG_SMALL_SCREEN = 0x2;//小屏视图标识
    public static final int FLAG_SAME_SCREEN = 0x3;//大小同屏
    @IntDef({FLAG_FULL_SCREEN, FLAG_SMALL_SCREEN, FLAG_SAME_SCREEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenModel {}
    private int screenModel = 0;
    private boolean switchScreenEnable;

    FrameLayout rootView;

    public void initConfiguration() {
        setScreenModel(isFullScreen() ? FLAG_FULL_SCREEN : FLAG_FULL_SCREEN);
        setSwitchScreenEnable(true);
    }

    public int getScreenModel() {
        return screenModel;
    }

    public void setScreenModel(@ScreenModel int model) {
        this.screenModel = model;
    }

    public boolean isSwitchScreenEnable() {
        return switchScreenEnable;
    }

    public void setSwitchScreenEnable(boolean switchScreenEnable) {
        this.switchScreenEnable = switchScreenEnable;
    }

    /**
     * 如果你只想显示大屏或者小屏视图，请重写此方法。
     * 默认显示大屏视图。
     * @return true，大屏；false，小屏。
     */
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * {@link #FLAG_SAME_SCREEN} 模式下大小屏切换监听。
     * <br>比如说在这里切换{@link android.support.v7.widget.RecyclerView.Adapter}适配器等。
     * @param isFullScreen isFullScreen
     */
    protected void onSwitchWhenInSameScreenModel(boolean isFullScreen){}

    /**
     * 创建大小同屏视图。
     * <br>创建视图的两种方式:
     * <ul>
     *     <li>1、纯代码创建视图</li>
     *     <li>2、调用{@link #attachLayout(ViewGroup, int)}绑定布局文件方式创建视图</li>
     * </ul>
     * @param rootView rootView
     */
    public abstract void createSameScreen(FrameLayout rootView);

    /**
     * 创建小屏视图。
     * <br>创建视图的两种方式:
     * <ul>
     *     <li>1、纯代码创建视图</li>
     *     <li>2、调用{@link #attachLayout(ViewGroup, int)}绑定布局文件方式创建视图</li>
     * </ul>
     * @param rootView rootView
     */
    public abstract void createSmallScreen(FrameLayout rootView);

    /**
     * 创建大屏视图。
     * <br>创建视图的两种方式:
     * <ul>
     *     <li>1、纯代码创建视图</li>
     *     <li>2、调用{@link #attachLayout(ViewGroup, int)}绑定布局文件方式创建视图</li>
     * </ul>
     * @param rootView rootView
     */
    public abstract void createFullScreen(FrameLayout rootView);

    /**
     * 初始化视图。
     * @param isFullScreen true,初始化全屏视图；false，初始化小屏视图。你可以根据这个字段针对大小屏做特殊处理。
     *                     通常情况下，大小屏初始化视图是一样的。
     */
    public abstract void initView(boolean isFullScreen);

    /**
     * 加载并缓存数据。
     * 在加载完数据后请主动调用{@link #bindCacheData(boolean)}方法，因为网络数据会有延时性。
     */
    public abstract void loadAndCacheData();

    /**
     * 给视图绑定数据
     */
    public abstract void bindCacheData(boolean isFullScreen);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initConfiguration();
        if (isSwitchScreenEnable()){
            // TODO: 2018/9/13 在这里注册大小屏切换监听
        }
    }

    @Override
    public void onDestroy() {
        if (isSwitchScreenEnable() ){
            // TODO: 2018/9/13 在这里注销大小屏切换监听
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new FrameLayout(inflater.getContext());
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadSuitableLayout();
        initView(isFullScreen());
        loadAndCacheData();
        return rootView;
    }

    public <T extends View> T findViewById(@IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }

    /**
     * 绑定视图布局文件。
     * 通常在{@link #createSmallScreen(FrameLayout)}或者{@link #createFullScreen(FrameLayout)}方法中调用。
     *
     * @param rootView rootView
     * @param layoutId layoutId
     * @param <T> rootView类型
     */
    public <T extends ViewGroup> void attachLayout(T rootView, @LayoutRes int layoutId){
        getLayoutInflater().inflate(layoutId, rootView, true);
    }

    private void loadSuitableLayout(){
        rootView.removeAllViews();
        switch (screenModel){
            case FLAG_SAME_SCREEN:
                createSameScreen(rootView);
                break;
            case FLAG_FULL_SCREEN:
                createFullScreen(rootView);
                break;
            case FLAG_SMALL_SCREEN:
                createSmallScreen(rootView);
                break;
        }
    }

    @Override
    public void onScreenChange(boolean fullScreen) {
        if (screenModel == FLAG_SAME_SCREEN){
            onSwitchWhenInSameScreenModel(fullScreen);
        } else {
            screenModel = fullScreen ? FLAG_FULL_SCREEN : FLAG_SMALL_SCREEN;
            loadSuitableLayout();
            initView(fullScreen);
        }
        bindCacheData(fullScreen);
    }
}
