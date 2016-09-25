package com.zcj.zhbj.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SPhelper {
    public static String IS_GUIDE_SHOWED="is_guide_showed";//是否展示过引导页
    public static String CONFIG="config";//SharedPreferences的名称
    public static SharedPreferences sp;
    public static String READ_IDS="read_ids";           //已经阅读的新闻的id
    public static String NEW_TEXT_SIZE="new_text_size";//新闻的字体大小

    /**
     * 获取SharedPreferences对象
     * @param context
     * @return
     */
    public static SharedPreferences getSP(Context context){
        sp= context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        return sp;
    }
    public static void setSP(Context context){
        if (sp==null){
            sp=getSP(context);
        }
    }
    /**
     * 获取SharedPreferences的boolean值
     * @param context
     * @param key       要获取的key
     * @param defValue  默认值
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        setSP(context);
        return sp.getBoolean(key,defValue);
    }

    /**
     * 设置SharedPreferences的boolean值
     * @param context
     * @param key   要设置的key
     * @param value     key要设置的值
     */
    public static void putBoolean(Context context,String key,boolean value) {
        setSP(context);
        sp.edit().putBoolean(key,value).commit();
    }

    public static String getString(Context context,String key,String defaultValue){
        setSP(context);
        return sp.getString(key,defaultValue);
    }

     public static void putString(Context context,String key, String value){
         setSP(context);
         sp.edit().putString(key,value).commit();
     }

    public static int getInt(Context context,String key,int defaultValue){
        setSP(context);
        return sp.getInt(key,defaultValue);
    }

    public static void putInt(Context context,String key,int value){
        setSP(context);
        sp.edit().putInt(key,value);
    }
}
