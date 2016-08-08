package com.zcj.zhbj.Fragment;

import android.view.View;

import com.zcj.zhbj.R;

/**
 * 主页面fragment
 */
public class MainFragment extends BaseFragment {
    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_main,null);
    }
}
