package jsc.kit.component.swiperecyclerview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jsc.kit.component.baseui.resizable.BaseResizableAdapter;

import static android.view.View.NO_ID;

/**
 * Link {@link BaseResizableAdapter}.
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == NO_ID) {
            return null;
        }
        return itemView.findViewById(id);
    }

    public final void addOnClickListener(@IdRes int id, View.OnClickListener listener){
        View view = findViewById(id);
        if (view != null)
            view.setOnClickListener(listener);
    }

    public final void setText(@IdRes int id, CharSequence txt) {
        TextView textView = findViewById(id);
        if (textView != null)
            textView.setText(txt);
    }

    public final void setText(@IdRes int id, @StringRes int resId) {
        TextView textView = findViewById(id);
        if (textView != null)
            textView.setText(resId);
    }

    public final void setBackgroundResource(@IdRes int id, @DrawableRes int resid) {
        View view = findViewById(id);
        if (view != null)
            view.setBackgroundResource(resid);
    }

    public final void setBackground(@IdRes int id, Drawable background) {
        View view = findViewById(id);
        if (view != null)
            view.setBackground(background);
    }

    public final void setBackgroundColor(@IdRes int id, @ColorInt int backgroundColor) {
        View view = findViewById(id);
        if (view != null)
            view.setBackgroundColor(backgroundColor);
    }

    public final void setImageResource(@IdRes int id, @DrawableRes int resId) {
        ImageView view = findViewById(id);
        if (view != null)
            view.setImageResource(resId);
    }

    public final void setImageBitmap(@IdRes int id, Bitmap bitmap) {
        ImageView view = findViewById(id);
        if (view != null)
            view.setImageBitmap(bitmap);
    }

    public final void setImageDrawable(@IdRes int id, Drawable drawable) {
        ImageView view = findViewById(id);
        if (view != null)
            view.setImageDrawable(drawable);
    }
}
