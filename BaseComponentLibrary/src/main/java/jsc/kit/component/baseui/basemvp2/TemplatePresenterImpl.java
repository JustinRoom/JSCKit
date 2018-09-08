package jsc.kit.component.baseui.basemvp2;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class TemplatePresenterImpl implements TemplateContract.Presenter {


    @Override
    public void attachModel(TemplateContract.Model model) {

    }

    @Override
    public boolean isModelAttached() {
        return false;
    }

    @Override
    public TemplateContract.Model model() {
        return null;
    }

    @Override
    public void attachView(TemplateContract.View view) {

    }

    @Override
    public boolean isViewAttached() {
        return false;
    }

    @Override
    public TemplateContract.View view() {
        return null;
    }
}
