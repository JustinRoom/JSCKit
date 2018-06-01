package jsc.exam.jsckit.ui;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import jsc.exam.jsckit.R;

public class CircularRevealActivity extends ABaseActivity {

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
//        setContentView(R.layout.activity_circular_reveal);
//        setTitle(getClass().getSimpleName().replace("Activity", ""));
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        setContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initSharedElement();
        sendUIEmptyMessageDelay(0, 0);
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

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        FrameLayout customView = new FrameLayout(this);
        customView.setPadding(padding, 0, 0, 0);
        ActionBar.LayoutParams barParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActionBarSize());
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customView, barParams);
        //
        TextView tvTitle = new TextView(this);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvTitle.setText(getClass().getSimpleName().replace("Activity", ""));
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        customView.addView(tvTitle, titleParams);

        //
        ActionMenuView actionMenuView = new ActionMenuView(this);
//        actionMenuView.setPopupTheme(R.style.AppTheme_NoActionBar_PopupOverlay2);
        FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        customView.addView(actionMenuView, menuParams);
        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    if (imageView.isShown()){
                        imageView.setVisibility(View.GONE);
                        item.setTitle("ShowImg");
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        item.setTitle("HideImg");
                    }
                } else {
                    if (imageView.isShown()){
                        hide(item);
                    } else {
                        show(item);
                    }
                }
                return true;
            }
        });

        actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "HideImg").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }


    private boolean isRunning;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void show(final MenuItem item){
        if (isRunning)
            return;

        int cx = imageView.getWidth() / 2;
        int cy = imageView.getHeight() / 2;
        float finalRadius = (float) (Math.hypot(imageView.getWidth(), imageView.getHeight()) / 2.0);
        Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setVisibility(View.VISIBLE);
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                item.setTitle("HideImg");
                isRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(500);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hide(final MenuItem item){
        if (isRunning)
            return;

        int cx = imageView.getWidth() / 2;
        int cy = imageView.getHeight() / 2;
        float finalRadius = (float) (Math.hypot(imageView.getWidth(), imageView.getHeight()) / 2.0);
        Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, finalRadius, 0);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setVisibility(View.GONE);
                item.setTitle("ShowImg");
                isRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(500);
        anim.start();
    }
}
