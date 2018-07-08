package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.itemlayout.JSCItemLayout;
import jsc.kit.component.reboundlayout.ReboundFrameLayout;

public class ReboundFrameLayoutActivity extends BaseActivity implements View.OnClickListener {

    ReboundFrameLayout reboundFrameLayout;
    int viewType = -1;
    int subViewType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showView(1);
        initMenu();
    }

    private void showView(int viewType) {
        if (this.viewType == viewType)
            return;

        this.viewType = viewType;
        if (this.viewType == 1){
            setContentView(R.layout.activity_rebound_layout);
        } else {
            createReboundFrameLayoutIfNecessary();
            setContentView(reboundFrameLayout);
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
//        reboundFrameLayout.setScrollRatio(0.55f);
            //设置回弹动画时间，默认为300
            reboundFrameLayout.setMaxReboundAnimDuration(1200);
        }
    }

    private void initMenu() {
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 10:
                        showView(1);
                        break;
                    case Menu.FIRST + 1:
                        showView(2);
                        showSubView(1);
                        break;
                    case Menu.FIRST + 2:
                        showView(2);
                        showSubView(2);
                        break;
                }
                return true;
            }
        });

        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 10, Menu.NONE, "Xml");
        SubMenu subMenu = getActionMenuView().getMenu().addSubMenu(Menu.NONE, Menu.FIRST + 11, Menu.NONE, "View");
        subMenu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "ListView");
        subMenu.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "TextView");
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
