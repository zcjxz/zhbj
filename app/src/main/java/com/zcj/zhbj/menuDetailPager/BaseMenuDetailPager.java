package com.zcj.zhbj.menuDetailPager;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;
    public BaseMenuDetailPager(Activity activity) {
        mActivity=activity;
        mRootView=initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
