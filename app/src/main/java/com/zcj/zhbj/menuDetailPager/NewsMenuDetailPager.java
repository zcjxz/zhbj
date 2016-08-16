package com.zcj.zhbj.menuDetailPager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.viewpagerindicator.TabPageIndicator;
import com.zcj.zhbj.R;
import com.zcj.zhbj.TabDetailPager.BaseTabDetailPager;
import com.zcj.zhbj.domain.NewsData;

import java.util.ArrayList;

/**
 * 菜单详情页--新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager mViewPager;
    private ArrayList<BaseTabDetailPager> mPagerList;
    private ArrayList<NewsData.NewsTabData> mNewsTabData;//页签的网络数据
    private TabPageIndicator indicator;
    private ImageButton ibNextPager;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData=children;
    }

    @Override
    public View initView() {
        View newsMenuDetail = View.inflate(mActivity, R.layout.news_menu_detail, null);
        mViewPager = (ViewPager) newsMenuDetail.findViewById(R.id.vp_news_detail);
        indicator = (TabPageIndicator) newsMenuDetail.findViewById(R.id.indicator);
        ibNextPager = (ImageButton) newsMenuDetail.findViewById(R.id.ib_next_pager);
        return newsMenuDetail;
    }

    @Override
    public void initData() {
        mPagerList=new ArrayList<BaseTabDetailPager>();
        //初始化页签数据
        for (int i = 0; i < mNewsTabData.size(); i++) {
            BaseTabDetailPager pager= new BaseTabDetailPager(mActivity,mNewsTabData.get(i));
            mPagerList.add(pager);
        }
        mViewPager.setAdapter(new NewsDetailAdapter());
        //初始化indicator
        //必须在viewpager设置完adapter后才能调用，否则会报错
        indicator.setViewPager(mViewPager);

        ibNextPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }
        });
    }

    class NewsDetailAdapter extends PagerAdapter{
        //重写该方法，用于viewpagerIndicator的页签的显示
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseTabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
