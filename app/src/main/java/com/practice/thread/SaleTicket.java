package com.practice.thread;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/17.
 */

public class SaleTicket implements Runnable {

    private int ticket = 20;

    @Override
    public void run() {
        while (true){
            //对多个线程要操作同一个地方的资源加锁
            synchronized (this){
                if (ticket > 0){
                    Log.i("TAG:" , Thread.currentThread().getName()+"卖出了第"+(21-ticket)+"张票");
                    ticket--;
                }
                //票卖完了
                else{
                    break;
                }
            }

            //每卖一张票休息一会儿
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
