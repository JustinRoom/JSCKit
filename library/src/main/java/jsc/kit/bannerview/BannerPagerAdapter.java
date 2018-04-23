package jsc.kit.bannerview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 *
 * @author jiangshicheng
 */
public final class BannerPagerAdapter<T> extends PagerAdapter {

    private boolean loop = false;
    private List<T> bannerItems = new ArrayList<>();
    private OnCreateIndicatorViewListener<T> onCreateIndicatorViewListener;
    private PageAdapterItemLifeCycle<T> pageAdapterItemLifeCycle;
    private OnPageAdapterItemClickListener<T> onPageAdapterItemClickListener;
    private OnDataSetChangedListener<T> dataSetChangedListener;

    public BannerPagerAdapter() {
        this(false);
    }

    public BannerPagerAdapter(boolean loop) {
        this.loop = loop;
    }

    public void setBannerItems(List<T> bannerItems) {
        this.bannerItems.clear();
        if (bannerItems != null && !bannerItems.isEmpty())
            this.bannerItems.addAll(bannerItems);
        notifyDataSetChanged();
        if (dataSetChangedListener != null)
            dataSetChangedListener.onDataSetChanged(bannerItems, onCreateIndicatorViewListener);
    }

    @Override
    public int getCount() {
        int number = getBannerItemNumber();
        if (loop && number >= 3)
            return Integer.MAX_VALUE;
        else
            return number;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (pageAdapterItemLifeCycle != null) {
            int index = changePositionToIndex(position);
            View itemView = pageAdapterItemLifeCycle.onInstantiateItem(container, getItemAtPosition(index));
            container.addView(itemView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            itemView.setTag(index);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    if (onPageAdapterItemClickListener != null)
                        onPageAdapterItemClickListener.onPageAdapterItemClick(v, getItemAtPosition(index));
                }
            });
            return itemView;
        }
        TextView view = new TextView(container.getContext());
        view.setText("Please override PageAdapterItemLifeCycle#onInstantiateItem(ViewGroup, T) method.");
        container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (pageAdapterItemLifeCycle == null || !pageAdapterItemLifeCycle.onDestroyItem(container, object))
            container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    public int getBannerItemNumber() {
        return bannerItems.size();
    }

    public int changePositionToIndex(int position) {
        return position % getBannerItemNumber();
    }

    public T getItemAtPosition(int index) {
        return bannerItems.get(index);
    }

    public OnCreateIndicatorViewListener<T> getOnCreateIndicatorViewListener() {
        return onCreateIndicatorViewListener;
    }

    public void setOnCreateIndicatorViewListener(OnCreateIndicatorViewListener<T> onCreateIndicatorViewListener) {
        this.onCreateIndicatorViewListener = onCreateIndicatorViewListener;
    }

    public void setPageAdapterItemLifeCycle(PageAdapterItemLifeCycle<T> pageAdapterItemLifeCycle) {
        this.pageAdapterItemLifeCycle = pageAdapterItemLifeCycle;
    }

    public void setOnPageAdapterItemClickListener(OnPageAdapterItemClickListener<T> onPageAdapterItemClickListener) {
        this.onPageAdapterItemClickListener = onPageAdapterItemClickListener;
    }

    void setDataSetChangedListener(OnDataSetChangedListener<T> dataSetChangedListener) {
        this.dataSetChangedListener = dataSetChangedListener;
    }
}
