package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;

public class RippleViewActivity extends BaseActivity {

    final String TAG = getClass().getSimpleName();
//    RippleView rippleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

//        rippleView = findViewById(R.id.ripple_view);
//        rippleView.setColor(Color.CYAN);
//        rippleView.setMinRadius(50.0f);
//        rippleView.setRepeatCount(-1);
//        rippleView.setAutoRunOnAttached(true);
//        rippleView.setAnimListener(new RippleView.AnimListener() {
//            @Override
//            public void onAnimationStart() {
//                Log.i(TAG, "onAnimationStart: ");
//            }
//
//            @Override
//            public void onAnimationRepeat(int repeatIndex) {
//                Log.i(TAG, "onAnimationRepeat: repeatIndex = " + repeatIndex);
//            }
//
//            @Override
//            public void onAnimationStop() {
//                Log.i(TAG, "onAnimationStop: ");
//            }
//        });
    }

    public void widgetClick(View v){
//        rippleView.start();
    }
}
