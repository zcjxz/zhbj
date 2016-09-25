package com.zcj.zhbj.utils.bitmapUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.zcj.zhbj.utils.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 本地缓存图片
 */
public class LocalCacheUtils {
    /**
     * 缓存地址
     */
    public static final String CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache";


    /**
     * 从sd卡（本地）读取图片缓存
     * @param url
     * @return
     */
    public Bitmap getBitmapFromLocal(String url){
        Log.i("getBitmapFromLocal: ","从本地读取图片的缓存");
        try {
            String fileName = MD5Encoder.encode(url);
            File file=new File(CACHE_PATH,fileName);
            if (file.exists()){
                Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把图片缓存到sd卡（本地）
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url,Bitmap bitmap){
        try {
            String fileName = MD5Encoder.encode(url);
            File file=new File(CACHE_PATH,fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                //如果文件夹不存在，则创建文件夹
                parentFile.mkdir();
            }
            //将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
