package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import java.io.IOException;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.widget.CameraMask;

public class CameraMaskActivity extends BaseActivity {

    private CameraMask cameraMask;

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
        frameLayout.addView(cameraMask, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cameraMask.getTextView().setText("Put QR code inside camera lens please.");

        //
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.space_32);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.space_32);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.space_32);
        params.gravity = Gravity.BOTTOM;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        frameLayout.addView(linearLayout, params);
        //
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
        button1.setText("Below Camera Lens");
        RadioButton button2 = new RadioButton(this);
        button2.setId(R.id.radio_above);
        button2.setTextColor(Color.WHITE);
        button2.setText("Above Camera Lens");
        radioGroup.addView(button1);
        radioGroup.addView(button2);
        //top margin
        LinearLayout.LayoutParams sbp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sbp1.topMargin = getResources().getDimensionPixelSize(R.dimen.space_8);
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

        //text vertical margin
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

        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

    }
}
