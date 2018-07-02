package jsc.kit.component.baseui.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;
import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface TransitionDelegate {

    /**
     * activity's entering transition
     * <br>This transition will be executed when jump to this activity.
     *
     * @return a transition for entering activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Transition createEnterTransition();

    /**
     * activity's exiting transition
     * <br>This transition will be executed when jump to next activity.
     *
     * @return a transition for exiting activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Transition createExitTransition();

    /**
     * activity's returning transition
     * <br>This transition will be executed when return to previous activity.
     *
     * @return a transition for returning activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Transition createReturnTransition();

    /**
     * activity's re-entering transition
     * <br>This transition will be executed when back to this activity.
     *
     * @return a transition for re-entering activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Transition createReenterTransition();

    /**
     * initialize shared_elements' transition name here.
     * <br>It should be called after calling {@link android.app.Activity#setContentView(View)} method on your own.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initSharedElement();
}
