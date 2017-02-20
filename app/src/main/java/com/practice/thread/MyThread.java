package com.practice.thread;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/17.
 */

public class MyThread extends Thread {
    @Override
    public void run() {
        Log.i("TAG:" , Thread.currentThread().getName()+" is running ");
    }
}
