package jsc.kit.component.baseui.baseview;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface BaseViewShowDelegate {

    void onShowContentPage(@NonNull View contentView, @Nullable Bundle bundle);
    void onShowEmptyPage(@NonNull View emptyView, @Nullable Bundle bundle);
    void onShowLoadingPage(@NonNull View loadingView, @Nullable Bundle bundle);
    void onShowErrorPage(@NonNull View errorView, @Nullable Bundle bundle);

}
