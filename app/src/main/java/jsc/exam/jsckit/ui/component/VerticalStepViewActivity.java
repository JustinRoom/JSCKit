package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.adapter.ClassItemAdapter;
import jsc.exam.jsckit.entity.ClassItem;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.stepview.RouteViewPoint;
import jsc.kit.component.stepview.VerticalStepLinearLayout;
import jsc.kit.component.stepview.VerticalStepView;
import jsc.kit.component.swiperecyclerview.VerticalStepItemDecoration;
import jsc.kit.component.utils.CompatResourceUtils;

public class VerticalStepViewActivity extends BaseActivity {

    LinearLayout layout;
    VerticalStepItemDecoration leftDecoration, rightDecoration;
    int checkType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int space = getResources().getDimensionPixelOffset(R.dimen.space_16);
        //add bottom
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setPadding(0, space / 2, 0, space / 2);
        radioGroup.setGravity(Gravity.CENTER_HORIZONTAL);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setBackgroundColor(Color.BLACK);
        layout.addView(radioGroup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_camera_lens_line:
                        checkType = 0;
                        showCustomStepView();
                        break;
                    case R.id.radio_camera_lens_pic:
                        checkType = 1;
                        showRecyclerViewStep();
                        break;
                    case R.id.radio_below:
                        checkType = 2;
                        showLinearLayoutStep();
                        break;
                }
            }
        });

        //add radio 1
        RadioGroup.LayoutParams params1 = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.leftMargin = space;
        params1.rightMargin = space;
        RadioButton radioButton1 = new RadioButton(this);
        radioButton1.setId(R.id.radio_camera_lens_line);
