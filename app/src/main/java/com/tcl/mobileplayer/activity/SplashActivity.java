package com.tcl.mobileplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.tcl.mobileplayer.R;
import com.tcl.mobileplayer.activity.MainActivity;

public class SplashActivity extends Activity {
    private static final  String TAG= SplashActivity.class.getSimpleName();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Handler在哪里new 就在哪里执行
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //两秒钟后执行这个线程
                //执行在主线程中
                startMainActivity();
                Log.e(TAG,"运行："+Thread.currentThread().getName());
            }
        },2000);
    }

    //跳转到主页面，把当前页面关闭
    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEevent==Aciton"+event.getAction());
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
