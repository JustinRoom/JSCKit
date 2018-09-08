package jsc.exam.jsckit.ui;

import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.fragment.DefaultFragment;
import jsc.kit.component.baseui.fragmentmanager.FragmentBackHelper;
import jsc.kit.component.baseui.fragmentmanager.BackRecord;

public class FragmentBackHelperActivity extends BaseActivity {

    TextView tvRecord;
    FragmentBackHelper fragmentBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);
        initMenu();
        tvRecord = findViewById(R.id.tv_record);
        fragmentBackHelper = new FragmentBackHelper(getSupportFragmentManager());
    }

    private void initMenu() {
        getActionMenuView().setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragmentBackHelper.clear();
                tvRecord.setText(getBackRecords());
                return true;
            }
        });

        getActionMenuView().getMenu().add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Clear").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    public void widgetClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                if (!fragmentBackHelper.back())
                    Toast.makeText(this, "can't back", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_show_next:
                boolean returnable = new Random().nextBoolean();
                int index = new Random().nextInt(100);
                fragmentBackHelper.show(R.id.fragment_container, new DefaultFragment(), newBundle(index, returnable), returnable);
                break;
        }
        tvRecord.setText(getBackRecords());
    }

    private Bundle newBundle(int index, boolean returnable){
        String content = "fragment" + index + (returnable ? "" : "\u2000X");
        Bundle bundle = new Bundle();
        bundle.putString(DefaultFragment.EXTRA_CONTENT, content);
        return bundle;
    }

    private String getBackRecords(){
        StringBuilder builder = new StringBuilder();
        for (BackRecord r : fragmentBackHelper.getBackRecordStack()) {
            Bundle bundle = r.getBundle();
            if (bundle != null){
                builder.append(bundle.getString(DefaultFragment.EXTRA_CONTENT));
            } else {
                builder.append("no bundle fragment");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
