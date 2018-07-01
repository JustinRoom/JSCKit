package jsc.kit.component.baseui.handler;

import android.os.Message;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface HandlerDelegate {

    /**
     * Do your ui operations here, in UI thread.
     *
     * @param msg message
     */
    public void handleUIMessage(Message msg);

    /**
     * Do your long-running operations here, in none UI thread.
     *
     * @param msg message
     */
    public void handleWorkMessage(Message msg);
}
