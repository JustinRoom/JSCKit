package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.adapter.ClassItemAdapter;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.itemlayout.JSCItemLayout;
import jsc.kit.component.reboundlayout.ReboundRecyclerView;
import jsc.kit.component.swiperecyclerview.BlankSpaceItemDecoration;
import jsc.kit.component.swiperecyclerview.OnCreateViewHolderDelegate;
import jsc.kit.component.swiperecyclerview.OnItemClickListener;
import jsc.kit.component.utils.CompatResourceUtils;

public class ReboundRecyclerViewActivity extends BaseActivity {

    ClassItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
        ReboundRecyclerView reboundRecyclerView = new ReboundRecyclerView(this);
        reboundRecyclerView.setMaxReboundAnimDuration(2000);
        RecyclerView recyclerView = reboundRecyclerView.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new BlankSpaceItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.space_16),
                getResources().getDimensionPixelOffset(R.dimen.space_4),
                getResources().getDimensionPixelOffset(R.dimen.space_16),
                0
        ));
        adapter = new ClassItemAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener<ClassItem>() {
            @Override
            public void onItemClick(View itemView, int position, ClassItem item) {
                showCustomToast(item.getLabel());
            }
        });
        recyclerView.setAdapter(adapter);
        setContentView(reboundRecyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        List<ClassItem> items = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            items.add(new ClassItem("Item " + (i < 10 ? "0" + i : "" + i), null));
        }
        adapter.setItems(items);
    }

    private void initMenu() {
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 10:
                        adapter.removeItem(3);
                        break;
                    case Menu.FIRST + 11:
                        adapter.removeItem(9);
                        break;
                    case Menu.FIRST + 12:
                        adapter.removeItem(24);

                        break;
                    case Menu.FIRST + 20:
                        break;
                    case Menu.FIRST + 21:
                        break;
                    case Menu.FIRST + 22:

                        break;
                }
                return true;
            }
        });

        Menu menu = getActionMenuView().getMenu();

        SubMenu subMenu1 = menu.addSubMenu(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Remove");
        subMenu1.add(0x60, Menu.FIRST + 10, Menu.NONE, "remove top").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        subMenu1.add(0x60, Menu.FIRST + 11, Menu.NONE, "remove center");
        subMenu1.add(0x60, Menu.FIRST + 12, Menu.NONE, "remove bottom");

        SubMenu subMenu2 = menu.addSubMenu(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "Add");
        subMenu2.add(0x70, Menu.FIRST + 20, Menu.NONE, "add top");
        subMenu2.add(0x70, Menu.FIRST + 21, Menu.NONE, "add center");
        subMenu2.add(0x70, Menu.FIRST + 22, Menu.NONE, "add bottom");
    }
}
