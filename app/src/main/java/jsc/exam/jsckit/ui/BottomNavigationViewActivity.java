package jsc.exam.jsckit.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.adapter.MyFragmentPagerAdapter;
import jsc.exam.jsckit.ui.fragment.DefaultFragment;
import jsc.kit.component.utils.CompatResourceUtils;
import jsc.kit.component.widget.DotView;
import jsc.kit.component.widget.NoScrollViewPager;

public class BottomNavigationViewActivity extends BaseActivity {

    int[] MENU_ID = {R.id.menu_home, R.id.menu_circumference, R.id.menu_navigation, R.id.menu_me};
    NoScrollViewPager noScrollViewPager;
    BottomNavigationView navigation;
    DotView[] dotViews = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_view);
//        setTitle(getClass().getSimpleName().replace("Activity", ""));
        setTitleBarTitle(R.string.menu_home);

        noScrollViewPager = findViewById(R.id.view_pager);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked())
                    return false;
                item.setChecked(true);
                setTitleBarTitle(item.getTitle());
                int index = 0;
                for (int i = 0; i < MENU_ID.length; i++) {
                    if (item.getItemId() == MENU_ID[i]) {
                        index = i;
                        break;
                    }
                }
                boolean smoothScroll = (noScrollViewPager.getCurrentItem() + 1 == index) || (noScrollViewPager.getCurrentItem() + 1 == index);
                noScrollViewPager.setCurrentItem(index, smoothScroll);
                return false;
            }
        });
        initUnReadMessageViews();
        initViewPager();
    }

    private void initViewPager() {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        noScrollViewPager.setAdapter(adapter);
        List<Fragment> fragments = new ArrayList<>(dotViews.length);
        for (int i = 0; i < dotViews.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString("content", navigation.getMenu().findItem(MENU_ID[i]).getTitle().toString());
            DefaultFragment fragment = new DefaultFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter.setFragments(fragments);
    }

    private void initUnReadMessageViews() {
        //初始化红点view
        BottomNavigationMenuView menuView = null;
        for (int i = 0; i < navigation.getChildCount(); i++) {
            View child = navigation.getChildAt(i);
            if (child instanceof BottomNavigationMenuView) {
                menuView = (BottomNavigationMenuView) child;
                break;
            }
        }
        if (menuView != null) {
            int dp8 = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_8);
            dotViews = new DotView[menuView.getChildCount()];
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView.LayoutParams params = new BottomNavigationItemView.LayoutParams(i == menuView.getChildCount() - 1 ? dp8 : dp8 * 2, 0);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = dp8 * 3;
                params.topMargin = dp8 / 2;
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                DotView dotView = new DotView(this);
                dotView.setBackgroundColor(Color.RED);
                dotView.setTextColor(Color.WHITE);
                dotView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                itemView.addView(dotView, params);
                if (i < menuView.getChildCount() - 1) {
                    dotView.setUnReadCount(new Random().nextInt(20) + 1);
                }
                dotViews[i] = dotView;
            }
        }
    }
}
