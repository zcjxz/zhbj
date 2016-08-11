package com.zcj.zhbj.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zcj.zhbj.fragment.LeftMenuFragment;
import com.zcj.zhbj.fragment.MainFragment;
import com.zcj.zhbj.R;

public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_MAIN = "fragment_main";
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        initFragment();
    }
    private void initFragment(){
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
        transaction.replace(R.id.fl_main,new MainFragment(),FRAGMENT_MAIN);
        transaction.commit();
    }
}
