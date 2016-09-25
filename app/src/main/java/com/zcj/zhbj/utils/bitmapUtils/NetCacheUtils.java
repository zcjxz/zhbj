package com.zcj.zhbj.utils.bitmapUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络缓存
 */
public class NetCacheUtils {

    private  LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils mLocalCacheUtils, MemoryCacheUtils mMemoryCacheUtils) {
        this.mLocalCacheUtils = mLocalCacheUtils;
        this.mMemoryCacheUtils=mMemoryCacheUtils;
    }

    /**
     * 从网络上下载图片
     * @param ivPic
     * @param url 图片的链接
     */
    public void getBitmapFormNet(ImageView ivPic, String url) {
        new BitmapTask(ivPic,url).execute(ivPic,url);//启动AsyncTask
    }

    /**
     *  三个泛型：
     *  1。参数类型
     *  2。更新进度的泛型
     *  3。onPostExecute的返回结果
     */
    class BitmapTask extends AsyncTask<Object,Void,Bitmap>{
        /**
         * 这两个一定要放在这个类里面，
         * 因为 NetCacheUtils类 只实例化了一次，
         * 如果放在外面，整个 NetCacheUtils类 里面只存在一个ImageView对象，
         * setImageBitmap()的时候就只有一个ImageView被设置了图片。
         * 并且是最后一个。
         */

        private ImageView ivPic;
        private String url;

        public BitmapTask(ImageView ivPic, String url) {
            Log.i("BitmapTask: ","从网络读取图片");
            this.ivPic = ivPic;
            this.url = url;
        }

        //初始化，在doInBackground()之前调用
        @Override
        protected void onPreExecute() {
            ivPic.setTag(url);
        }

        /**
         * 子线程运行
         * 后台耗时方法在此执行
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            ivPic = (ImageView) params[0];
            url = (String) params[1];
            return downLoadBitmap(url);
        }
        /**
         * 主线程运行
         * 更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
        }

        /**
         * 主线程运行
         * 耗时操作执行完后执行该方法
         * @param result
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result!=null){
                String bindurl = (String) ivPic.getTag();
                if (url.equals(bindurl)){
                    //确保图片设置给了正确的imageView
                    ivPic.setImageBitmap(result);
                    //把图片缓存到本地
                    mLocalCacheUtils.setBitmapToLocal(url,result);
                    //把图片缓存到内存
                    mMemoryCacheUtils.setBitmapToMemory(url,result);
                }
            }
        }
    }
    private Bitmap downLoadBitmap(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode==200){
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                connection.disconnect();
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
