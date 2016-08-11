package com.zcj.zhbj.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zcj.zhbj.R;

/**
 * 主页的5个子页面的基类
 */
public class BasePager {
    public Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public FrameLayout flContent;

    public BasePager(Activity activity) {
        mActivity=activity;
        initView();
    }
    public void initView(){
        mRootView = View.inflate(mActivity, R.layout.pager_base, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
    }
    public void initData(){

    }
}
