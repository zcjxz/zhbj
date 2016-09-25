package com.zcj.zhbj.menuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 菜单详情页--专题
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {

    public TopicMenuDetailPager(Activity activity, FrameLayout menuView) {
        super(activity,menuView);
    }

    @Override
    public View initView() {
        TextView text=new TextView(mActivity);
        text.setText("专题");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
