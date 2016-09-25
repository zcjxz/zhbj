package com.zcj.zhbj.menuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 菜单详情页--新闻
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {

    public InteractMenuDetailPager(Activity activity, FrameLayout menuView) {
        super(activity,menuView);
    }

    @Override
    public View initView() {
        TextView text=new TextView(mActivity);
        text.setText("互动");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