//        radioButton1.setChecked(true);
        radioButton1.setTextColor(Color.WHITE);
        radioButton1.setText("CStep");
        radioGroup.addView(radioButton1, params1);
        //add radio 2
        RadioGroup.LayoutParams params2 = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.leftMargin = space;
        params2.rightMargin = space;
        RadioButton radioButton2 = new RadioButton(this);
        radioButton2.setId(R.id.radio_camera_lens_pic);
        radioButton2.setTextColor(Color.WHITE);
        radioButton2.setText("RStep");
        radioGroup.addView(radioButton2, params2);
        //add radio 3
        RadioGroup.LayoutParams params3 = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.leftMargin = space;
        params3.rightMargin = space;
        RadioButton radioButton3 = new RadioButton(this);
        radioButton3.setId(R.id.radio_below);
        radioButton3.setTextColor(Color.WHITE);
        radioButton3.setText("LStep");
        radioGroup.addView(radioButton3, params3);

        radioGroup.check(R.id.radio_camera_lens_line);
        leftDecoration = new VerticalStepItemDecoration(
                this,
                getResources().getDimensionPixelOffset(R.dimen.space_16) * 2,
                getResources().getDimensionPixelOffset(R.dimen.space_8),
                getResources().getDimensionPixelOffset(R.dimen.space_16) * 2,
                0
        ).setLocation(VerticalStepItemDecoration.LEFT);
        rightDecoration = new VerticalStepItemDecoration(
                this,
                getResources().getDimensionPixelOffset(R.dimen.space_16) * 2,
                getResources().getDimensionPixelOffset(R.dimen.space_8),
                getResources().getDimensionPixelOffset(R.dimen.space_16) * 2,
                0
        ).setLocation(VerticalStepItemDecoration.RIGHT);
    }

    ScrollView cScrollView;

    private void showCustomStepView() {
        setTitleBarTitle("CustomStep");
        initMenu(false);
        if (cScrollView == null) {
            cScrollView = new ScrollView(this);
            VerticalStepView verticalStepView = new VerticalStepView(this);
            verticalStepView.setPadding(
                    getResources().getDimensionPixelOffset(R.dimen.space_16) * 2,
                    getResources().getDimensionPixelOffset(R.dimen.space_16) / 2,
                    getResources().getDimensionPixelOffset(R.dimen.space_16) * 2,
                    getResources().getDimensionPixelOffset(R.dimen.space_16) / 2
            );
            cScrollView.addView(verticalStepView, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));
            verticalStepView.setPoints(gePoints());
        }

        if (layout.getChildCount() > 1) {
            layout.removeViewAt(0);
        }
        layout.addView(cScrollView, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
    }

    RecyclerView recyclerView;


    private void showRecyclerViewStep() {
        setTitleBarTitle("RecyclerViewStep");
        initMenu(true);
        if (recyclerView == null) {
            recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(leftDecoration);
            ClassItemAdapter adapter = new ClassItemAdapter();
            List<ClassItem> items = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                items.add(new ClassItem("item" + (i + 1), null));
            }
            adapter.setItems(items);
            recyclerView.setAdapter(adapter);
        }
        if (layout.getChildCount() > 1) {
            layout.removeViewAt(0);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        params.topMargin = getResources().getDimensionPixelOffset(R.dimen.space_16) / 2;
        params.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.space_16) / 2;
        layout.addView(recyclerView, 0, params);
    }

    ScrollView lScrollView;
    VerticalStepLinearLayout stepLinearLayout;

    private void showLinearLayoutStep() {
        setTitleBarTitle("LinearLayoutStep");
        initMenu(true);
        if (lScrollView == null) {
            int space = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_16);
            String[] txts = {
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "To enable all community members to contribute and participate in the success of the Open Badges ecosystem, there are many opportunities to be involved. IMS Global Learning Consortium will be guiding the Specification development process effective January 1, 2017, and will continue to rely on the support and advocacy of the Open Badges community in the ongoing development of the Specification. See IMS Transition FAQ",
                    "Community contributions will be encouraged through a public form to ensure all inputs are heard and considered. Meeting agendas, minutes, and working group decisions will be published for viewing by anyone who opens a free account on the IMS website.",
                    "An Open Badges Community Council will bring together a vibrant and effective channel of community thought leaders and experts. The Community Council will enable all organizations and individuals, that wish to contribute, be recognized for their advancement of Open Badges via a profile published on the Open Badges website.",
                    "IMS will be establishing a process for joining the Community Council. Contact us to receive further information.",
                    "IMS members will seek input from the larger community, as described above, and maintain a transparent governance process when making formal decisions related to the Specification. In addition, an Open Badges executive board will set priorities for the development and adoption of the Open Badges Specification. IMS Contributing Members who are leaders in Open Badges issuing platforms will be invited to participate on the board.",
                    "If your organization has a stake in the future of digital credentials, learn more about IMS Global membership."
            };
            lScrollView = new ScrollView(this);
            stepLinearLayout = new VerticalStepLinearLayout(this);
            stepLinearLayout.setPadding(space * 2, 0, space * 2, 0);
            stepLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            stepLinearLayout.setDividerDrawable(CompatResourceUtils.getDrawable(this, R.drawable.linear_layout_space_line_shape));
//            stepLinearLayout.setDividerPadding(getResources().getDimensionPixelSize(R.dimen.space_16));
            lScrollView.addView(stepLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < txts.length; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = space;
                TextView textView = new TextView(this);
                textView.setText(txts[i]);
                stepLinearLayout.addView(textView, params);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        Log.i("VerticalStep", "onScrollChange:>>>>> scrollY=" + v.getScrollY());
                        if (v instanceof ViewGroup){
                            View child = ((ViewGroup) v).getChildAt(0);
                            if (child instanceof VerticalStepLinearLayout)
                                ((VerticalStepLinearLayout) child).updateScroll(v.getScrollY());
                        }
                    }
                });
            }
        }

        if (layout.getChildCount() > 1) {
            layout.removeViewAt(0);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
//            params.topMargin = getResources().getDimensionPixelOffset(R.dimen.space_16) / 2;
//            params.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.space_16) / 2;
        layout.addView(lScrollView, 0, params);
    }

    private void initMenu(boolean show) {
        if (!show) {
            getActionMenuView().getMenu().clear();
            return;
        }

        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 1:
                        if (checkType == 1) {
                            recyclerView.removeItemDecoration(leftDecoration);
                            recyclerView.removeItemDecoration(rightDecoration);
                            recyclerView.addItemDecoration(leftDecoration);
                        } else if (checkType == 2) {
                            stepLinearLayout.setLocation(VerticalStepLinearLayout.LEFT);
                        }
                        break;
                    case Menu.FIRST + 2:
                        if (checkType == 1) {
                            recyclerView.removeItemDecoration(leftDecoration);
                            recyclerView.removeItemDecoration(rightDecoration);
                            recyclerView.addItemDecoration(rightDecoration);
                        } else if (checkType == 2){
                            stepLinearLayout.setLocation(VerticalStepLinearLayout.RIGHT);
                        }
                        break;
                    case Menu.FIRST + 3:
                        stepLinearLayout.setSortBase(VerticalStepLinearLayout.SORT_BASE_TOP);
                        break;
                    case Menu.FIRST + 4:
                        stepLinearLayout.setSortBase(VerticalStepLinearLayout.SORT_BASE_FIRST);
                        break;
                }
                return true;
            }
        });

        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "LEFT");
        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "RIGHT");
        if (checkType == 2){
            getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 3, Menu.NONE, "BASE TOP");
            getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 4, Menu.NONE, "BASE FIRST");
        } else {
            getActionMenuView().getMenu().removeItem(Menu.FIRST + 3);
            getActionMenuView().getMenu().removeItem(Menu.FIRST + 4);

        }
