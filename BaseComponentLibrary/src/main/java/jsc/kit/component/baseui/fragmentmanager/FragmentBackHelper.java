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
    private boolean keepUniqueness = false;
    private OnChangedCallBack onChangedCallBack = null;

    public FragmentBackHelper(@NonNull FragmentManager fragmentManager) {
        this(fragmentManager, false);
    }

    /**
     *
     * @param fragmentManager {@link FragmentManager}.
     * @param keepUniqueness true, there is only one instance of {@link Fragment} in the back stack.
     */
    public FragmentBackHelper(@NonNull FragmentManager fragmentManager, boolean keepUniqueness) {
        this.fragmentManager = fragmentManager;
        this.keepUniqueness = keepUniqueness;
    }

    @NonNull
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setOnChangedCallBack(OnChangedCallBack onChangedCallBack) {
        this.onChangedCallBack = onChangedCallBack;
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
        if (keepUniqueness) {
            if (hadInstanceOnTheTopOfStack(fragment.getClass())){
                return;
            }
            clearAllBackRecords(fragment.getClass());
        }
        //bundle data
        if (bundle != null)
            fragment.setArguments(bundle);
        else
            bundle = fragment.getArguments();
        doBeforeChanged(1);
        fragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
        backRecordStack.push(new BackRecord(containerViewId, fragment, bundle, returnable));
        //remember the current showing fragment.
        currentShowFragment = fragment;
        doAfterChanged(1);
    }

    public boolean back() {
        if (backRecordStack.empty())
            return false;

        //back current step record
        backRecordStack.pop();
        if (currentShowFragment != null) {
            doBeforeChanged(0);
            fragmentManager.beginTransaction().remove(currentShowFragment).commit();
            currentShowFragment = null;
            doAfterChanged(0);
        }
        //get the last returnable step record
        BackRecord lastReturnableBackRecord = getLastReturnableStepRecord();
        if (lastReturnableBackRecord != null) {
            Fragment tempFragment = lastReturnableBackRecord.createInstanceIfNecessary();
            if (tempFragment != null) {
                doBeforeChanged(1);
                fragmentManager.beginTransaction()
                        .replace(lastReturnableBackRecord.getContainerViewId(), tempFragment)
                        .commit();
                currentShowFragment = tempFragment;
                doAfterChanged(1);
            }
        }
        return true;
    }

    public boolean canGoBack() {
        if (backRecordStack.empty())
            return false;
        Stack<BackRecord> tempStack = new Stack<>();
        tempStack.addAll(backRecordStack);
        tempStack.pop();
        int returnableCount = 0;
        if (!tempStack.empty()) {
            for (BackRecord record : tempStack) {
                if (record.isReturnable())
                    returnableCount++;
            }
        }
        return returnableCount > 0;
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
     *
     * @see #clearAllBackRecords(boolean)
     */
    public void clear() {
        clearAllBackRecords(true);
    }

    /**
     * Clear back stack except the current showing fragment.
     *
     * @param keepCurrentFragment true, keep current showing fragment, else remove.
     */
    public void clearAllBackRecords(boolean keepCurrentFragment) {
        if (backRecordStack.empty())
            return;
        BackRecord top = backRecordStack.peek();
        backRecordStack.clear();
        if (keepCurrentFragment) {
            //if current showing fragment is not null, it's step record will not be removed.
            if (currentShowFragment != null)
                backRecordStack.push(top);
        } else {
            if (currentShowFragment != null){
                doBeforeChanged(0);
                fragmentManager.beginTransaction().remove(currentShowFragment).commit();
                currentShowFragment = null;
                doAfterChanged(0);
            }
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

    /**
     * Whether there already was a instance of the target fragment class on the top of stack.
     *
     * @param fragmentClazz Class
     * @return true, there was a instance of this Class on the top of back stack.
     */
    public boolean hadInstanceOnTheTopOfStack(Class<? extends Fragment> fragmentClazz){
        if (backRecordStack.empty())
            return false;
        BackRecord backRecord = backRecordStack.peek();
        return backRecord.getClzName().equals(fragmentClazz.getName());
    }

    private void clearAllBackRecords(Class<? extends Fragment> fragmentClazz){
        if (backRecordStack.empty())
            return;
        final String className = fragmentClazz.getName();
        Stack<BackRecord> tempStack = new Stack<>();
        for (BackRecord record : backRecordStack) {
            if (record.getClzName().equals(className))
                tempStack.push(record);
        }
        backRecordStack.removeAll(tempStack);
    }

    private void doBeforeChanged(int action){
        if (onChangedCallBack != null)
            onChangedCallBack.beforeChanged(action, getCurrentShowFragment());
    }

    private void doAfterChanged(int action){
        if (onChangedCallBack != null)
            onChangedCallBack.afterChanged(action, getCurrentShowFragment());
    }

    /**
     * To listener that the current showing fragment was removed or replaced.
     */
    public interface OnChangedCallBack {

        /**
         * A call back before the current showing fragment was removed or replaced.
         *
         * @param action 0:remove action, 1:replace action
         * @param currentShowFragment the current show fragment
         */
        void beforeChanged(int action, @Nullable Fragment currentShowFragment);
        /**
         * A call back after the current showing fragment was removed or replaced.
         *
         * @param action 0:remove action, 1:replace action
         * @param currentShowFragment the current show fragment
         */
        void afterChanged(int action, @Nullable Fragment currentShowFragment);
    }
}
