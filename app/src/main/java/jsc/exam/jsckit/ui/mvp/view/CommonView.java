package jsc.exam.jsckit.ui.mvp.view;

import android.app.Dialog;

import jsc.kit.component.baseui.IBaseView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 * @createTime 2018-06-06 2:22 PM Wednesday
 */
public interface CommonView extends IBaseView{

    /**
     * Show log of network requesting or not.
     * @return show if {@code true}, else not.
     */
    boolean isShowNetLog();

    /**
     * Get the base url of network requesting.
     * @return base url
     */
    String getBaseUrl();

    /**
     * Get network requesting token
     * @return token
     */
    String getToken();

    /**
     * Get the current user id
     * @return the current user id
     */
    String getCurrentUserId();

    /**
     * Create loading dialog.
     * @return loading dialog instance
     */
    Dialog getLoadingDialog();
}
