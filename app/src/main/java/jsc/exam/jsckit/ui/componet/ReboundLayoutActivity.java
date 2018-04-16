package jsc.exam.jsckit.ui.componet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import jsc.exam.jsckit.R;
import jsc.kit.itemlayout.JSCItemLayout;

public class ReboundLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebound_layout);
        setTitle(getClass().getSimpleName().replace("Activity", ""));
    }

    public void widgetClick(View view){
        if (view instanceof JSCItemLayout)
            Toast.makeText(this, ((JSCItemLayout) view).getLabel(), Toast.LENGTH_SHORT).show();
    }
}
