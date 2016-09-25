package com.zcj.zhbj.activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.zcj.zhbj.R;
import com.zcj.zhbj.utils.SPhelper;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends Activity {

    private FrameLayout fl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        startAnimation();
    }

    private void initView() {
        fl_root = (FrameLayout) findViewById(R.id.fl_root);
    }
    private void startAnimation(){
        AnimationSet set=new AnimationSet(false);//参数是，判断是否共享插补器
        RotateAnimation rotateAnimation=new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setFillAfter(true);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        set.setDuration(2000);
        fl_root.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //跳转到下一个页面
                intoNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void intoNextPage() {
        boolean isGuideShowed = SPhelper.getBoolean(SplashActivity.this,SPhelper.IS_GUIDE_SHOWED,false);
        if (isGuideShowed){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }
        finish();
    }
}
