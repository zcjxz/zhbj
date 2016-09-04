package com.zcj.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *  11个子页签的水平滑动的ViewPager，暂时不用
 */
public class HorizontalViewPager extends ViewPager{

    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentItem()!=0){//请求父类控件不要拦截触摸事件
            getParent().requestDisallowInterceptTouchEvent(true);
        }else{//如果是第一个页面，则不请求
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }

}
