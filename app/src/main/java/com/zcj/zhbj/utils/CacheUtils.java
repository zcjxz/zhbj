package com.zcj.zhbj.utils;

import android.content.Context;

public class CacheUtils {
    /**
     * 设置缓存
     * @param key       所需的url
     * @param value     key对应的json
     * @param context
     */
    public static void setCache(String key, String value, Context context){
        SPhelper.putString(context,key,value);
        //可以将缓存放在文件中，文件名就是url(若不支持就md5一下)，文件内容就是json
    }

    /**
     * 获取缓存
     * @param key
     * @param context
     */
    public static String getCache(String key,Context context){
        return SPhelper.getString(context,key,null);
    }
}
