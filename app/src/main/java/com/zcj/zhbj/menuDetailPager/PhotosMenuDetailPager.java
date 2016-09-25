package com.zcj.zhbj.menuDetailPager;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zcj.zhbj.R;
import com.zcj.zhbj.domain.PhotosData;
import com.zcj.zhbj.global.GlobalContants;
import com.zcj.zhbj.utils.CacheUtils;
import com.zcj.zhbj.utils.bitmapUtils.MyBitmapUtils;

import java.util.ArrayList;

/**
 * 菜单详情页--组图
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager {

    private View inflate;
    private ListView lvPhoto;
    private GridView gvPhoto;
    private String cache;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private PhotoAdapter mAdapter;
    private View menu;
    static final int LIST_TYPE =1;
    static final int GRID_TYPE=0;
    private int mCurrentType=1;
    private View type_list;
    private View type_grid;

    public PhotosMenuDetailPager(Activity activity,FrameLayout menuView) {
        super(activity,menuView);
    }

    @Override
    public View initView() {
        inflate = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        menu = View.inflate(mActivity, R.layout.photos_menu, null);
        type_list = menu.findViewById(R.id.type_list);
        type_grid = menu.findViewById(R.id.type_grid);
        lvPhoto = (ListView) this.inflate.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) this.inflate.findViewById(R.id.gv_photo);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentType== LIST_TYPE){
                    mCurrentType=GRID_TYPE;
                }else{
                    mCurrentType= LIST_TYPE;
                }
                setMenuType(mCurrentType);
            }
        });

        return this.inflate;
    }

    @Override
    public void onSwitch() {
        mMenuView.removeAllViews();
        mMenuView.addView(menu);
    }

    private void setMenuType(int type){
        switch (type){
            case LIST_TYPE:
                type_list.setVisibility(View.INVISIBLE);
                type_grid.setVisibility(View.VISIBLE);
                lvPhoto.setVisibility(View.VISIBLE);
                gvPhoto.setVisibility(View.INVISIBLE);
                break;
            case GRID_TYPE:
                type_list.setVisibility(View.VISIBLE);
                type_grid.setVisibility(View.INVISIBLE);
                gvPhoto.setVisibility(View.VISIBLE);
                lvPhoto.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void initData() {
        cache = CacheUtils.getCache(GlobalContants.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)){
            parseData(cache);
        }
        getDataFromServer();
        setMenuType(mCurrentType);
    }

    private void getDataFromServer() {
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result);
                CacheUtils.setCache(GlobalContants.PHOTOS_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson=new Gson();
        PhotosData data = gson.fromJson(result, PhotosData.class);
        //获取组图列表集合
        mPhotoList = data.data.news;
        if (mPhotoList!=null){
            mAdapter = new PhotoAdapter();
            lvPhoto.setAdapter(mAdapter);
            gvPhoto.setAdapter(mAdapter);
        }

    }

    class PhotoAdapter extends BaseAdapter{
        private MyBitmapUtils bitmapUtils;

        public PhotoAdapter(){
            bitmapUtils= new MyBitmapUtils();
//            bitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }


        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhotosData.PhotoInfo item = mPhotoList.get(position);
            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(mActivity,R.layout.list_photo_item,null);
                holder.ivPic= (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(item.title);

            bitmapUtils.display(holder.ivPic,item.listimage);
            return convertView;
        }
    }

    class ViewHolder{
        TextView tvTitle;
        ImageView ivPic;
    }

}
