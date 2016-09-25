package com.zcj.zhbj.menuDetailPager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 菜单详情页基类
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;
    public FrameLayout mMenuView;
    public BaseMenuDetailPager(Activity activity, FrameLayout menuView) {
        mActivity=activity;
        mMenuView=menuView;
        mMenuView.removeAllViews();
        mRootView=initView();
    }

    public abstract View initView();

    public void initData(){

    }
    public void onSwitch(){
        mMenuView.removeAllViews();
    }

}
