package jsc.exam.jsckit.ui.componet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.ABaseActivity;

public class JSCRoundCornerProgressBarActivity extends ABaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsc_round_corner_progress_bar);
        setTitle(getClass().getSimpleName().replace("Activity", ""));
    }
}
