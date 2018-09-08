package jsc.kit.component.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import jsc.kit.component.IViewAttrDelegate;

/**
 * change text sie automatically to fit it's fixed width .
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class AutoTextSizeView extends AppCompatTextView implements IViewAttrDelegate{

    public AutoTextSizeView(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public AutoTextSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public AutoTextSizeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    @Override
    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setGravity(Gravity.CENTER);
        setMaxLines(1);
    }

    public void setAutoSizeImmediatelyText(CharSequence text){
        setAutoSizeText(text, true);
    }

    public void setAutoSizeText(CharSequence text, boolean resizeImmediately){
        setText(text);
        if (resizeImmediately)
            resetTextSizeIfNecessary();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //do nothing if it's width was not changed
        if (oldw == w){
            return;
        }
        resetTextSizeIfNecessary();
    }

    private void resetTextSizeIfNecessary() {
        removeCallbacks(r);
        postDelayed(r, 20);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            loop();
        }
    };

    private void loop() {
        CharSequence text = getText();
        if (text == null || text.length() == 0)
            return;

        int maxTextWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float textWidth = getPaint().measureText(text, 0, text.length());
        float textSize = getPaint().getTextSize();
        float alphaWidth = getPaint().measureText("a");
        if (textWidth >= (maxTextWidth - alphaWidth * 2) && textWidth <= maxTextWidth) {
            //reset text
            //call it's onMeasure(int, int) and onDraw(Canvas)
            setAutoSizeText(text, false);
            return;
        }

        //here is the idea of binary search
        if (textWidth > maxTextWidth) {
            textSize = textSize + textSize / 2;
            getPaint().setTextSize(textSize / 2);
        }

        if (textWidth < maxTextWidth - alphaWidth * 2) {
            textSize = textSize + textSize * 2;
            getPaint().setTextSize(textSize / 2);
        }
        loop();
    }
}
