package jsc.kit.component.baseui.basemvp2;

import android.support.annotation.NonNull;

/**
 * Created by Justin Qin on 8/12/2018.
 */

public interface LifecycleContract {
    public interface Presenter {
        public void attach(@NonNull View v);
        public boolean isAttached();
        public View view();
        public void start();
        public void resume();
        public void pause();
        public void stop();
        public void destroy();
    }
    public interface View {
        public void onLifecycleStart();
        public void onLifecycleResume();
        public void onLifecyclePause();
        public void onLifecycleStop();
        public void onLifecycleDestroy();
    }
}
