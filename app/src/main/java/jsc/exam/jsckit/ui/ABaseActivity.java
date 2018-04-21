package jsc.exam.jsckit.ui;

import jsc.exam.jsckit.InstanceManager;
import jsc.kit.baseui.APermissionCheckActivity;
import jsc.kit.utils.CustomToast;

public abstract class ABaseActivity extends APermissionCheckActivity {

    public final void showCustomToast(String txt){
        CustomToast.Builder builder = new CustomToast.Builder()
                .text(txt)
                .topMargin(getActionBarSize());
        showCustomToast(builder);
    }

    public final void showCustomToast(CustomToast.Builder builder){
        CustomToast.getInstance().show(builder);
    }
}
