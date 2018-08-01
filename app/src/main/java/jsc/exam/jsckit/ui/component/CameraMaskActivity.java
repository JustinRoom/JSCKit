package jsc.exam.jsckit.ui.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.io.IOException;
import java.io.InputStream;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.utils.CompatResourceUtils;
import jsc.kit.component.widget.CameraMask;

public class CameraMaskActivity extends BaseActivity {

    private CameraMask cameraMask;
    private Bitmap cameraLensBitmap;

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
        cameraMask = new CameraMask(this);
        cameraMask.setSizeRatio(.75f);
        if (cameraLensBitmap ==  null)
            cameraLensBitmap = decodeDefaultCameraLens();
        cameraMask.setCameraLensBitmap(cameraLensBitmap);
        frameLayout.addView(cameraMask, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cameraMask.setText("Put QR code inside camera lens please.");

        //
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_16);
        params.rightMargin = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_16);
        params.bottomMargin = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_32);
        params.gravity = Gravity.BOTTOM;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        frameLayout.addView(linearLayout, params);
        //
        createCameraLensChoices(linearLayout);
        createTipTextLocationChoices(linearLayout);
        createSeekBars(linearLayout);

        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

    }

    private void createCameraLensChoices(LinearLayout linearLayout){
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_camera_lens_pic:
                        cameraMask.setSizeRatio(.75f);
                        if (cameraLensBitmap ==  null)
                            cameraLensBitmap = decodeDefaultCameraLens();
                        cameraMask.setCameraLensBitmap(cameraLensBitmap);
                        break;
                    case R.id.radio_camera_lens_line:
                        cameraMask.setSizeRatio(.6f);
                        cameraMask.setCameraLensBitmap(null);
                        break;
                }
            }
        });
        linearLayout.addView(radioGroup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RadioButton button1 = new RadioButton(this);
        button1.setId(R.id.radio_camera_lens_pic);
        button1.setTextColor(Color.WHITE);
        button1.setChecked(true);
        button1.setText("PictureCameraLens");
        RadioButton button2 = new RadioButton(this);
        button2.setId(R.id.radio_camera_lens_line);
        button2.setTextColor(Color.WHITE);
        button2.setText("NormalCameraLens");
        radioGroup.addView(button1);
        radioGroup.addView(button2);
    }

    private void createTipTextLocationChoices(LinearLayout linearLayout){
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_below:
                        cameraMask.setTextLocation(CameraMask.LOCATION_BELOW_CAMERA_LENS);
                        break;
                    case R.id.radio_above:
                        cameraMask.setTextLocation(CameraMask.LOCATION_ABOVE_CAMERA_LENS);
                        break;
                }
            }
        });
        linearLayout.addView(radioGroup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RadioButton button1 = new RadioButton(this);
        button1.setId(R.id.radio_below);
        button1.setTextColor(Color.WHITE);
        button1.setChecked(true);
        button1.setText("BelowCameraLens");
        RadioButton button2 = new RadioButton(this);
        button2.setId(R.id.radio_above);
        button2.setTextColor(Color.WHITE);
        button2.setText("AboveCameraLens");
        radioGroup.addView(button1);
        radioGroup.addView(button2);
    }

    private void createSeekBars(LinearLayout linearLayout){
        //top margin seek bar
        LinearLayout.LayoutParams sbp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sbp1.topMargin = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_8);
        SeekBar seekBar1 = new SeekBar(this);
        seekBar1.setMax(200);
        linearLayout.addView(seekBar1, sbp1);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraMask.setTopMargin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //text vertical margin seek bar
        LinearLayout.LayoutParams sbp2 = new LinearLayout.LayoutParams(sbp1);
        SeekBar seekBar2 = new SeekBar(this);
        seekBar2.setMax(100);
        linearLayout.addView(seekBar2, sbp2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraMask.setTextVerticalMargin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Bitmap decodeDefaultCameraLens() {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = getResources().getAssets().open("default_camera_lens.png");
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return bitmap;
    }
}
