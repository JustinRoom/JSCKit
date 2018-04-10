package jsc.exam.jsckit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import jsc.kit.rippleview.RippleView;

public class RippleViewActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();
    RippleView rippleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_view);

        rippleView = findViewById(R.id.ripple_view);
//        rippleView.setRepeatCount(5);
        rippleView.setAnimListener(new RippleView.AnimListener() {
            @Override
            public void onAnimationStart() {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationRepeat(int repeatIndex) {
                Log.i(TAG, "onAnimationRepeat: repeatIndex = " + repeatIndex);
            }

            @Override
            public void onAnimationStop() {
                Log.i(TAG, "onAnimationStop: ");
            }
        });
    }

    public void widgetClick(View v){
        rippleView.start();
    }
}
