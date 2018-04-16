package jsc.lib.retrofitlibrary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public abstract class LoadingDialogObserver<T> implements Observer<T>, DialogInterface.OnCancelListener {

    private final int SHOW_DIALOG = 0x6990;
    private final int HIDE_DIALOG = 0x6991;
    private Dialog loadingDialog;
    private boolean ifShowDialog;
    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SHOW_DIALOG:
                    if (loadingDialog != null && !loadingDialog.isShowing())
                        loadingDialog.show();
                    break;
                case HIDE_DIALOG:
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    break;
            }
            return true;
        }
    });
    private Disposable disposable;

    /**
     * Constructor.
     */
    public LoadingDialogObserver() {
        this(null);
    }

    /**
     * Constructor.
     */
    public LoadingDialogObserver(Dialog loadingDialog) {
        this(loadingDialog, true);
    }

    /**
     * Constructor.
     *
     * @param loadingDialog
     * @param ifShowDialog  Show loadingDialog if true else not.
     */
    public LoadingDialogObserver(Dialog loadingDialog, boolean ifShowDialog) {
        this.loadingDialog = loadingDialog;
        this.ifShowDialog = ifShowDialog;
        if (loadingDialog != null)
            loadingDialog.setOnCancelListener(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if (ifShowDialog)
            handler.sendEmptyMessage(SHOW_DIALOG);
        onNetStart(d);
    }

    @Override
    public void onError(Throwable e) {
        if (ifShowDialog)
            handler.sendEmptyMessage(HIDE_DIALOG);
        onNetError(e);
        onNetFinish(disposable);
    }

    @Override
    public void onComplete() {
        if (ifShowDialog)
            handler.sendEmptyMessage(HIDE_DIALOG);
        onNetFinish(disposable);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        disposable.dispose();
        onNetFinish(disposable);
    }

    public abstract void onNetStart(Disposable disposable);

    public abstract void onNetError(Throwable e);

    public abstract void onNetFinish(Disposable disposable);
}