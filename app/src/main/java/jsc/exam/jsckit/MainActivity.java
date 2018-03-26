package jsc.exam.jsckit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.cl_arc_header_view:
                startActivity(new Intent(this, ArcHeaderViewActivity.class));
                break;
            case R.id.cl_banner_view:
                startActivity(new Intent(this, BannerViewActivity.class));
                break;
            case R.id.cl_month_view:
                startActivity(new Intent(this, MonthViewActivity.class));
                break;
            case R.id.cl_rebound_layout:
                startActivity(new Intent(this, ReboundLayoutActivity.class));
                break;
            case R.id.cl_vertical_step_view:
                startActivity(new Intent(this, StepViewActivity.class));
                break;
            case R.id.cl_refresh_layout:
                startActivity(new Intent(this, RefreshLayoutActivity.class));
                break;
            case R.id.cl_round_corner_progress_bar:
                startActivity(new Intent(this, RoundCornerProgressBarActivity.class));
                break;
            case R.id.cl_item_layout:
                startActivity(new Intent(this, ItemLayoutActivity.class));
                break;
            case R.id.cl_v_scroll_screen:
                startActivity(new Intent(this, VScrollScreenLayoutActivity.class));
                break;
            case R.id.cl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }
}
