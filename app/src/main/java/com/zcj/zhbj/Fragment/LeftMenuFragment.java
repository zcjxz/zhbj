package com.zcj.zhbj.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zcj.zhbj.R;
import com.zcj.zhbj.activity.MainActivity;
import com.zcj.zhbj.domain.NewsData;
import com.zcj.zhbj.pager.NewsContentPager;

import java.util.ArrayList;

/**
 * 侧边栏fragment
 */
public class LeftMenuFragment extends BaseFragment {

    private ListView lvList;
    private ArrayList<NewsData.NewsMenuData> mMenuList;
    private int mCurrentPos;//当前被点击的菜单项
    private MenuAdapter adapter;
    private MainActivity mainActivity;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        lvList = (ListView) view.findViewById(R.id.lv_list);
        return view;
    }

    @Override
    public void initData() {

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos=position;
                adapter.notifyDataSetChanged();

                setCurrentMenuDetailPager(position);

                toggleSlidingMenu();
            }
        });
    }

    private void toggleSlidingMenu() {
        mainActivity.getSlidingMenu().toggle();//toggle()函数：slingMenu显示时隐藏，隐藏时显示
    }


    /**
     * 设置菜单详情页
     * @param position
     */
    private void setCurrentMenuDetailPager(int position) {
        mainActivity = (MainActivity) mActivity;
        MainFragment mainFragment = mainActivity.getMainFragment();
        NewsContentPager newsContentPager = mainFragment.getNewsContentPager();
        newsContentPager.setCurrentMenuDetailPager(position);
    }

    //设置网络数据
    public void setDate(NewsData data){
        mMenuList = data.data;
        adapter = new MenuAdapter();
        lvList.setAdapter(adapter);
    }

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_menu_item, null);
            TextView tvMenuTab = (TextView) view.findViewById(R.id.tv_menu_tab);
            NewsData.NewsMenuData newsMenuData = (NewsData.NewsMenuData) getItem(position);
            tvMenuTab.setText(newsMenuData.title);
            if (position==mCurrentPos){
                tvMenuTab.setEnabled(true);
            }else{
                tvMenuTab.setEnabled(false);
            }
            return view;
        }
    }
}
