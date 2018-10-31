package jsc.kit.component.baseui.fragmentmanager;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Stack;

/**
 * Back stack manager for fragments.
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class FragmentBackHelper {

    private Stack<BackRecord> backRecordStack = new Stack<>();
    private FragmentManager fragmentManager;
    private Fragment currentShowFragment = null;

    public FragmentBackHelper(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void show(@IdRes int containerViewId, @NonNull Fragment fragment) {
        show(containerViewId, fragment, true);
    }

    public void show(@IdRes int containerViewId, @NonNull Fragment fragment, boolean returnable) {
        show(containerViewId, fragment, null, returnable);
    }

    /**
     * Show fragment by {@link android.support.v4.app.FragmentTransaction#replace(int, Fragment)} and create a back record.
     *
     * @param containerViewId the container view id
     * @param fragment        fragment
     * @param bundle          bundle
     * @param returnable      true, a returnable step, otherwise false.
     */
    public void show(@IdRes int containerViewId, @NonNull Fragment fragment, Bundle bundle, boolean returnable) {
        //remember the current showing fragment.
        currentShowFragment = fragment;
        //bundle data
        if (bundle != null)
            fragment.setArguments(bundle);
        else
            bundle = fragment.getArguments();
        fragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
        backRecordStack.push(new BackRecord(containerViewId, fragment, bundle, returnable));
    }

    public boolean back() {
        if (backRecordStack.empty())
            return false;

        //back current step record
        backRecordStack.pop();
        if (currentShowFragment != null) {
            fragmentManager.beginTransaction().remove(currentShowFragment).commit();
            currentShowFragment = null;
        }
        //get the last returnable step record
        BackRecord lastReturnableBackRecord = getLastReturnableStepRecord();
        if (lastReturnableBackRecord != null) {
            Fragment tempFragment = lastReturnableBackRecord.createInstanceIfNecessary();
            if (tempFragment != null) {
                currentShowFragment = tempFragment;
                fragmentManager.beginTransaction()
                        .replace(lastReturnableBackRecord.getContainerViewId(), currentShowFragment)
                        .commit();
            }
        }
        return true;
    }

    public boolean canGoBack(){
        return !backRecordStack.empty();
    }

    /**
     * Get the current showing fragment.
     *
     * @return the current showing fragment
     */
    @Nullable
    public Fragment getCurrentShowFragment() {
        return currentShowFragment;
    }

    /**
     * Get fragment back stack.
     *
     * @return the back stack of fragments
     */
    public Stack<BackRecord> getBackRecordStack() {
        return backRecordStack;
    }

    /**
     * Clear back stack except the current showing fragment.
     * @see #clear(boolean)
     */
    public void clear() {
        clear(true);
    }

    /**
     * Clear back stack except the current showing fragment.
     * @param keepCurrentFragment true, keep current showing fragment, else remove.
     */
    public void clear(boolean keepCurrentFragment) {
        if (backRecordStack.empty())
            return;
        BackRecord top = backRecordStack.peek();
        backRecordStack.clear();
        if (keepCurrentFragment) {
            //if current showing fragment is not null, it's step record will not be removed.
            if (currentShowFragment != null)
                backRecordStack.push(top);
        } else {
            fragmentManager.beginTransaction().remove(currentShowFragment).commit();
        }
    }

    @Nullable
    private BackRecord getLastReturnableStepRecord() {
        if (backRecordStack.isEmpty())
            return null;

        //get the top of stack
        BackRecord record = backRecordStack.pop();
        if (!record.isReturnable()) {
            return getLastReturnableStepRecord();
        }
        return backRecordStack.push(record);
    }
}
