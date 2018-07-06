package jsc.kit.zxing.zxing.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import jsc.kit.zxing.R;
import jsc.kit.zxing.core.QRCodeView;
import jsc.kit.zxing.zxing.ZXingView;

public class ZXingFragment extends Fragment implements QRCodeView.Delegate, View.OnClickListener {

    public static String SHOW_FLASH_LIGHT = "flash_light";
    private ZXingView zXingView;
    private ImageView ivFlashLight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_z_xing_scan, container, false);
        zXingView = root.findViewById(R.id.z_xing_view);
        ivFlashLight = root.findViewById(R.id.iv_flash_light);
        zXingView.setDelegate(this);
        ivFlashLight.setOnClickListener(this);

        Uri uri = getActivity().getIntent().getData();
        assert uri != null;
        boolean showFlashLight = uri.getBooleanQueryParameter(SHOW_FLASH_LIGHT, false) && zXingView.isFlashLightAvailable();
        ivFlashLight.setVisibility(showFlashLight ? View.VISIBLE : View.GONE);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_flash_light) {
            if (zXingView.isFlashLightClosed())
                zXingView.openFlashlight();
            else
                zXingView.closeFlashlight();
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Activity.VIBRATOR_SERVICE);
        if (vibrator == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, 0x8F));
        } else {
            vibrator.vibrate(200);
        }
    }
}
