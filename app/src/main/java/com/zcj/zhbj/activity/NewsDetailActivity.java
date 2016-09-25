package com.zcj.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zcj.zhbj.R;
import com.zcj.zhbj.utils.SPhelper;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends Activity implements View.OnClickListener{

    private ImageButton btnShare;
    private ImageButton btnTextSize;
    private ImageButton btnBack;
    private WebView mWedView;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        String url = getIntent().getStringExtra("url");
        mWedView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnTextSize = (ImageButton) findViewById(R.id.btn_textSize);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        settings = mWedView.getSettings();
        settings.setJavaScriptEnabled(true);//开启js支持
        settings.setBuiltInZoomControls(true);//开启缩放
        settings.setUseWideViewPort(true);//支持双击缩放
        mWedView.setWebViewClient(new WebViewClient(){
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }
            //网页加载完成
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }
            //所有跳转链接都会在此方法中回调
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWedView.loadUrl(url);
        mCurrentItem=SPhelper.getInt(this,SPhelper.NEW_TEXT_SIZE,2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                showShare();
                break;
            case R.id.btn_textSize:
                showChooseDialog();
                break;
        }
    }

    private int mCurrentChooseItem;//当前选中的item


    private int mCurrentItem;//记录的当前选中的item
    private void showChooseDialog() {

        final String[] items=new String[]{
                "超大号字体","大号字体","正常字体","小号字体","超小号字体",
        };
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(NewsDetailActivity.this, items[which], Toast.LENGTH_SHORT).show();
                mCurrentChooseItem=which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWedView.getSettings();
                switch (mCurrentChooseItem){
                    case 0:
                        //该方法已经弃用，但为了兼容低版本而使用，新方法在下面
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
//                        settings.setTextZoom(20);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentItem=mCurrentChooseItem;
                SPhelper.putInt(NewsDetailActivity.this,SPhelper.NEW_TEXT_SIZE,mCurrentItem);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}