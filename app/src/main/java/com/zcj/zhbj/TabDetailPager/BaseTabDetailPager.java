package com.zcj.zhbj.TabDetailPager;


import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

public abstract class BaseTabDetailPager {
    public Activity mActivity;
    public View mRootView;
    public BaseTabDetailPager(Activity activity) {
        mActivity=activity;
        mRootView=initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
