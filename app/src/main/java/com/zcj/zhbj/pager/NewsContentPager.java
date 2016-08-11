package com.zcj.zhbj.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 新闻中心
 */
public class NewsContentPager extends BasePager {
    public NewsContentPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻");
        TextView text=new TextView(mActivity);
        text.setText("新闻中心");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        flContent.addView(text);
    }
}
