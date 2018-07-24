package jsc.kit.component.baseui.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public final class HandlerProvider {

    private Handler uiHandler = null;
    private Handler workHandler = null;
    private HandlerDelegate handlerDelegate;
    private String workThreadName = "handler_provider";

    public HandlerProvider(String workThreadName) {
        this(workThreadName, null);
    }

    public HandlerProvider(String workThreadName, @Nullable HandlerDelegate handlerDelegate) {
        this.workThreadName = workThreadName;
        this.handlerDelegate = handlerDelegate;
    }

    public void setHandlerDelegate(HandlerDelegate handlerDelegate) {
        this.handlerDelegate = handlerDelegate;
    }

    /**
     * @param message a message to update ui
     */
    public final void sendUIMessage(Message message) {
        createUIHandlerIfNecessary();
        uiHandler.sendMessage(message);
    }

    /**
     * @param message     a delay message to update ui
     * @param delayMillis delay
     */
    public final void sendUIMessageDelay(Message message, long delayMillis) {
        createUIHandlerIfNecessary();
        uiHandler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * @param what a message with what to update ui
     */
    public final void sendUIEmptyMessage(int what) {
        createUIHandlerIfNecessary();
        uiHandler.sendEmptyMessage(what);
    }

    /**
     * @param what        a delay message with what to update ui
     * @param delayMillis delay
     */
    public final void sendUIEmptyMessageDelay(int what, long delayMillis) {
        createUIHandlerIfNecessary();
        uiHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    public final void postUIRunnable(@NonNull Runnable r){
        createUIHandlerIfNecessary();
        uiHandler.post(r);
    }

    public final void postUIRunnableDelay(@NonNull Runnable r, long delay){
        createUIHandlerIfNecessary();
        uiHandler.postDelayed(r, delay);
    }

    /**
     * @param message message
     */
    public final void sendWorkMessage(Message message) {
        createWorkHandlerIfNecessary();
        workHandler.sendMessage(message);
    }

    /**
     * @param message     message
     * @param delayMillis delay time
     */
    public final void sendWorkMessageDelay(Message message, long delayMillis) {
        createWorkHandlerIfNecessary();
        workHandler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * @param what what
     */
    public final void sendWorkEmptyMessage(int what) {
        createWorkHandlerIfNecessary();
        workHandler.sendEmptyMessage(what);
    }

    /**
     * @param what        what
     * @param delayMillis delay time
     */
    public final void sendWorkEmptyMessageDelay(int what, long delayMillis) {
        createWorkHandlerIfNecessary();
        workHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    public final void postWorkRunnable(@NonNull Runnable r){
        createUIHandlerIfNecessary();
        uiHandler.post(r);
    }

    public final void postWorkRunnableDelay(@NonNull Runnable r, long delay){
        createUIHandlerIfNecessary();
        uiHandler.postDelayed(r, delay);
    }

    private void createUIHandlerIfNecessary() {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (handlerDelegate != null)
                        handlerDelegate.handleUIMessage(msg);
                }
            };
        }
    }

    private void createWorkHandlerIfNecessary() {
        if (workHandler == null) {
            HandlerThread handlerThread = new HandlerThread(workThreadName);
            handlerThread.start();
            workHandler = new Handler(handlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (handlerDelegate != null)
                        handlerDelegate.handleWorkMessage(msg);
                }
            };
        }
    }

    /**
     * remove all message in MessageQueue
     */
    public void destroy() {
        if (workHandler != null) {
            if (workHandler.getLooper() != null)
                workHandler.getLooper().quitSafely();
            workHandler.removeCallbacksAndMessages(null);
            workHandler = null;
        }
        if (uiHandler != null) {
//            if (uiHandler.getLooper() != null)
//                uiHandler.getLooper().quitSafely();
            uiHandler.removeCallbacksAndMessages(null);
            uiHandler = null;
        }
    }
}
