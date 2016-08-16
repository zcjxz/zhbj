package com.zcj.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zcj.zhbj.R;
import com.zcj.zhbj.pager.BasePager;
import com.zcj.zhbj.pager.GovAffairsPager;
import com.zcj.zhbj.pager.HomePager;
import com.zcj.zhbj.pager.NewsContentPager;
import com.zcj.zhbj.pager.SettingPager;
import com.zcj.zhbj.pager.SmartServerPager;

import java.util.ArrayList;

/**
 * 主页面fragment
 */
public class  MainFragment extends BaseFragment {

    private View contentView;
    //xUtil注解，就不用findViewById（）了
    @ViewInject(R.id.rg_group)
    private RadioGroup rgGroup;
    @ViewInject(R.id.vp_main)
    private ViewPager mViewPage;
    private ArrayList<BasePager> mPagerList;

    @Override
    public View initView() {
        contentView = View.inflate(mActivity, R.layout.fragment_main, null);
//        rgGroup = (RadioGroup) contentView.findViewById(R.id.rg_group);
        ViewUtils.inject(this, contentView);
        return contentView;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);//默认选中首页
        mPagerList = new ArrayList<BasePager>();
//        for (int i = 0; i < 5; i++) {
//            BasePager pager=new BasePager(mActivity);
//            mPagerList.add(pager);
//        }
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsContentPager(mActivity));
        mPagerList.add(new SmartServerPager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));
        mViewPage.setAdapter(new ContentAdapter());
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPage.setCurrentItem(0, false);//false为不显示动画
                        break;
                    case R.id.rb_news:
                        mViewPage.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPage.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPage.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPage.setCurrentItem(4, false);
                        break;
                }
            }
        });
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();//初始化选中页面的数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化首页数据
        mPagerList.get(0).initData();
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagerList.get(position);
            container.addView(basePager.mRootView);
            //不要再这里初始化数据，viewPager会自动加载下一页，造成业务逻辑的混乱
            //自动加载下一页还会造成  用户流量 和 资源  的浪费
            //所以把初始化数据的逻辑写在viewPager的监听事件里
//            basePager.initData();
            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    public NewsContentPager getNewsContentPager(){
        return (NewsContentPager) mPagerList.get(1);
    }
}
