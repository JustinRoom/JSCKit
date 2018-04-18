package jsc.exam.jsckit.ui;

import jsc.exam.jsckit.InstanceManager;
import jsc.kit.baseui.BasePermissionCheckActivity;

public abstract class ABaseActivity extends BasePermissionCheckActivity {

    public final void showCustomToast(String txt){
        InstanceManager.getInstance().getCustomToast().show(txt,getActionBarSize());
    }
}
