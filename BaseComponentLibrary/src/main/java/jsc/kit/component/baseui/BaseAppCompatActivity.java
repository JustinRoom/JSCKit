package jsc.kit.component.baseui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import jsc.kit.component.entity.TransitionEnum;
import jsc.kit.component.widget.dialog.LoadingDialog;

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    private Handler uiHandler = null;
    private Handler workHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            if (createEnterTransition() != null)
                getWindow().setEnterTransition(createEnterTransition());
            if (createExitTransition() != null)
                getWindow().setExitTransition(createExitTransition());
            if (createReturnTransition() != null)
                getWindow().setReturnTransition(createReturnTransition());
            if (createReenterTransition() != null)
                getWindow().setReenterTransition(createReenterTransition());
        }
    }

    /**
     * @param message a message to update ui
     * @see #handleUIMessage(Message)
     */
    public final void sendUIMessage(Message message) {
        createUIHandlerIfNecessary();
        uiHandler.sendMessage(message);
    }

    /**
     * @param message     a delay message to update ui
     * @param delayMillis delay
     * @see #handleUIMessage(Message)
     */
    public final void sendUIMessageDelay(Message message, long delayMillis) {
        createUIHandlerIfNecessary();
        uiHandler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * @param what a message with what to update ui
     * @see #handleUIMessage(Message)
     */
    public final void sendUIEmptyMessage(int what) {
        createUIHandlerIfNecessary();
        uiHandler.sendEmptyMessage(what);
    }

    /**
     * @param what        a delay message with what to update ui
     * @param delayMillis delay
     * @see #handleUIMessage(Message)
     */
    public final void sendUIEmptyMessageDelay(int what, long delayMillis) {
        createUIHandlerIfNecessary();
        uiHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * @param message message
     * @see #handleWorkMessage(Message)
     */
    public final void sendWorkMessage(Message message) {
        createWorkHandlerIfNecessary();
        workHandler.sendMessage(message);
    }

    /**
     * @param message message
     * @param delayMillis delay time
     * @see #handleWorkMessage(Message)
     */
    public final void sendWorkMessageDelay(Message message, long delayMillis) {
        createWorkHandlerIfNecessary();
        workHandler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * @param what what
     * @see #handleWorkMessage(Message)
     */
    public final void sendWorkEmptyMessage(int what) {
        createWorkHandlerIfNecessary();
        workHandler.sendEmptyMessage(what);
    }

    /**
     * @param what what
     * @param delayMillis delay time
     * @see #handleWorkMessage(Message)
     */
    public final void sendWorkEmptyMessageDelay(int what, long delayMillis) {
        createWorkHandlerIfNecessary();
        workHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    private void createUIHandlerIfNecessary() {
        if (uiHandler == null) {
            uiHandler = new Handler(getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    handleUIMessage(msg);
                }
            };
        }
    }

    private void createWorkHandlerIfNecessary() {
        if (workHandler == null) {
            HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName());
            handlerThread.start();
            workHandler = new Handler(handlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    handleWorkMessage(msg);
                }
            };
        }
    }

    /**
     * Do your ui operations here, in UI thread.
     *
     * @param msg message
     */
    public abstract void handleUIMessage(Message msg);

    /**
     * Do your long-running operations here, in none UI thread.
     *
     * @param msg message
     */
    public abstract void handleWorkMessage(Message msg);


    /**
     * activity's entering transition
     * <br>This transition will be executed when jump to this activity.
     *
     * @return a transition for entering activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public abstract Transition createEnterTransition();

    /**
     * activity's exiting transition
     * <br>This transition will be executed when jump to next activity.
     *
     * @return a transition for exiting activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public abstract Transition createExitTransition();

    /**
     * activity's returning transition
     * <br>This transition will be executed when return to previous activity.
     *
     * @return a transition for returning activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public abstract Transition createReturnTransition();

    /**
     * activity's re-entering transition
     * <br>This transition will be executed when back to this activity.
     *
     * @return a transition for re-entering activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public abstract Transition createReenterTransition();

    /**
     * initialize shared_elements' transition name here.
     * <br>It should be called after calling {@link android.app.Activity#setContentView(View)} method on your own.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public abstract void initSharedElement();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public final Transition createTransition(String transition, long duration) {
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
    public final Transition createTransition(byte value, long duration) {
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
    public final Explode createExplode(long duration) {
        Explode explode = new Explode();
        explode.setDuration(duration);
        return explode;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public final Fade createFade(long duration) {
        Fade fade = new Fade();
        fade.setDuration(duration);
        return fade;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public final Slide createSlide(long duration) {
        Slide slide = new Slide(Gravity.END);
        slide.setDuration(duration);
        return slide;
    }

    @Override
    protected void onDestroy() {
        if (workHandler != null) {
            if (workHandler.getLooper() != null)
                workHandler.getLooper().quitSafely();
            workHandler.removeCallbacksAndMessages(null);
            workHandler = null;
        }
        if (uiHandler != null) {
            uiHandler.removeCallbacksAndMessages(null);
            uiHandler = null;
        }
        super.onDestroy();
    }

    /**
     * Get status bar height.
     *
     * @return the height of status bar
     */
    public final int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * Get action bar height.
     *
     * @return the height of action bar
     */
    public final int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{android.R.attr.actionBarSize};
        TypedArray array = obtainStyledAttributes(typedValue.resourceId, attribute);
        int actionBarSize = array.getDimensionPixelSize(0, 0);
        array.recycle();
        return actionBarSize;
    }

    protected Dialog createLoadingDialog(@StringRes int resId) {
        return createLoadingDialog(getString(resId));
    }

    protected Dialog createLoadingDialog() {
        return createLoadingDialog("Loading…");
    }

    protected Dialog createLoadingDialog(CharSequence txt) {
        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage(txt);
        dialog.showMessageView(!TextUtils.isEmpty(txt));
        return dialog;
    }

    public final void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public final void showToast(CharSequence txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }
}
