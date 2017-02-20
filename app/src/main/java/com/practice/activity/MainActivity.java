package com.practice.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.practice.R;
import com.practice.thread.MyRunable;
import com.practice.thread.MyThread;
import com.practice.thread.SaleTicket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView textView;
    private ProgressBar progressBar;
    private Button button;

    private int progress = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    textView.setText("123456");
                    break;
                case 2:
                    String string = (String) msg.obj;
                    textView.setText(string);
                    break;
                case 3:
                    if (progress < 100){
                        progress += 10;
                        progressBar.setProgress(progress);
                        handler.sendEmptyMessageDelayed(3 , 2*1000);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        textView = (TextView) this.findViewById(R.id.textview);
        textView.setOnClickListener(this);
        progressBar = (ProgressBar) this.findViewById(R.id.progressbar);
        button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview:
                testThread();
                testRunable();
                testSale();
                testCache();
                testFixed();
                testSingle();
                testScheduled();

                //线程之间的通讯
                testHandler();
                testTimer();
                break;
            case R.id.button:
                this.startActivity(new Intent(this , ViewPagerActivity.class));
                break;
        }
    }

    private void testThread() {
        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();
        mt1.start();
        mt2.start();
    }

    private void testRunable(){
        MyRunable mr1 = new MyRunable();
        MyRunable mr2 = new MyRunable();
        //多线程可以共用同一个MyRunable接口对象
        //MyRunable对象只有一个，所以要考虑线程同步，同步加锁
        Thread t1 = new Thread(mr1);
        Thread t2 = new Thread(mr2);
        t1.start();
        t2.start();
    }

    private void testSale(){
        SaleTicket saleTicket = new SaleTicket();

        Thread t1 = new Thread(saleTicket , "A代理");
        Thread t2 = new Thread(saleTicket , "B代理");
        Thread t3 = new Thread(saleTicket , "C代理");
        Thread t4 = new Thread(saleTicket , "D代理");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    //缓存线程池，newCacheThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，
    // 可灵活回收空闲线程，若无可回收，则新建线程
    private void testCache(){
        ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
        for (int i=0 ; i<10 ; i++){
            final int index = i;

            //休息两秒，方便更好理解缓存线程池的灵活回收、缓存这个特性
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cacheThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.i("TAG:" , Thread.currentThread().getName()+" "+index);
                }
            });
        }
    }

    //定长线程池，newFixedThreadPool创建一个定长线程池，可控制线程最大并发数，超过的线程会在队列中等待
    private void testFixed(){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);//穿入最大并发线程数量值
        for (int i=0 ; i<10 ; i++){
            final int index = i;

            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.i("TAG:" , Thread.currentThread().getName()+" "+index);

                    //每一个线程执行完了都回休眠两秒，帮助理解定长线程池可控制线程最大并发数
                    try {
                        Thread.sleep(2*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //单线程化线程池，newSingleThreadExecutor创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
    // 保所有任务按照指定顺序（FIFO:先入先出;LIFO:后入先出）执行
    private void testSingle(){
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        for (int i=0 ; i<10 ; i++){

            final int index = i;
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.i("TAG:" , Thread.currentThread().getName()+" "+index);

                    try {
                        Thread.sleep(2*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //定长线程池，newScheduledThreadPool创建一个定长线程池，支持定时以及周期性任务执行
    private void testScheduled(){
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //定时三秒以后执行
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                Log.i("TAG:" , "delay 3 seconds");
            }
        } , 3 , TimeUnit.SECONDS);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.i("TAG:" , "delay 2 seconds , and execute exery 3 seconds");
            }
        } , 2 , 3 , TimeUnit.SECONDS);
    }

    private void testHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        });

        //用Message类作为数据的载体，handler的send方法有延迟定长时间后发送的功能
        Message message = new Message();
        message.what = 2;
        message.obj = new String("qiaozhi");
        handler.sendMessageDelayed(message , 2*1000);
    }

    private void testTimer() {
        progressBar.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(3 , 2*1000);
    }

    private void testPost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG:" , "handler post");
                    }
                });


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG:" , "handler post delay");
                    }
                } , 2*1000);


                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("view post");
                    }
                });

                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("view postDelay");
                    }
                } , 2*1000);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("runOnUiThread");
                    }
                });
            }
        });
    }



    class MyTask extends AsyncTask<Void , Void , String>{

        //当前还在主线程当中，做一些准备工作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //在异步线程里面执行
        @Override
        protected String doInBackground(Void... params) {

            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String string = "qiaozhi";

            //拿一个进度条举例的话，就是通知主线程当前的进度是多少
//            publishProgress(10);

            return string;
        }

        //只有调用了publishProgress()方法后，就会调用onProgressUpdate()这个方法，
        // 该方法执行的时候，就切换到了主线程当中了，可以根据传递的参数做UI的更新
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //切换到主线程里面执行
        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }

}
