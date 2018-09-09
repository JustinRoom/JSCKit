package jsc.exam.jsckit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ActionMenuView;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.fragment.DefaultFragment;
import jsc.kit.component.baseui.fragmentmanager.BackRecord;
import jsc.kit.component.baseui.fragmentmanager.FragmentBackHelper;

public class FragmentBackHelperActivity2 extends BaseActivity {

    TextView tvRecord;
    ArrayMap<Integer, String> contentCache = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        tvRecord = findViewById(R.id.tv_record);
        findViewById(R.id.btn_back).setVisibility(View.GONE);
    }

    int index = -1;

    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (!getSupportFragmentManager().popBackStackImmediate())
                    Toast.makeText(this, "can't back", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_show_next:
                index++;
                showNext1();
//                showNext2();
                break;
        }
        tvRecord.setText(getBackRecords());
    }

    private void showNext1(){
        Fragment fragment = new DefaultFragment();
        //绑定data
        String content = "fragment" + index;
        Bundle bundle = new Bundle();
        bundle.putString(DefaultFragment.EXTRA_CONTENT, content);
        fragment.setArguments(bundle);
        contentCache.put(index, content);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showNext2(){
        boolean returnable = new Random().nextBoolean();
        Fragment fragment = new DefaultFragment();
        //绑定data
        String content = "fragment" + index + (returnable ? "" : "\u2000X");
        Bundle bundle = new Bundle();
        bundle.putString(DefaultFragment.EXTRA_CONTENT, content);
        fragment.setArguments(bundle);
        contentCache.put(index, content);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (returnable)
            transaction.addToBackStack(null);
        else
            transaction.disallowAddToBackStack();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private String getBackRecords() {
        StringBuilder builder = new StringBuilder();
        for (int key : contentCache.keySet()) {
            builder.append(contentCache.get(key));
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
