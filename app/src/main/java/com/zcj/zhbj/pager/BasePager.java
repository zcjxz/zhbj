package com.zcj.zhbj.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zcj.zhbj.R;
import com.zcj.zhbj.activity.MainActivity;

/**
 * 主页的5个子页面的基类
 */
public class BasePager {
    public Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public FrameLayout flContent;
    public ImageButton btnMenu;

    public BasePager(Activity activity) {
        mActivity=activity;
        initView();
    }
    public void initView(){
        mRootView = View.inflate(mActivity, R.layout.pager_base, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity= (MainActivity) mActivity;
                mainActivity.getSlidingMenu().toggle();
            }
        });
    }
    public void initData(){}

    /**
     * 设置侧边栏是否可用
     * @param enable    用于判断
     */
    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
