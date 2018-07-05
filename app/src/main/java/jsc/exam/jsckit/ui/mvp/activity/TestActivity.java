package jsc.exam.jsckit.ui.mvp.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.BuildConfig;
import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.mvp.model.TestModel;
import jsc.exam.jsckit.ui.mvp.presenter.TestPresenter;
import jsc.exam.jsckit.ui.mvp.view.CommonView;
import jsc.exam.jsckit.ui.mvp.view.ITestView;
import jsc.kit.component.baseui.basemvp.BaseMVPActivity;
import jsc.kit.component.utils.AntiShakeUtils;
import jsc.kit.component.utils.WindowUtils;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class TestActivity extends BaseMVPActivity implements ITestView, CommonView {

    private TextView textView;
    private TestPresenter testPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        textView = new TextView(this);
        layout.addView(textView);

        Button button = new Button(this);
        button.setText("AntiShake");
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AntiShakeUtils.isInvalidClick(v, 1500)) {
                    showToast("InvalidClick");
                    return;
                }
                showToast("AntiShake");
            }
        });

        setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TestActivity", "onClick: ");
            }
        });

        testPresenter = new TestPresenter(this, new TestModel());
        testPresenter.setCommonView(this);
        addToPresenterManager(testPresenter);
        handlerProvider.sendUIEmptyMessageDelay(0, 350L);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        FrameLayout customView = new FrameLayout(this);
//        customView.setPadding(padding, 0, padding, 0);
        ActionBar.LayoutParams barParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowUtils.getActionBarSize(this));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customView, barParams);
        //添加标题
        TextView tvTitle = new TextView(this);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setText(getClass().getSimpleName().replace("Activity", ""));
        customView.addView(tvTitle, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //添加返回按钮
        ImageView ivBack = new ImageView(this);
        ivBack.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivBack.setImageResource(R.drawable.ic_chevron_left_white_24dp);
        ivBack.setPadding(padding / 2, 0, padding / 2, 0);
        customView.addView(ivBack, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //添加menu菜单
        ActionMenuView actionMenuView = new ActionMenuView(this);
        FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        customView.addView(actionMenuView, menuParams);
    }


    @Override
    public void handleUIMessage(Message msg) {
        super.handleUIMessage(msg);
        testPresenter.loadVersionInfo();
    }

    @Override
    public boolean isShowNetLog() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getBaseUrl() {
        return "https://raw.githubusercontent.com/";
    }

    @Override
    public String getToken() {
        return "54s5dfsd1fpasjfshlasdfhsan";
    }

    @Override
    public String getCurrentUserId() {
        return "20185612";
    }

    @Override
    public Dialog getLoadingDialog() {
        return createLoadingDialog();
    }

    @Override
    public void onLoadVersionInfo(String result) {
        if (!TextUtils.isEmpty(result)) {
            textView.setText(result);
        } else {
            textView.setText("");
        }
    }
}
