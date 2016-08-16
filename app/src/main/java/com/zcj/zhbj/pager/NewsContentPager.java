package com.zcj.zhbj.pager;

import android.app.Activity;

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

import java.util.ArrayList;

/**
 * 新闻中心
 */
public class NewsContentPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mPagers;//4个菜单详情页的集合
    private NewsData data;

    public NewsContentPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(true);
        //发送服务器请求
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
                parseData(result);
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
        //刷新侧边栏数据
        MainActivity mainActivity= (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setDate(data);
        //准备4个菜单详情页
        mPagers=new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mainActivity,data.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mainActivity));
        mPagers.add(new PhotosMenuDetailPager(mainActivity));
        mPagers.add(new InteractMenuDetailPager(mainActivity));
        setCurrentMenuDetailPager(0);
    }
    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetailPager pager=mPagers.get(position);
        flContent.removeAllViews();
        flContent.addView(pager.mRootView);
        NewsData.NewsMenuData newsMenuData = data.data.get(position);
        tvTitle.setText(newsMenuData.title);
        pager.initData();
    }
}
