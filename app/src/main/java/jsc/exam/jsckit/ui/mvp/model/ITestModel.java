package jsc.exam.jsckit.ui.mvp.model;

import io.reactivex.Observable;
import jsc.kit.component.baseui.basemvp.IBaseModel;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 * @createTime 2018-06-06 2:29 PM Wednesday
 */
public interface ITestModel extends IBaseModel {

    Observable<String> loadVersionInfo(String baseUrl, String token, boolean showNetLog);
}
