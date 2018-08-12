package jsc.kit.component.baseui.basemvp2;

import android.support.annotation.NonNull;

/**
 * Created by Justin Qin on 8/12/2018.
 */

public interface TemplateContract {
    public interface Presenter {
        public void attach(@NonNull View v);
        public boolean isAttached();
        public View view();
    }
    public interface View {

    }
}
