package com.practice.activity;

import android.support.v4.view.ViewPager;

import com.practice.R;
import com.practice.adapter.MyViewPagerAdapter;
import com.practice.utils.LocalImages;

/**
 * Created by Administrator on 2017/2/20.
 */
public class ViewPagerActivity extends BaseActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_viewpager;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
    }

    @Override
    public void initData() {
        //适配器初始化
        myViewPagerAdapter = new MyViewPagerAdapter(this , LocalImages.images);
        //绑定适配器
        viewPager.setAdapter(myViewPagerAdapter);
    }
}
