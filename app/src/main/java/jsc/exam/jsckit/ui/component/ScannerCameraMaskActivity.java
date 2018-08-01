package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.utils.CompatResourceUtils;
import jsc.kit.component.widget.ScannerCameraMask;

public class ScannerCameraMaskActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        setContentView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.beauty_bg);
        frameLayout.addView(imageView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //
        ScannerCameraMask scannerCameraMask = new ScannerCameraMask(this);
        frameLayout.addView(scannerCameraMask, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scannerCameraMask.setText("Put QR code inside camera lens please.");
        scannerCameraMask.setTextVerticalMargin(CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_16));
        scannerCameraMask.setTopMargin(CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_32));

        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

    }
}
