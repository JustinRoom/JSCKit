package jsc.kit.component.widget.spacelickable;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class SpaceClickHelper<V extends ViewGroup> {
    private V parent;
    private float downX = 0.0f;
    private float downY = 0.0f;
    private long downTimeStamp = 0L;
    private RectF outRectF = new RectF();
    private Rect outRect = new Rect();
    private OnSpaceClickListener<V> onSpaceClickListener = null;

    public SpaceClickHelper(@NonNull V parent) {
        this.parent = parent;
    }

    public void setOnSpaceClickListener(OnSpaceClickListener<V> onSpaceClickListener) {
        this.onSpaceClickListener = onSpaceClickListener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTimeStamp = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float upX = event.getX();
                float upY = event.getY();
                if (onSpaceClickListener != null && System.currentTimeMillis() - downTimeStamp < 2000) {
                    if (touchedInChildren(downX, downY) && touchedInChildren(upX, upY))
                        onSpaceClickListener.onSpaceClick(parent);
                }
                break;
        }
        return true;
    }

    private boolean touchedInChildren(float touchX, float touchY){
        int childCount = parent.getChildCount();
        boolean isTouchInside = false;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child.getVisibility() != View.VISIBLE)
                continue;

            if (touchedInChild(child, touchX, touchY)){
                isTouchInside = true;
                break;
            }
        }
        return !isTouchInside;
    }

    private boolean touchedInChild(View child, float touchX, float touchY){
        child.getDrawingRect(outRect);
        outRect.offset(child.getLeft(), child.getTop());
        outRectF.set(outRect);
        return outRectF.contains(touchX, touchY);
    }
}
