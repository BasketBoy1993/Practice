package com.practice.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/2/14.
 */

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();
}