//        SubMenu subMenu = getActionMenuView().getMenu().addSubMenu(Menu.NONE, Menu.FIRST + 11, Menu.NONE, "View");
//        subMenu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "LEFT");
//        subMenu.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "TextView");
    }

    private List<RouteViewPoint> gePoints() {
        Random random = new Random();
        float txt10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        List<RouteViewPoint> points = new ArrayList<>();
        int index = 0;
        RouteViewPoint start = getBasePoint();
        start.setIndex(String.valueOf(++index));
        start.setKey(0);
        start.setLabel("起点");
        start.setLabelColor(0xFF999999);
        points.add(start);

        int num = random.nextInt(5) + 8;
        for (int i = 0; i < num; i++) {
            int val = random.nextInt(100);
            RouteViewPoint center = getBasePoint();
            if (val % 3 == 0) {
                center.setDistance(80);
                center.setKey(101);
                center.setRadius(10);
                center.setLabelColor(0xFF999999);
                center.setLabelSize(txt10);
                center.setLabel("步行" + (val * 15) + "米");
            } else {
                center.setIndex(String.valueOf(++index));
                center.setKey(100);
                center.setLabel("公交站" + val);
            }
            points.add(center);
        }

        num = new Random().nextInt(5) + 5;
        for (int k = 0; k < num; k++) {
            RouteViewPoint stopPoint = getBasePoint();
            stopPoint.setDistance(50);
            stopPoint.setKey(101);
            stopPoint.setRadius(10);
            stopPoint.setBackgroundColor(0xFF00BA86);
            stopPoint.setBorderColor(0xFF00BA86);
            stopPoint.setLabelColor(0xFF999999);
            stopPoint.setLabel("•••");
            stopPoint.setLabelSize(txt10);
            points.add(stopPoint);
        }

        RouteViewPoint end = getBasePoint();
        end.setIndex(String.valueOf(++index));
        end.setKey(1);
        end.setLabel("终点");
        end.setLabelColor(0xFF999999);
        points.add(end);
        return points;
    }

    private RouteViewPoint getBasePoint() {
        float textSize10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        float textSize12 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        RouteViewPoint p = new RouteViewPoint();
        p.setDistance(120);
        p.setRadius(20);
        p.setBackgroundColor(0xFFCCCCCC);
        p.setBorderColor(0xFFCCCCCC);
        p.setIndexSize(textSize10);
        p.setCursorSize(textSize12);
        p.setCursorColor(0xFFFF00FF);
        p.setTransitSize(textSize12);
        p.setTransitColor(0xFF999999);
        p.setLabelSize(textSize12);
        return p;
    }
}
