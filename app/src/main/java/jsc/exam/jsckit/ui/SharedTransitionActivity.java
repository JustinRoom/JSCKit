package jsc.exam.jsckit.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jsc.kit.component.baseui.transition.TransitionEnum;
import jsc.kit.component.baseui.transition.TransitionProvider;

public class SharedTransitionActivity extends BaseActivity {

    ImageView imageView;

    @Override
    public Transition createExitTransition() {
        return TransitionProvider.createFade(300L);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = new FrameLayout(this);
        setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        layout.addView(imageView, params);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCircularReveal();
            }
        });
        initSharedElement();
        handlerProvider.sendUIEmptyMessageDelay(0, 350);
    }

    private void toCircularReveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(this, CircularRevealActivity.class).putExtra("transition", TransitionEnum.SLIDE.getLabel()),
                    ActivityOptions.makeSceneTransitionAnimation(this, new Pair<View, String>(imageView, imageView.getTransitionName())).toBundle());
        } else {
            startActivity(new Intent(this, CircularRevealActivity.class));
        }
    }

    @Override
    public void handleUIMessage(Message msg) {
        super.handleUIMessage(msg);
        Glide.with(this)
                .load("file:///android_asset/img/6.jpg")
                .into(imageView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initSharedElement() {
        super.initSharedElement();
        imageView.setTransitionName("share_img");
    }
}
