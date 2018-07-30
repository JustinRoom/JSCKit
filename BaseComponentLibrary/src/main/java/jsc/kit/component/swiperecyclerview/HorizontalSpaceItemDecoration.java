package jsc.kit.component.swiperecyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    int leftSpace;
    int topSpace;
    int rightSpace;
    int bottomSpace;

    public HorizontalSpaceItemDecoration(int topSpace) {
        this(0, topSpace, 0, 0);
    }

    public HorizontalSpaceItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = leftSpace;
        if (parent.getChildAdapterPosition(view) > 0){
            outRect.top = topSpace;
        }
        outRect.right = rightSpace;
        if (parent.getAdapter() != null
                && parent.getAdapter().getItemCount() > 0
                && parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount() -1){
            outRect.bottom = bottomSpace;
        }
    }
}
