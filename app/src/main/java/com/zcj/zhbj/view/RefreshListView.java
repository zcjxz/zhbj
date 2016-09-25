package com.zcj.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zcj.zhbj.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

    private int startY;
    private View mHeaderView;
    private int measuredHeight;
    private static final int STATE_PULL_REFRESH=0;//下拉刷新
    private static final int STATE_RELEASE_REFRESH  =1;//松开刷新
    private static final int STATE_REFRESH=2;//正在刷新

    private int mCurrentState=0;
    private TextView title;
    private TextView time;
    private ImageView arrow;
    private ProgressBar progress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;

    OnRefreshListener mListener;
    private int mFooterViewHeight;
    private View mFooterView;
    private boolean isLoadingMore=false;

    public RefreshListView(Context context) {
        super(context);
        initView();
    }
    private void initView(){
        initHeaderView();
        //有个bug，无法调用initFooterView()，只有在initHeaderView()里才能调用
//        initFooterView();
    }

    private void initFooterView() {
        Log.i("initFooterView: ","初始化footerView");
        mFooterView = View.inflate(getContext(), R.layout.refresh_footer, null);
        addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        this.setOnScrollListener(this);
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        Log.i("initHeaderView: ","初始化头布局");
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        title = (TextView) mHeaderView.findViewById(R.id.tv_title);
        time = (TextView) mHeaderView.findViewById(R.id.tv_time);
        arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        progress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        mHeaderView.measure(0,0);//通知系统去测量mHeaderView
        //获取mHeader的测量高度
        measuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-measuredHeight,0,0);//隐藏mHeader
        setCurrentTime();
        initArrowAnimation();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //由于子View：TopNewsViewPager请求了父控件(即本控件)不要拦截down事件，
                // 故，事件会往下传递，导致down事件无法传递到OnTouchEvent(即本方法)，
                // 无法记录到down事件的坐标，所以把记录起始坐标的任务就交给dispatchTouchEvent方法
//                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentState==STATE_REFRESH){
                    break;
                }
                if (getFirstVisiblePosition() == 0) {
                        float deltaY = ev.getRawY() - startY;
                        float paddingTop = deltaY - measuredHeight;
                        if (paddingTop > -measuredHeight) {
                            mHeaderView.setPadding(0, (int) paddingTop, 0, 0);
                            if (paddingTop >= 0 && mCurrentState == STATE_PULL_REFRESH) {
                                //从下拉刷新进入松开刷新状态
                                mCurrentState = STATE_RELEASE_REFRESH;
                                refreshState();
                            } else if (paddingTop < 0 && mCurrentState == STATE_RELEASE_REFRESH) {
                                //从松开刷新进入下拉刷新状态
                                mCurrentState = STATE_PULL_REFRESH;
                                refreshState();
                            }
                            return true;
                        }
                    }
                else{
                    startY= (int) ev.getRawY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mHeaderView.getPaddingTop()>-measuredHeight){
                    //判断mHeaderView的paddingTop，如果大于-measuredHeight，
                    // 则说明在使用下拉刷新，return true;防止响应onItemClick事件
                    if (mCurrentState==STATE_RELEASE_REFRESH){
                        mCurrentState=STATE_REFRESH;
                        mHeaderView.setPadding(0,0,0,0);
                    }else if (mCurrentState==STATE_PULL_REFRESH){
                        mHeaderView.setPadding(0,-measuredHeight,0,0);
                    }
                    refreshState();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的状态
     */
    private void refreshState() {
        switch(mCurrentState){
            case STATE_PULL_REFRESH:
                //下拉刷新
                title.setText("下拉刷新");
                arrow.setVisibility(VISIBLE);
                progress.setVisibility(INVISIBLE);
                arrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                //松开刷新
                title.setText("松开刷新");
                arrow.setVisibility(VISIBLE);
                progress.setVisibility(INVISIBLE);
                arrow.startAnimation(animUp);
                break;
            case STATE_REFRESH:
                //正在刷新
                title.setText("正在刷新");
                arrow.clearAnimation();//必须先清除动画才能隐藏
                arrow.setVisibility(INVISIBLE);
                progress.setVisibility(VISIBLE);
                if (mListener!=null){
                    mListener.onRefresh();
                }
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                    startY = (int) ev.getRawY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initArrowAnimation(){
        //箭头向上的动画
        animUp = new RotateAnimation(0,-180,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);
        //箭头向下的动画
        animDown = new RotateAnimation(-180,-360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener=listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==OnScrollListener.SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
                if(getLastVisiblePosition()==(getCount()-1)&&!isLoadingMore){
                    mFooterView.setPadding(0,0,0,0);
                    setSelection(getCount());
                    isLoadingMore=true;
                    if (mListener!=null){
                        mListener.onLoadingMore();
                    }
                }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }
    }


    public interface OnRefreshListener{
        void onRefresh();
        void onLoadingMore();
    }

    /**
     * 刷新数据后，隐藏headerView的方法
     * @param isSuccess 是否成功获取下拉刷新的数据
     */
    public void OnRefreshComplete(boolean isSuccess){
        if (isLoadingMore){
         mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadingMore=false;
        }else {
            mCurrentState = STATE_PULL_REFRESH;
            mHeaderView.setPadding(0, -measuredHeight, 0, 0);
            title.setText("下拉刷新");
            arrow.setVisibility(VISIBLE);
            progress.setVisibility(INVISIBLE);
            if (isSuccess) {
                setCurrentTime();
            }
        }
    }
    public void setCurrentTime(){
        time.setText(getCurrentTime());
    }
    public String getCurrentTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }
    OnItemClickListener mOnItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mOnItemClickListener=listener;
    }
}
