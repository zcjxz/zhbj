package com.zcj.zhbj.TabDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;
import com.zcj.zhbj.R;
import com.zcj.zhbj.domain.NewsData;
import com.zcj.zhbj.domain.TabData;
import com.zcj.zhbj.global.GlobalContants;
import com.zcj.zhbj.menuDetailPager.BaseMenuDetailPager;
import com.zcj.zhbj.view.RefreshListView;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class BaseTabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    NewsData.NewsTabData mTableData;
    private String mUrl;
    private TabData mTabDetailData;
    private ViewPager mViewPager;
    private BitmapUtils bitmapUtils;
    private TextView tvTitle;
    private ArrayList<TabData.TopNewsData> mTopnews;//头条新闻的数据
    private CirclePageIndicator mIndicator;//头条新闻的位置指示器
    private RefreshListView mlvList;
    private ArrayList<TabData.TabNewsData> mNewsList;//新闻列表的数据
    private String mMoreUrl;
    private NewsListAdapter mNewsListAdapter;

    public BaseTabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTableData=newsTabData;
        mUrl= GlobalContants.SERVER_URL+mTableData.url;
    }

    @Override
    public View initView() {
        View inflate = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        mViewPager = (ViewPager) headerView.findViewById(R.id.vp_news);
        tvTitle =  (TextView) headerView.findViewById(R.id.tv_title);
        mIndicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        mlvList = (RefreshListView) inflate.findViewById(R.id.lv_list);
        mlvList.addHeaderView(headerView);
        //设置下拉刷新的监听
        mlvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadingMore() {
                if (mMoreUrl!=null){
                    Log.i("onLoadingMore: ","onLoadingMore");
                    getMoreData();
                    mlvList.OnRefreshComplete(false);
                }else{
                    Toast.makeText(mActivity,"没有下一页了",Toast.LENGTH_SHORT).show();
                    mlvList.OnRefreshComplete(false);
                }
            }
        });
        return inflate;
    }

    private void getMoreData() {
        Log.i("getMoreData: ","getMoreData");
        HttpUtils utils= new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result,true);
                mlvList.OnRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mlvList.OnRefreshComplete(true);
            }
        });
    }


    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.i("test", "onSuccess: 成功获取json"+result);
                parseData(result,false);
                mlvList.OnRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "msg", Toast.LENGTH_SHORT).show();
                Log.i("test", "onFailure: 获取json失败");
                error.printStackTrace();
                mlvList.OnRefreshComplete(false);
            }
        });
    }

    /**
     * 解析数据，处理数据
     */
    private void parseData(String result,boolean isMore) {
        Gson gson=new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        mTopnews = mTabDetailData.data.topnews;
        mNewsList = mTabDetailData.data.news;
        String more=mTabDetailData.data.more;
        if (!TextUtils.isEmpty(more)){
            mMoreUrl = GlobalContants.SERVER_URL+more;
        }else{
            mMoreUrl=null;
        }
        if (!isMore){
        mViewPager.setAdapter(new TopNewsAdapter());
        mIndicator.setOnPageChangeListener(this);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setSnap(true);//支持快照显示
        tvTitle.setText(mTopnews.get(0).title);
            mNewsListAdapter=new NewsListAdapter();
        mlvList.setAdapter(mNewsListAdapter);
        }else{
            //如果要加载下一页，需要将数据追加给原来的数据集合
            ArrayList<TabData.TabNewsData> news=mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData = mTopnews.get(position);
        tvTitle.setText(topNewsData.title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class TopNewsAdapter extends PagerAdapter{

        public TopNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            //设置默认载入图片
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.drawable.topnews_item_default);
            TabData.TopNewsData topNewsData = mTopnews.get(position);
            //使用XUtils加载图片，只需要传入imageView对象和图片url
            bitmapUtils.display(imageView,topNewsData.topimage);
            container.addView(imageView);
            return imageView;
        }
    }
    class NewsListAdapter extends BaseAdapter{

        private final BitmapUtils utils;

        public NewsListAdapter() {
            utils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            TabData.TabNewsData item = getItem(position);
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(mActivity,R.layout.list_news_item,null);
                holder.ivPic= (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate= (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
            utils.display(holder.ivPic,item.listimage);
            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);
            return convertView;
        }
    }
    class ViewHolder{
        ImageView ivPic;
        TextView tvTitle;
        TextView tvDate;
    }
}
