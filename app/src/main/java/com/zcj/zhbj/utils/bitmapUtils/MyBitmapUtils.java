package com.zcj.zhbj.utils.bitmapUtils;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zcj.zhbj.R;


/**
 * 自定义图片加载工具
 */
public class MyBitmapUtils {
    NetCacheUtils mNetCacheUtils;
    LocalCacheUtils mLocalCacheUtils;
    MemoryCacheUtils mMemoryCacheUtils;
    public MyBitmapUtils() {
        mMemoryCacheUtils =new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }
    public void display(ImageView ivPic, String ImageUrl){
        ivPic.setImageResource(R.mipmap.news_pic_default);
        Bitmap bitmap=null;
        //从内存读取
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(ImageUrl);
        if (bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            return;
        }
        //从本地读取
        bitmap = mLocalCacheUtils.getBitmapFromLocal(ImageUrl);
        if (bitmap!=null){
            //从本地读取图片缓存
            ivPic.setImageBitmap(bitmap);
            //把图片保存到内存
            mMemoryCacheUtils.setBitmapToMemory(ImageUrl,bitmap);
            return;
        }
        //从网络读取
        mNetCacheUtils.getBitmapFormNet(ivPic,ImageUrl);
    }
}
