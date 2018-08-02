package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.stepview.VerticalStepLinearLayout;
import jsc.kit.component.swiperecyclerview.VerticalStepItemDecoration;
import jsc.kit.component.utils.CompatResourceUtils;
import jsc.kit.component.utils.dynamicdrawable.DynamicDrawableFactory;

public class VerticalStepLinearLayoutActivity extends BaseActivity {

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
    VerticalStepLinearLayout stepLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        int space = CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_16);
        ScrollView lScrollView = new ScrollView(this);
        stepLinearLayout = new VerticalStepLinearLayout(this);
        stepLinearLayout.setPadding(space * 2, 0, space * 2, 0);
        stepLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable lineSpaceDrawable = DynamicDrawableFactory.cornerRectangleDrawable(Color.TRANSPARENT, 0);
        lineSpaceDrawable.setSize(-1, CompatResourceUtils.getDimensionPixelSize(this, R.dimen.space_8));
        stepLinearLayout.setDividerDrawable(lineSpaceDrawable);
//            stepLinearLayout.setDividerPadding(getResources().getDimensionPixelSize(R.dimen.space_16));
        lScrollView.addView(stepLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(lScrollView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            lScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    stepLinearLayout.updateScroll(v.getScrollY());
                }
            });
        }

        for (int i = 0; i < txts.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = space;
            TextView textView = new TextView(this);
            textView.setText(txts[i]);
            stepLinearLayout.addView(textView, params);
        }
    }

    private void initMenu() {
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST + 1:
                        stepLinearLayout.setLocation(VerticalStepLinearLayout.LEFT);
                        break;
                    case Menu.FIRST + 2:
                        stepLinearLayout.setLocation(VerticalStepLinearLayout.RIGHT);
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

        getActionMenuView().getMenu().add(10, Menu.FIRST + 1, Menu.NONE, "LEFT");
        getActionMenuView().getMenu().add(10, Menu.FIRST + 2, Menu.NONE, "RIGHT");
        getActionMenuView().getMenu().add(11, Menu.FIRST + 3, Menu.NONE, "BASE TOP");
        getActionMenuView().getMenu().add(11, Menu.FIRST + 4, Menu.NONE, "BASE FIRST");
    }
}
