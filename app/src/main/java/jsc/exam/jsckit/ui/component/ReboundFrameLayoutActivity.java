package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.ABaseActivity;
import jsc.kit.component.itemlayout.JSCItemLayout;
import jsc.kit.component.reboundlayout.ReboundFrameLayout;

public class ReboundFrameLayoutActivity extends ABaseActivity implements View.OnClickListener {

    ActionMenuView actionMenuView;
    ReboundFrameLayout reboundFrameLayout;
    int viewType = -1;
    int subViewType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        showView(1);
    }

    private void showView(int viewType) {
        if (this.viewType == viewType)
            return;

        this.viewType = viewType;
        if (this.viewType == 1){
            setContentView(R.layout.activity_rebound_layout);
            setTitle(getClass().getSimpleName().replace("Activity", ""));
            actionMenuView.getMenu().removeItem(Menu.FIRST + 1);
            actionMenuView.getMenu().removeItem(Menu.FIRST + 2);
        } else {
            createReboundFrameLayoutIfNecessary();
            setContentView(reboundFrameLayout);
            actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "ListView");
            actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "TextView");
        }
    }

    private void showSubView(int subViewType) {
        if (reboundFrameLayout == null || this.subViewType == subViewType)
            return;

        if (this.subViewType != -1)
            reboundFrameLayout.removeViewAt(0);
        this.subViewType = subViewType;
        if (subViewType == 1)
            reboundFrameLayout.addView(getListView(), 0, new ReboundFrameLayout.LayoutParams(ReboundFrameLayout.LayoutParams.MATCH_PARENT, ReboundFrameLayout.LayoutParams.WRAP_CONTENT));
        else
            reboundFrameLayout.addView(getTextView(), 0, new ReboundFrameLayout.LayoutParams(ReboundFrameLayout.LayoutParams.MATCH_PARENT, ReboundFrameLayout.LayoutParams.WRAP_CONTENT));
    }

    private void createReboundFrameLayoutIfNecessary(){
        if (reboundFrameLayout == null) {
            reboundFrameLayout = new ReboundFrameLayout(this);
            //设置滑动比率, 默认为0.65f
//        reboundFrameLayout.setSlidingScaleRatio(0.55f);
            //设置回弹动画时间，默认为300
            reboundFrameLayout.setReboundAnimationDuration(1200);

            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setImageResource(R.mipmap.ic_launcher_round);
            ReboundFrameLayout.LayoutParams params = new ReboundFrameLayout.LayoutParams(ReboundFrameLayout.LayoutParams.WRAP_CONTENT, ReboundFrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            reboundFrameLayout.addView(imageView, params);
        }
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
        actionMenuView = new ActionMenuView(this);
//        actionMenuView.setPopupTheme(R.style.AppTheme_NoActionBar_PopupOverlay2);
        FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        customView.addView(actionMenuView, menuParams);
        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 10:
                        showView(1);
                        break;
                    case Menu.FIRST + 11:
                        showView(2);
                        showSubView(1);
                        break;
                    case Menu.FIRST + 1:
                        showSubView(1);
                        break;
                    case Menu.FIRST + 2:
                        showSubView(2);
                        break;
                }
                return true;
            }
        });

        actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 10, Menu.NONE, "Xml").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        actionMenuView.getMenu().add(Menu.NONE, Menu.FIRST + 11, Menu.NONE, "View").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private View getListView() {
        LinearLayout itemContainer = new LinearLayout(this);
        itemContainer.setOrientation(LinearLayout.VERTICAL);
        int margin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height);
        for (int i = 0; i < 15; i++) {
            JSCItemLayout itemLayout = new JSCItemLayout(this);
            itemLayout.setPadding(margin, 0, margin, 0);
            itemLayout.setBackgroundResource(R.drawable.ripple_round_corner_white_r4);
            itemLayout.setLabel("MenuItem" + i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            params.leftMargin = margin;
            params.rightMargin = margin;
            params.topMargin = margin / 8;
            params.bottomMargin = margin / 8;
            itemContainer.addView(itemLayout, params);
            itemLayout.setOnClickListener(this);
        }
        return itemContainer;
    }

    private View getTextView() {
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        TextView textView = new TextView(this);
        textView.setPadding(padding, 0, padding, 0);
        textView.setText(R.string.article);
        return textView;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof JSCItemLayout)
            showCustomToast(((JSCItemLayout) v).getLabel());
    }
}
