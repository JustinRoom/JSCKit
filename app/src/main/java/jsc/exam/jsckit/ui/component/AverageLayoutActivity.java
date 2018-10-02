package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.widget.AverageLayout;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class AverageLayoutActivity extends BaseActivity {

    TextView tvInfo;
    AverageLayout averageLayout;
    LinearLayout.LayoutParams params;
    String[] infos = new String[3];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_layout);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        initMenu();

        tvInfo = findViewById(R.id.tv_info);
        averageLayout = findViewById(R.id.average_layout);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        infos[0] = "layout_width：wrap_content";
        infos[1] = "layout_height：match_parent";
        infos[2] = "orientation：horizontal";
        tvInfo.setText(
                new StringBuilder()
                        .append(infos[0]).append("\n")
                        .append(infos[1]).append("\n")
                        .append(infos[2]).toString()
        );
    }

    private void initMenu() {
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean updateInfo = true;
                switch (item.getItemId()) {
                    case Menu.FIRST + 11:
                        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        averageLayout.setLayoutParams(params);
                        infos[0] = "layout_width：wrap_content";
                        break;
                    case Menu.FIRST + 12:
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        averageLayout.setLayoutParams(params);
                        infos[0] = "layout_width：match_parent";
                        break;

                    case Menu.FIRST + 21:
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        averageLayout.setLayoutParams(params);
                        infos[1] = "layout_height：wrap_content";
                        break;
                    case Menu.FIRST + 22:
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        averageLayout.setLayoutParams(params);
                        infos[1] = "layout_height：match_parent";
                        break;

                    case Menu.FIRST + 31:
                        averageLayout.setOrientation(AverageLayout.HORIZONTAL);
                        infos[2] = "orientation：horizontal";
                        break;
                    case Menu.FIRST + 32:
                        averageLayout.setOrientation(AverageLayout.VERTICAL);
                        infos[2] = "orientation：vertical";
                        break;
                    default:
                        updateInfo = false;
                        break;
                }

                if (updateInfo)
                    tvInfo.setText(
                            new StringBuilder()
                                    .append(infos[0]).append("\n")
                                    .append(infos[1]).append("\n")
                                    .append(infos[2]).toString()
                    );
                return true;
            }
        });

        SubMenu subMenu1 = getActionMenuView().getMenu().addSubMenu(10, Menu.FIRST + 10, Menu.NONE, "WIDTH");//.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        subMenu1.add(10, Menu.FIRST + 11, Menu.NONE, "wrap_content");
        subMenu1.add(10, Menu.FIRST + 12, Menu.NONE, "match_parent");

        SubMenu subMenu2 = getActionMenuView().getMenu().addSubMenu(20, Menu.FIRST + 20, Menu.NONE, "HEIGHT");
        subMenu2.add(20, Menu.FIRST + 21, Menu.NONE, "wrap_content");
        subMenu2.add(20, Menu.FIRST + 22, Menu.NONE, "match_parent");

        SubMenu subMenu3 = getActionMenuView().getMenu().addSubMenu(30, Menu.FIRST + 30, Menu.NONE, "ORIENTATION");
        subMenu3.add(30, Menu.FIRST + 31, Menu.NONE, "horizontal");
        subMenu3.add(30, Menu.FIRST + 32, Menu.NONE, "vertical");
    }
}
