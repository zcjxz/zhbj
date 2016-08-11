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
public class MainFragment extends BaseFragment {

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
        ViewUtils.inject(this,contentView);
        return contentView;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);//默认选中首页
        mPagerList=new ArrayList<BasePager>();
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
    }
    class ContentAdapter extends PagerAdapter{

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
            BasePager basePager = mPagerList.get(position);
            container.addView(basePager.mRootView);
            basePager.initData();
            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
