package com.zcj.zhbj.fragment;

import android.view.View;

import com.zcj.zhbj.R;

/**
 * 侧边栏fragment
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_left_menu,null);
    }
}
