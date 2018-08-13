package jsc.kit.component.baseui.basemvp2;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.transition.Transition;

import jsc.kit.component.baseui.BaseAppCompatActivity;

/**
 * Created by Justin Qin on 8/12/2018.
 */

public abstract class LifecycleActivity extends BaseAppCompatActivity implements LifecycleContract.View{

    private LifecycleContract.Presenter presenter = new LifecyclePresenterImpl();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.attach(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    protected void onPause() {
        presenter.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void handleUIMessage(Message msg) {

    }

    @Override
    public void handleWorkMessage(Message msg) {

    }

    @Override
    public Transition createEnterTransition() {
        return null;
    }

    @Override
    public Transition createExitTransition() {
        return null;
    }

    @Override
    public Transition createReturnTransition() {
        return null;
    }

    @Override
    public Transition createReenterTransition() {
        return null;
    }

    @Override
    public void initSharedElement() {

    }

    @Override
    public void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus) {

    }

    @Override
    public void onDownloadCompleted(Uri uri) {

    }

    @Override
    public void onLifecycleStart() {

    }

    @Override
    public void onLifecycleResume() {

    }

    @Override
    public void onLifecyclePause() {

    }

    @Override
    public void onLifecycleStop() {

    }

    @Override
    public void onLifecycleDestroy() {

    }
}
