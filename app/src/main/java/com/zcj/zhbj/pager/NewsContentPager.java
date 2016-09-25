package com.zcj.zhbj.pager;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zcj.zhbj.activity.MainActivity;
import com.zcj.zhbj.domain.NewsData;
import com.zcj.zhbj.fragment.LeftMenuFragment;
import com.zcj.zhbj.global.GlobalContants;
import com.zcj.zhbj.menuDetailPager.BaseMenuDetailPager;
import com.zcj.zhbj.menuDetailPager.InteractMenuDetailPager;
import com.zcj.zhbj.menuDetailPager.NewsMenuDetailPager;
import com.zcj.zhbj.menuDetailPager.PhotosMenuDetailPager;
import com.zcj.zhbj.menuDetailPager.TopicMenuDetailPager;
import com.zcj.zhbj.utils.CacheUtils;

import java.util.ArrayList;

/**
 * 新闻中心
 */
public class NewsContentPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mPagers;//4个菜单详情页的集合
    private NewsData data;
    private BaseMenuDetailPager mCurrentMenuPager;

    public NewsContentPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(true);
        String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            //缓存不为空，直接解析数据
            parseData(cache);
        }
        //再获取数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        //使用XUtil发送网络请求
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {
            //下面两个方法在主线程运行
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.i("NewsContentPager", "获取json成功，onSuccess: "+result);
                parseData(result);
                //设置缓存
                CacheUtils.setCache(GlobalContants.CATEGORIES_URL,result,mActivity);
            }
            @Override
            public void onFailure(HttpException error, String msg) {
//                Toast.makeText(mActivity.this,msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    /**
     * 解析网络数据
     * @param result
     */
    private void parseData(String result) {
        Gson gson=new Gson();
        data = gson.fromJson(result,NewsData.class);
        Log.i("NewsContentPager", "解析数据，parseData: "+data);
        //刷新侧边栏数据
        MainActivity mainActivity= (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setDate(data);
        //准备4个菜单详情页
        mPagers=new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mainActivity,fl_menu,data.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mainActivity,fl_menu));
        mPagers.add(new PhotosMenuDetailPager(mainActivity,fl_menu));
        mPagers.add(new InteractMenuDetailPager(mainActivity,fl_menu));
        setCurrentMenuDetailPager(leftMenuFragment.mCurrentPos);
    }
    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position){
        mCurrentMenuPager = mPagers.get(position);
        flContent.removeAllViews();
        flContent.addView(mCurrentMenuPager.mRootView);
        NewsData.NewsMenuData newsMenuData = data.data.get(position);
        tvTitle.setText(newsMenuData.title);
        mCurrentMenuPager.initData();
        mCurrentMenuPager.onSwitch();
    }
    public BaseMenuDetailPager getCurrentMenuPager(){
        return  mCurrentMenuPager;
    }
}
