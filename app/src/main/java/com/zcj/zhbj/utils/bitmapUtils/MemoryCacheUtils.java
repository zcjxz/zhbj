package com.zcj.zhbj.utils.bitmapUtils;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;


/**
 * 内存缓存
 */
public class MemoryCacheUtils {
    private LruCache<String,Bitmap> mMemoryCache;

    public MemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory();//应用所拥有的最大内存
        mMemoryCache=new LruCache<String,Bitmap>((int) (maxMemory/8)){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int size = value.getByteCount();//获取每个图片的大小如果要兼容低版本，可使用下面的方法
//                int size = value.getRowBytes() * value.getHeight();
                return size;
            }
        };
    }

    /**
     * 从内存读取图片缓存
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url){
        Log.i("getBitmapFromMemory","从内存读取图片缓存");
        return mMemoryCache.get(url);
    }

    /**
     * 把图片缓存到内存
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap){
        mMemoryCache.put(url,bitmap);
    }
}
