package jsc.kit.component.utils;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.ArrayMap;
import android.view.inputmethod.EditorInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link android.widget.EditText#setKeyListener(KeyListener)}
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class LetterNumberKeyListener extends NumberKeyListener {

    public final static int TYPE_NUMBER_SIGNED = 0x10;
    public final static int TYPE_NUMBER_DECIMAL = 0x11;
    public final static int TYPE_LETTER_NUMBER = 0x12;
    @IntDef({TYPE_NUMBER_SIGNED, TYPE_NUMBER_DECIMAL, TYPE_LETTER_NUMBER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MyInputType{

    }

    private static ArrayMap<Integer, InputTypeItem> arrayMap = new ArrayMap<>();
    static {
        //0
        InputTypeItem item1 = new InputTypeItem();
        item1.inputType = EditorInfo.TYPE_CLASS_NUMBER|EditorInfo.TYPE_NUMBER_FLAG_SIGNED;
        item1.input = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        arrayMap.put(TYPE_NUMBER_SIGNED, item1);
        //1
        InputTypeItem item2 = new InputTypeItem();
        item2.inputType = EditorInfo.TYPE_CLASS_NUMBER|EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;
        item2.input = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'
        };
        arrayMap.put(TYPE_NUMBER_DECIMAL, item2);
        //2
        InputTypeItem item3 = new InputTypeItem();
        item3.inputType = EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        item3.input = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };
        arrayMap.put(TYPE_LETTER_NUMBER, item3);
    }

    private int type = -1;

    public LetterNumberKeyListener() {
        this(TYPE_LETTER_NUMBER);
    }

    public LetterNumberKeyListener(@MyInputType int type) {
        this.type = type;
    }

    @NonNull
    @Override
    protected char[] getAcceptedChars() {
        return arrayMap.get(type).input;
    }

    @Override
    public int getInputType() {
        return arrayMap.get(type).inputType;
    }

    public static class InputTypeItem {
        int inputType;
        char[] input;
    }
}
