package jsc.lib.zxinglibrary.zxing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import jsc.lib.zxinglibrary.R;
import jsc.lib.zxinglibrary.core.QRCodeView;
import jsc.lib.zxinglibrary.zxing.ZXingView;

public class ZXingFragment extends Fragment implements QRCodeView.Delegate {

    private ZXingView zXingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_z_xing_scan, container, false);
        zXingView = root.findViewById(R.id.z_xing_view);
        zXingView.setDelegate(this);
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        zXingView.startSpot();
    }

    @Override
    public void onStop() {
        zXingView.stopCamera();
        super.onStop();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Intent intent = new Intent();
        intent.putExtra("result", result);
        Activity activity = getActivity();
        assert activity != null : "Make sure that the fragment was attached.";
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Activity.VIBRATOR_SERVICE);
        assert vibrator != null;
        vibrator.vibrate(200);
    }
}
