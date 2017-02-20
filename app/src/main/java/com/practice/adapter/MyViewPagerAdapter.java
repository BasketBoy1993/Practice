package com.practice.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.practice.R;

/**
 * Created by Administrator on 2017/2/20.
 */
public class MyViewPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private int[] datas;

    public MyViewPagerAdapter(Context context , int[] datas){
        this.context = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //渲染每一页的数据
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = layoutInflater.inflate(R.layout.viewpager_item_layout , null);
        ImageView imageView = (ImageView) layout.findViewById(R.id.item_img);
        //设置显示的图片
        imageView.setImageResource(datas[position]);
        //添加到viewpager
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
