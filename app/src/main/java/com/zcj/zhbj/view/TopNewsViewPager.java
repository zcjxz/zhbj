package com.zcj.zhbj.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  头条新闻的ViewPager
 */
public class TopNewsViewPager extends ViewPager{

    private int startX;
    private int startY;
    private boolean isParentIntercept=true;
//    private Timer timer;
//    private TimerTask timerTask;

    public TopNewsViewPager(Context context) {
        this(context,null);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        startTimer();
    }


    /**
     * 事件分发
     * 1，右划，并且是第一个页面，需要父控件拦截
     * 2，左划，并且是最后一个页面，需要父控件拦截
     * 3，上下滑动，需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //父控件不要拦截事件，目的是为了保证ACTION_MOVER能够响应
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = (int) ev.getX();
                startY = (int) ev.getY();
//                cancelTimer();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                if (Math.abs(endX -startX)>Math.abs(endY -startY)){
                    //左右滑动的距离大于上下滑动的距离，判断为左右滑动
                    if (endX >startX){//右划
                        if (getCurrentItem()==0){
                            parentIntercept();
                        }else{
                            parentNoIntercept();
                        }
                    }else{//左划
                        if (getCurrentItem()==(getAdapter().getCount()-1)){
                            parentIntercept();
                        }else{
                            parentNoIntercept();
                        }
                    }
                }else{
                    //上下滑动，
                    //判断是否要父类去拦截，如果要，则让父类去拦截
                    if (isParentIntercept){
                        parentIntercept();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isParentIntercept=true;
//                startTimer();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    private void  parentIntercept(){
        getParent().requestDisallowInterceptTouchEvent(false);
        isParentIntercept=true;
    }
    private void  parentNoIntercept(){
        getParent().requestDisallowInterceptTouchEvent(true);
        isParentIntercept=false;
    }

//    private void startTimer(){
//
//        if (timerTask==null) {
//            timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    mHandler.sendEmptyMessage(0);
//                }
//            };
//        }
//        if (timer==null){
//            timer = new Timer();
//        }
//        if (timer!=null&&timerTask!=null){
//            timer.schedule(timerTask,3000,3000);
//        }
//    }
//    private void cancelTimer(){
//        if (timerTask!=null){
//            timerTask.cancel();
//            timerTask=null;
//        }
//        if (timer!=null){
//            timer.cancel();
//            timer=null;
//        }
//    }
}
