package jsc.kit.component.baseui.basemvp2;

import android.support.annotation.NonNull;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface IBasePresenter<V> {
    public void attach(@NonNull V v);
    public boolean isAttached();
    public V view();
}
