package jsc.kit.component.baseui.basemvp2;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface LifecycleContract {
    public interface Model extends IBaseModel {}
    public interface View extends IBaseView {
        public void onLifecycleStart();
        public void onLifecycleResume();
        public void onLifecyclePause();
        public void onLifecycleStop();
        public void onLifecycleDestroy();
    }
    public interface Presenter {
        public void start();
        public void resume();
        public void pause();
        public void stop();
        public void destroy();
    }
}
