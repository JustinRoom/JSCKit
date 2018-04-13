package jsc.kit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Init it in your application's onCreate() method like this:<br/>
 * <br/>{@code BitmapCacheManager.getInstance().init();}
 * </p>
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 */
public class BitmapCacheManager {

    private Map<String, SoftReference<Bitmap>> bitmapCache = new HashMap<>();
    private volatile static BitmapCacheManager instance = null;

    private BitmapCacheManager() {
    }

    public static BitmapCacheManager getInstance() {
        if (instance == null) {
            synchronized (BitmapCacheManager.class) {
                if (instance == null)
                    instance = new BitmapCacheManager();
            }
        }
        return instance;
    }

    /**
     * Make sure that the singleton is thread_safe.
     */
    public void init() {

    }

    /**
     * Add a image file to bitmap cache.
     *
     * @param path the image file path. It will be used as the key of this bitmap cache.
     */
    public void addBitmapToCache(@NonNull String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        addBitmapToCache(path, bitmap);
    }

    /**
     * Add a bitmap to bitmap cache.
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToCache(@NonNull String key, @NonNull Bitmap bitmap) {
        SoftReference<Bitmap> bitmapSoftReference = new SoftReference<>(bitmap);
        bitmapCache.put(key, bitmapSoftReference);
    }

    /**
     * Destroy a bitmap from cache.
     *
     * @param key
     */
    public void destroyBitmapFromCache(@NonNull String key) {
        SoftReference<Bitmap> bitmapSoftReference = bitmapCache.get(key);
        if (bitmapSoftReference != null) {
            Bitmap bitmap = bitmapSoftReference.get();
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        bitmapCache.remove(key);
    }

    /**
     * Remove a bitmap from cache.
     *
     * @param key
     */
    @Nullable
    public Bitmap removeBitmapFromCache(@NonNull String key) {
        SoftReference<Bitmap> bitmapSoftReference = bitmapCache.get(key);
        bitmapCache.remove(key);
        return bitmapSoftReference == null ? null : bitmapSoftReference.get();
    }

    /**
     * Get a bitmap from cache.
     * <br/>If exist, return the cached bitmap.
     * <br/>If not exist, a bitmap will be created and added to cache.
     *
     * @param path the image file path
     * @return
     */
    @NonNull
    public Bitmap getBitmapByPath(@NonNull String path) {
        SoftReference<Bitmap> bitmapSoftReference = bitmapCache.get(path);
        Bitmap bitmap = null;
        if (bitmapSoftReference == null) {
            bitmap = BitmapFactory.decodeFile(path);
            addBitmapToCache(path, bitmap);
            return bitmap;
        }

        bitmap = bitmapSoftReference.get();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(path);
            addBitmapToCache(path, bitmap);
            return bitmap;
        }

        return bitmap;
    }

    /**
     * Clear all bitmap cache.
     */
    public void clear() {
        for (String s : bitmapCache.keySet()) {
            SoftReference<Bitmap> bitmapSoftReference = bitmapCache.get(s);
            Bitmap bitmap = bitmapSoftReference.get();
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        bitmapCache.clear();
        //加快内存的回收
        System.gc();
    }
}
