package jsc.exam.jsckit.ui.componet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.kit.stepview.RouteViewPoint;
import jsc.kit.stepview.VerticalStepView;

public class VerticalStepViewActivity extends AppCompatActivity {

    VerticalStepView verticalStepView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        verticalStepView = findViewById(R.id.step_view);
        verticalStepView.setPoints(gePoints());
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
            if (val % 3 == 0){
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
