package jsc.kit.datetimepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jsc.kit.datetimepicker.R;

/**
 * Created by liuwan on 2016/9/28.
 * <br>modify by jiangshicheng
 * <br>
 * <ul>
 *  <li>attr:wh_max_text_size</li>
 *  <li>attr:wh_min_text_size</li>
 *  <li>attr:wh_max_text_alpha</li>
 *  <li>attr:wh_min_text_alpha</li>
 *  <li>attr:wh_text_color</li>
 *  <li>attr:wh_selected_text_color</li>
 *  <li>attr:wh_text_space_ratio</li>
 * </ul>
 * @author jiangshicheng
 */
public class WheelView extends View {

    /**
     * 新增字段 控制是否首尾相接循环显示 默认为循环显示
     */
    private boolean loop = true;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 10;
    private List<String> items;
    /**
     * 选中的位置
     */
    private int mCurrentSelectedIndex;
    private Paint mPaint;
    private Paint nPaint;
    private float mMaxTextSize;
    private float mMinTextSize;
    private float mMaxTextAlpha;
    private float mMinTextAlpha;
    private float mTextSpaceRatio;//text之间间距和minTextSize之比
    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean canScroll = true;
    private OnSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    private Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else {
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            }
            invalidate();
        }
    };

    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(mCurrentSelectedIndex, items.get(mCurrentSelectedIndex));
        }
    }

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView, defStyleAttr, 0);
        init(a);
        a.recycle();
    }

    private void init(TypedArray a) {
        float defaultMaxTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
        float defaultMinTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        mMaxTextSize = a.getDimension(R.styleable.WheelView_wh_max_text_size, defaultMaxTextSize);
        mMinTextSize = a.getDimension(R.styleable.WheelView_wh_min_text_size, defaultMinTextSize);
        mMaxTextAlpha = a.getInteger(R.styleable.WheelView_wh_max_text_alpha, 0xFF);
        mMinTextAlpha = a.getInteger(R.styleable.WheelView_wh_min_text_alpha, 0x78);
        mTextSpaceRatio = a.getFloat(R.styleable.WheelView_wh_text_space_ratio, 1.6f);
        int mTextColor = a.getColor(R.styleable.WheelView_wh_text_color, 0xFF333333);
        int mSelectedTextColor = a.getColor(R.styleable.WheelView_wh_selected_text_color, 0xFF59B29C);
        mMaxTextAlpha = mMaxTextAlpha > 0xFF ? 0xFF : mMaxTextAlpha;
        mMaxTextAlpha = mMaxTextAlpha < 0 ? 0 : mMaxTextAlpha;
        mMinTextAlpha = mMinTextAlpha > 0xFF ? 0xFF : mMinTextAlpha;
        mMinTextAlpha = mMinTextAlpha < 0 ? 0 : mMinTextAlpha;

        timer = new Timer();
        items = new ArrayList<>();
        //第一个paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        //第二个paint
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setStyle(Style.FILL);
        nPaint.setTextAlign(Align.CENTER);
        setColors(mTextColor, mSelectedTextColor);

        //测试数据
        for (int i = 2010; i < 2032; i++) {
            items.add(String.valueOf(i));
        }
//        mCurrentSelectedIndex = items.size() / 2;
    }

    /**
     *
     * @param listener scroll to select listener
     */
    public void setOnSelectListener(OnSelectListener listener) {
        mSelectListener = listener;
    }

    /**
     *
     * @param mMaxTextSize the max text size
     * @param mMinTextSize  the min text size
     */
    public void setTextSize(@FloatRange(from = 0) float mMaxTextSize, @FloatRange(from = 0) float mMinTextSize) {
        this.mMaxTextSize = mMaxTextSize;
        this.mMinTextSize = mMinTextSize;
        invalidate();
    }

    /**
     *
     * @param mMaxTextAlpha the max text alpha
     * @param mMinTextAlpha the min text alpha
     */
    public void setTextAlpha(@FloatRange(from = 0, to = 1.0f) float mMaxTextAlpha, @FloatRange(from = 0, to = 1.0f) float mMinTextAlpha) {
        this.mMaxTextAlpha = mMaxTextAlpha;
        this.mMinTextAlpha = mMinTextAlpha;
        invalidate();
    }

    public String getItemAtPosition(@IntRange(from = 0) int position){
        return items == null ? "" : (position < items.size() ? items.get(position) : "");
    }

    /**
     * Set items.
     * @param items data
     */
    public void setItems(List<String> items) {
        canScroll = items != null && !items.isEmpty();
        this.items = items;
        invalidate();
    }

    /**
     * Set the space between two items.
     * @param mTextSpaceRatio the vertical space ratio between two items. {@code space = mMinTextSize * mTextSpaceRatio}
     */
    public void setTextSpaceRatio(@FloatRange(from = 0) float mTextSpaceRatio) {
        this.mTextSpaceRatio = mTextSpaceRatio;
        invalidate();
    }

    /**
     * Set the selected index.
     *
     * @param selected the selected index
     */
    public void setSelected(int selected) {
        mCurrentSelectedIndex = selected;
        if (loop) {
            int distance = items.size() / 2 - mCurrentSelectedIndex;
            if (distance < 0) {
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    mCurrentSelectedIndex--;
                }
            } else if (distance > 0) {
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    mCurrentSelectedIndex++;
                }
            }
        }
        invalidate();
    }

    public int getCurrentSelectedIndex() {
        return mCurrentSelectedIndex;
    }

    public String getCurrentSelectedItem(){
        return items == null ? "" : items.get(mCurrentSelectedIndex);
    }

    private void moveHeadToTail() {
        if (loop) {
            String head = items.get(0);
            items.remove(0);
            items.add(head);
        }
    }

    private void moveTailToHead() {
        if (loop) {
            String tail = items.get(items.size() - 1);
            items.remove(items.size() - 1);
            items.add(0, tail);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        invalidate();
    }

    /**
     * @param textColor         unselected text color
     * @param selectedTextColor selected text color
     */
    public void setColors(@ColorInt int textColor, @ColorInt int selectedTextColor) {
        nPaint.setColor(textColor);
        mPaint.setColor(selectedTextColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        if (items == null || items.isEmpty())
            return;

        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 2.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(items.get(mCurrentSelectedIndex), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelectedIndex - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelectedIndex + i) < items.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    /**
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = mTextSpaceRatio * mMinTextSize * position + type * mMoveLen;
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        nPaint.setTextSize(size);
        nPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = nPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(items.get(mCurrentSelectedIndex + type * position),
                (float) (mViewWidth / 2.0), baseline, nPaint);
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;

            case MotionEvent.ACTION_UP:
                doUp();
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event){
        mMoveLen += (event.getY() - mLastDownY);
        if (mMoveLen > mTextSpaceRatio * mMinTextSize / 2) {
            if (!loop && mCurrentSelectedIndex == 0) {
                mLastDownY = event.getY();
                invalidate();
                return;
            }
            if (!loop) {
                mCurrentSelectedIndex--;
            }
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLen = mMoveLen - mTextSpaceRatio * mMinTextSize;
        } else if (mMoveLen < -mTextSpaceRatio * mMinTextSize / 2) {
            if (mCurrentSelectedIndex == items.size() - 1) {
                mLastDownY = event.getY();
                invalidate();
                return;
            }
            if (!loop) {
                mCurrentSelectedIndex++;
            }
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLen = mMoveLen + mTextSpaceRatio * mMinTextSize;
        }
        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp() {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface OnSelectListener {
        void onSelect(int index, String text);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return canScroll && super.dispatchTouchEvent(event);
    }

    /**
     * 控制内容是否首尾相连
     *
     * @param isLoop loop scroll or not
     */
    public void setIsLoop(boolean isLoop) {
        loop = isLoop;
    }

}