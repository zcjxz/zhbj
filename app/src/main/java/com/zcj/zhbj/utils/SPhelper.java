package com.zcj.zhbj.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SPhelper {
    public static String IS_GUIDE_SHOWED="is_guide_showed";
    public static String CONFIG="config";
    public static SharedPreferences sp;

    /**
     * 获取SharedPreferences对象
     * @param context
     * @return
     */
    public static SharedPreferences getSP(Context context){
        sp= context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        return sp;
    }

    /**
     * 获取SharedPreferences的boolean值
     * @param context
     * @param key       要获取的key
     * @param defValue  默认值
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        if (sp==null){
            sp=getSP(context);
        }
        return sp.getBoolean(key,defValue);
    }

    /**
     * 设置SharedPreferences的boolean值
     * @param context
     * @param key   要设置的key
     * @param value     key要设置的值
     */
    public static void putBoolean(Context context,String key,boolean value) {
        if (sp==null){
            sp=getSP(context);
        }
        sp.edit().putBoolean(key,value).commit();
    }
}
