package com.zcj.zhbj.TabDetailPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.zcj.zhbj.activity.NewsDetailActivity;
import com.zcj.zhbj.domain.NewsData;
import com.zcj.zhbj.domain.TabData;
import com.zcj.zhbj.global.GlobalContants;
import com.zcj.zhbj.menuDetailPager.BaseMenuDetailPager;
import com.zcj.zhbj.utils.CacheUtils;
import com.zcj.zhbj.utils.SPhelper;
import com.zcj.zhbj.utils.bitmapUtils.MyBitmapUtils;
import com.zcj.zhbj.view.RefreshListView;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class NewsTabDetailPager extends BaseTabDetailPager implements ViewPager.OnPageChangeListener{
    private NewsData.NewsTabData mTableData;
    private String mUrl;
    private TabData mTabDetailData;
    private ViewPager mViewPager;
    private BitmapUtils bitmapUtils;
//    private MyBitmapUtils bitmapUtils;
    private TextView tvTitle;
    private ArrayList<TabData.TopNewsData> mTopnews;//头条新闻的数据
    private CirclePageIndicator mIndicator;//头条新闻的位置指示器
    private RefreshListView mlvList;
    private ArrayList<TabData.TabNewsData> mNewsList;//新闻列表的数据
    private String mMoreUrl;
    private NewsListAdapter mNewsListAdapter;
    private String readIds;
    private Handler mHandler=null;
    private TopNewsAdapter topNewsAdapter;


    public NewsTabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTableData=newsTabData;
        mUrl= GlobalContants.SERVER_URL+mTableData.url;
        readIds=SPhelper.getString(mActivity, SPhelper.READ_IDS, "");
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
        mlvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ids=mNewsList.get(position).id;
                if (!readIds.contains(ids)) {
                    readIds = readIds + ids + ",";
                    SPhelper.putString(mActivity, SPhelper.READ_IDS, readIds);
                }
//                mNewsListAdapter.notifyDataSetChanged();//刷新整个列表性能较低
                changeReadState(view);//局部View的界面刷新
                //跳转新闻详情页
                intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        return inflate;
    }

    /**
     * 改变已读新闻的颜色
     * @param view  被点击的item的View
     */
    private void changeReadState(View view) {
        TextView readTextView = (TextView) view.findViewById(R.id.tv_title);
        readTextView.setTextColor(Color.DKGRAY);
    }

    private void getMoreData() {
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
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)){
            parseData(cache,false);
        }
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
                CacheUtils.setCache(mUrl,result,mActivity);
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

        String more=mTabDetailData.data.more;
        if (!TextUtils.isEmpty(more)){
            mMoreUrl = GlobalContants.SERVER_URL+more;
        }else{
            mMoreUrl=null;
        }
        if (!isMore){
            mTopnews = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;
            if(mNewsList!=null){
                topNewsAdapter = new TopNewsAdapter();
                mViewPager.setAdapter(topNewsAdapter);
                mIndicator.setOnPageChangeListener(this);
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//支持快照显示
                mIndicator.onPageSelected(0);//让指示器重新定位到第一个点
                tvTitle.setText(mTopnews.get(0).title);
            }
            if (mNewsList!=null){
                mNewsListAdapter=new NewsListAdapter();
                mlvList.setAdapter(mNewsListAdapter);
            }
            //新闻轮播
            if (mHandler==null){
                mHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(mActivity==null){
                            Log.i("handleMessage: ","activity已经结束");

                            return;
                        }
                        int currentItem = mViewPager.getCurrentItem();
                        if(currentItem<mTopnews.size()-1){
                            currentItem++;
                        }else{
                            currentItem=0;
                        }
//                        Log.i("handleMessage: ","current    "+currentItem);
//                        mViewPager.getAdapter().notifyDataSetChanged();
//                        Log.i("handleMessage:    ",mTopnews.toString());
                        mIndicator.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,3000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);
            }
        }else{
            //如果要加载下一页，需要将数据追加给原来的数据集合
            ArrayList<TabData.TabNewsData> news=mTabDetailData.data.news;
            mNewsList.addAll(news);
//            topNewsAdapter.notifyDataSetChanged();
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

    private class TopNewsAdapter extends PagerAdapter{

        public TopNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
//            bitmapUtils = new MyBitmapUtils();
            //设置默认载入图片
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopnews.size();
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
    private class NewsListAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent){
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
            if (readIds.contains(getItem(position).id)){
                holder.tvTitle.setTextColor(Color.DKGRAY);
            }else{
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }
    private class ViewHolder{
        ImageView ivPic;
        TextView tvTitle;
        TextView tvDate;
    }
}
