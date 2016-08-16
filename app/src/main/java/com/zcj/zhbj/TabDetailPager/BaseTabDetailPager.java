package com.zcj.zhbj.TabDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zcj.zhbj.domain.NewsData;
import com.zcj.zhbj.menuDetailPager.BaseMenuDetailPager;

/**
 *
 */
public class BaseTabDetailPager extends BaseMenuDetailPager {
    NewsData.NewsTabData mTableData;
    private TextView text;

    public BaseTabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTableData=newsTabData;
    }

    @Override
    public View initView() {
        text = new TextView(mActivity);
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        return text;
    }

    @Override
    public void initData() {
        text.setText(mTableData.title);
    }
}
