package jsc.exam.jsckit.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * TODO<主页面适配器>
 * 
 * @author jsc
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setFragments(List<Fragment> fragments) {
		this.mFragments = fragments;
		notifyDataSetChanged();
	}

	public List<Fragment> getFragments() {
		return mFragments;
	}

	@Override
	public Fragment getItem(int position) {
		if (mFragments != null && mFragments.size() > 0) {
			return mFragments.get(position);
		} else {
			return null;
		}
	}

	@NonNull
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public int getCount() {
		return mFragments == null ? 0 : mFragments.size();
	}

	/**
	 * 重写此方法，不做实现，可防止子fragment被回收
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}

}
