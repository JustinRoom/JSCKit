package jsc.kit.component.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class SharePreferencesUtils {

    private static volatile SharePreferencesUtils instance;
    private SharedPreferences sharedPreferences;

    private SharePreferencesUtils() {
    }

    public static SharePreferencesUtils getInstance() {
        if (instance == null) {
            synchronized (SharePreferencesUtils.class) {
                if (instance == null)
                    instance = new SharePreferencesUtils();
            }
        }
        return instance;
    }

    public void init(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    // 保存字符串
    public void saveString(String key, String value) {
        checkIsInit();
        sharedPreferences.edit().putString(key, value).apply();
    }

    // 获取字符串
    public String getString(String key, String... defValue) {
        checkIsInit();
        if (defValue.length > 0)
            return sharedPreferences.getString(key, defValue[0]);
        else
            return sharedPreferences.getString(key, "");

    }

    // 保存布尔值
    public void saveInt(String key, Integer value) {
        checkIsInit();
        sharedPreferences.edit().putInt(key, value).apply();
    }

    // 获取布尔值
    public int getInt(String key, Integer defValue) {
        checkIsInit();
        return sharedPreferences.getInt(key, defValue);
    }

    // 保存布尔值
    public void saveBoolean(String key, Boolean value) {
        checkIsInit();
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    // 获取布尔值
    public Boolean getBoolean(String key, Boolean... defValue) {
        checkIsInit();
        if (defValue.length > 0)
            return sharedPreferences.getBoolean(key, defValue[0]);
        else
            return sharedPreferences.getBoolean(key, false);

    }

    // 保存布尔值
    public void saveFloat(String key, Float value) {
        checkIsInit();
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    // 获取布尔值
    public Float getFloat(String key, Float... defValue) {
        checkIsInit();
        if (defValue.length > 0)
            return sharedPreferences.getFloat(key, defValue[0]);
        else
            return sharedPreferences.getFloat(key, 0.0f);

    }

    // 保存布尔值
    public void saveLong(String key, Long value) {
        checkIsInit();
        sharedPreferences.edit().putLong(key, value).apply();
    }

    // 获取布尔值
    public Long getLong(String key, Long... defValue) {
        checkIsInit();
        if (defValue.length > 0)
            return sharedPreferences.getLong(key, defValue[0]);
        else
            return sharedPreferences.getLong(key, 0L);

    }

    // 删除记录
    public void delete(String key) {
        checkIsInit();
        sharedPreferences.edit().remove(key).apply();
    }


    private void checkIsInit() {
        if (sharedPreferences == null)
            throw new NullPointerException("It should be initialized at first.");
    }
}
