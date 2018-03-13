package jsc.exam.jsckit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = findViewById(R.id.tv_version);
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            int curVersionCode = info.versionCode;
            tvVersion.setText("Current version:" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
        }
    }
}
