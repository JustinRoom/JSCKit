package jsc.kit.component.baseui.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.Window;

/**
 * @author jiangshicheng
 */
public class TransitionProvider {

    private TransitionDelegate transitionDelegate;

    public TransitionProvider() {
    }

    public TransitionProvider(TransitionDelegate transitionDelegate) {
        this.transitionDelegate = transitionDelegate;
    }

    public void setTransitionDelegate(TransitionDelegate transitionDelegate) {
        this.transitionDelegate = transitionDelegate;
    }

    public void provide(@NonNull Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(transitionDelegate == null ? null : transitionDelegate.createEnterTransition());
            window.setExitTransition(transitionDelegate == null ? null : transitionDelegate.createExitTransition());
            window.setReturnTransition(transitionDelegate == null ? null : transitionDelegate.createReturnTransition());
            window.setReenterTransition(transitionDelegate == null ? null : transitionDelegate.createReenterTransition());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Transition createTransition(String transition, long duration) {
        TransitionEnum transitionEnum = TransitionEnum.createTransitionByLabel(transition);
        if (transitionEnum == null)
            return null;
        switch (transitionEnum) {
            case FADE:
                return createFade(duration);
            case SLIDE:
                return createSlide(duration);
            case EXPLODE:
                return createExplode(duration);
            default:
                return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Transition createTransition(byte value, long duration) {
        TransitionEnum transitionEnum = TransitionEnum.createTransitionByValue(value);
        if (transitionEnum == null)
            return null;
        switch (transitionEnum) {
            case FADE:
                return createFade(duration);
            case SLIDE:
                return createSlide(duration);
            case EXPLODE:
                return createExplode(duration);
            default:
                return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Explode createExplode(long duration) {
        Explode explode = new Explode();
        explode.setDuration(duration);
        return explode;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Fade createFade(long duration) {
        Fade fade = new Fade();
        fade.setDuration(duration);
        return fade;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Slide createSlide(long duration) {
        Slide slide = new Slide(Gravity.END);
        slide.setDuration(duration);
        return slide;
    }
}
