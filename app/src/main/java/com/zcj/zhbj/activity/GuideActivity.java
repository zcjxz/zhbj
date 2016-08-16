package com.zcj.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zcj.zhbj.R;
import com.zcj.zhbj.utils.DensityUtil;
import com.zcj.zhbj.utils.SPhelper;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    private static final int[] mImageIds=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout ll_point_group;//小圆点的父控件
    private int pointSize=10;//写入所需dp值
    private LinearLayout.LayoutParams params;
    private int mPointWidth;//小圆点间的距离
    private View point_red;
    private RelativeLayout.LayoutParams redPointParams;
    private Button bt_start;
    private int pageSize;
    private AlphaAnimation alphaShow;
    private AnimationSet setShow;
    private AnimationSet setHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
        initListener();
    }

    private void initListener() {
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GuidePageListen());
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPhelper.putBoolean(GuideActivity.this,SPhelper.IS_GUIDE_SHOWED,true);
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void initViews(){
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        point_red = (View) findViewById(R.id.view_point_red);
        bt_start = (Button) findViewById(R.id.bt_start);
        initGuideView();
        initPoint();
        initAnimation();
    }

    private void initAnimation() {
        ScaleAnimation scaleShow=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        alphaShow = new AlphaAnimation(0,1);
        setShow = new AnimationSet(false);
        setShow.addAnimation(scaleShow);
        setShow.addAnimation(scaleShow);
        setShow.setDuration(300);
        ScaleAnimation scaleHide=new ScaleAnimation(1,0,1,0,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaHide=new AlphaAnimation(1,0);
        setHide = new AnimationSet(false);
        setHide.addAnimation(scaleHide);
        setHide.addAnimation(alphaHide);
        setHide.setDuration(300);
    }

    /**
     * 初始化引导页背景图
     */
    private void initGuideView() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView=new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);
        }
        pageSize = mImageIds.length;
    }

    /**
     * 初始化小圆点
     */
    private void initPoint(){
        for (int i = 0; i < mImageIds.length; i++) {
            View point=new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            params = new LinearLayout.LayoutParams(dip2px(pointSize), dip2px(pointSize));
            if (i>0){
                params.leftMargin=dip2px(10);
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        /**
         * viewTree监听者
         */
        ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /**
             * onLayout完成后调用该方法
             */
            @Override
            public void onGlobalLayout() {
                mPointWidth = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
                ll_point_group.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }

            });
        redPointParams = (RelativeLayout.LayoutParams) point_red.getLayoutParams();
    }

    private int dip2px(int dp){
        return DensityUtil.dip2px(this, dp);
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class GuidePageListen implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int len= (int) (mPointWidth*positionOffset)+position*mPointWidth;
            redPointParams.leftMargin=len;
            point_red.setLayoutParams(redPointParams);
        }

        @Override
        public void onPageSelected(int position) {
            if (position==pageSize-1){
                bt_start.setVisibility(View.VISIBLE);
                bt_start.startAnimation(setShow);
            }else{
                if (bt_start.getVisibility()==View.VISIBLE){

                    bt_start.startAnimation(setHide);
                }
                bt_start.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
